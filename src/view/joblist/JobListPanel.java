package view.joblist;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.*;
import model.DownerModel;
import shared.JobListItem;

public class JobListPanel
{

    private DownerModel downerModel;

    private VBox listContainer;
    private ObservableList<JobListItem> items;

    public JobListPanel(DownerModel downerModel)
    {
        this.downerModel = downerModel;
        this.listContainer = constructListContainer();
    }


    public VBox constructListContainer()
    {
        // list config
        ListView<JobListItem> listView = new ListView<>();
        items = FXCollections.observableArrayList();
        listView.setItems(items);
        listView.setCellFactory(stringListView -> new JobItemCell());

        for(int i=0; i < 10; i++)
        {
            JobListItem it = new JobListItem()
                    .setDescription("test Desc" + i*2)
                    .setStatus("status" + i);
            items.add(it);
        }

        // Panel title
        Label title = new Label("Auto Update Tasks");
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

    static class JobItemCell extends ListCell<JobListItem>
    {

        @Override
        public void updateItem(JobListItem item, boolean empty)
        {
            super.updateItem(item, empty);
            if (item == null)
                return;

            // containers
            BorderPane mainPane = new BorderPane();
            HBox rightContainer = new HBox();

            // leaf nodes
            Label jobDescription = new Label(item.getDescription());
            Label status = new Label("Status   ");
            StupidImageButton stupidButton = new StupidImageButton();

            stupidButton.setOnClick(() -> System.out.println("h?"));

            // set up structure
            rightContainer.getChildren().add(status);
            rightContainer.getChildren().add(stupidButton.getView());

            mainPane.setLeft(jobDescription);
            mainPane.setRight(rightContainer);
            setGraphic(mainPane);

        }
    }

    public VBox getListContainer()
    {
        return listContainer;
    }
}
