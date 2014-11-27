package view.joblist;

import javafx.scene.CacheHint;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.*;
import shared.JobListItem;
import shared.JobStatus;

import java.awt.*;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;

public class JobItemCell extends ListCell<JobListItem>
{

    private Label jobDescription;
    private Label status;
    private StupidImageButton stupidButton;
    private BorderPane mainPane;
    private JobListPanel jobListPanel;

    private boolean shown = false;

    public JobItemCell(JobListPanel jobListPanel)
    {
        this.jobListPanel = jobListPanel;

        // containers
        mainPane = new BorderPane();
        HBox rightContainer = new HBox();

        // leaf nodes
        jobDescription = new Label();
        status = new Label();
        stupidButton = new StupidImageButton();

        // set up structure
        rightContainer.getChildren().add(status);
        rightContainer.getChildren().add(stupidButton.getView());
        mainPane.setLeft(jobDescription);
        mainPane.setRight(rightContainer);

        setGraphic(null);
    }


    @Override
    public void updateItem(JobListItem item, boolean empty)
    {
        super.updateItem(item, empty);

        if(empty || item == null)
        {
            if(shown)
            {
                setGraphic(null);
                shown = false;
                if(item != null)
                {
                    jobListPanel.removeCellReference(item.getId());
                }
            }
        }
        else
        {
            // handle first-time showing tasks
            if(!shown)
            {
                setGraphic(mainPane);
                shown = true;

                jobListPanel.setCellReference(item.getId(), this);

                mainPane.setOnMouseClicked(mouseEvent -> jobListPanel.getTaskClickedObservable().notifyObservers(item));
                stupidButton.setOnClick(() -> jobListPanel.getRemoveButtonObservable().notifyObservers(item));
            }

            // if this cell has been marked as active, its our job to actually mark it as selected
            if(jobListPanel.getSelectedJobItemId() == item.getId() && !this.isSelected())
            {
                this.getListView().getSelectionModel().select(this.getIndex());
            }

            // update fields
            jobDescription.setText(item.getDescription());
            String statusText = item.getStatus();
            if(statusText.contains("Sleeping") && statusText.length() < "Sleeping XXs".length())
            {
                // ghetto formatting :>
                statusText = statusText.replace(" ", "   ");
            }
            statusText += "   ";
            status.setText(statusText);

        }
    }
}
