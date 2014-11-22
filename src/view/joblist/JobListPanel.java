package view.joblist;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import model.DownerModel;
import shared.JobListItem;
import util.observable.CoolObservable;
import util.observable.CoolObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

public class JobListPanel
{

    private DownerModel downerModel;

    private VBox listContainer;
    private ListView<JobListItem> listView;

    private ObservableList<JobListItem> items = FXCollections.observableArrayList();
    private HashMap<Integer, JobListItem> idToItem = new HashMap<>();

    private CoolObservable<JobListItem> removeButtonObservable = new CoolObservable<>();
    private CoolObservable<JobListItem> taskClickedObservable = new CoolObservable<>();

    // itemIdToJobItemCell is a workaround to update a cell without triggering a full
    // list-update that causes flickering when using odd/even row coloring
    public HashMap<Integer, JobItemCell> itemIdToJobItemCell = new HashMap<>();


    public JobListPanel(DownerModel downerModel)
    {
        this.downerModel = downerModel;
        this.listContainer = constructListContainer();

        downerModel.addTaskListObserver(this::onListItemUpdate);
    }

    public void onListItemUpdate(ArrayList<JobListItem> updatedList)
    {
        ArrayList<JobListItem> newItems = new ArrayList<>();
        ArrayList<JobListItem> deletedItems = new ArrayList<>();
        HashMap<Integer, JobListItem> updatedItems = new HashMap<>();

        HashSet<Integer> allInputIndices = new HashSet<>();
        for(JobListItem uitem : updatedList)
        {
            allInputIndices.add(uitem.getId());
            JobListItem existingItem = idToItem.get(uitem.getId());
            if(existingItem == null)
            {
                newItems.add(uitem);
            }
            else if(!existingItem.equals(uitem))
            {
                updatedItems.put(uitem.getId(), uitem);
            }
        }

        for(JobListItem eitem : items)
        {
            if(!allInputIndices.contains(eitem.getId()))
            {
                deletedItems.add(eitem);
            }
        }

        Platform.runLater(() -> {

            // add
            for(JobListItem ji : newItems)
            {
                idToItem.put(ji.getId(), ji.copy());
            }

            if(!newItems.isEmpty())
            {
                items.addAll(newItems);
            }

            // remove
            for(JobListItem ji : deletedItems)
            {
                idToItem.remove(ji.getId());
                items.remove(ji);
            }

            // update
            for(JobListItem eitem : items)
            {
                if(updatedItems.containsKey(eitem.getId()))
                {
                    JobListItem updatedItem = updatedItems.get(eitem.getId());
                    int itemIndex = items.indexOf(eitem);
                    JobListItem itemToUpdate = items.get(itemIndex);
                    JobItemCell cellToUpdate = itemIdToJobItemCell.get(updatedItem.getId());

                    // see itemIdToJobItemCell definition-comment
                    if(cellToUpdate != null)
                    {
                        itemToUpdate.setStatus(updatedItem.getStatus());
                        itemIdToJobItemCell.get(updatedItem.getId()).updateItem(updatedItem, false);
                    }
                }
            }
        });

    }

    public void setCellReference(int jobId, JobItemCell cell)
    {
        itemIdToJobItemCell.put(jobId, cell);
    }

    public void removeCellReference(int jobId)
    {
        itemIdToJobItemCell.remove(jobId);
    }


    public VBox constructListContainer()
    {
        // list config
        listView = new ListView<>();
        listView.setItems(items);
        listView.setCellFactory(stringListView -> new JobItemCell(this));
        listView.setEditable(false);

        // Panel title
        Label title = new Label("Active Tasks");
        title.setPadding(new Insets(0, 0, 10, 0));

        // container
        VBox listContainer = new VBox();
        listContainer.setId("JobListPanel");
        listContainer.setPrefHeight(230);
        listContainer.setPrefWidth(350);

        listContainer.getChildren().add(title);
        listContainer.getChildren().add(listView);

        return listContainer;
    }


    public void onRemoveButtonClicked(CoolObserver<JobListItem> obs)
    {
        removeButtonObservable.addObserver(obs);
    }

    public void onRowClicked(CoolObserver<JobListItem> obs)
    {
        taskClickedObservable.addObserver(obs);
    }

    public VBox getListContainer()
    {
        return listContainer;
    }

    public CoolObservable<JobListItem> getTaskClickedObservable()
    {
        return taskClickedObservable;
    }

    public CoolObservable<JobListItem> getRemoveButtonObservable()
    {
        return removeButtonObservable;
    }
}
