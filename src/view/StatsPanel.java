package view;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import model.DownerModel;
import shared.JobStats;
import shared.JobStatus;

public class StatsPanel
{

    private Label status;
    private Label filesDowned;
    private Label speed;
    private Label estimationDone;


    private ProgressBar progressBar;

    private GridPane statsGrid;

    public StatsPanel(DownerModel downerModel)
    {
        this.statsGrid = constructStatsGrid();

        downerModel.addActiveStatsObserver(arg -> {
            Platform.runLater(() -> updateJobStats(arg));
        });
    }

    private void updateJobStats(JobStats jobStats)
    {
        if(jobStats != null)
        {
            status.setText(jobStats.getStatus().toString());
            progressBar.setProgress(jobStats.getPercentDone());
            filesDowned.setText(jobStats.getFiles());
            speed.setText(jobStats.getSpeed());
            estimationDone.setText(jobStats.getTime());
        }
        else
        {
            status.setText("");
            progressBar.setProgress(0);
            filesDowned.setText("");
            speed.setText("");
            estimationDone.setText("");
        }
    }


    private GridPane constructStatsGrid()
    {
        GridPane grid = new GridPane();
        grid.getColumnConstraints().add(new ColumnConstraints(120));
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(0, 15, 50, 15));
        grid.setId("jobStatsPanel");


        Label statusLabel = new Label("Status:");
        grid.add(statusLabel, 0, 1);

        status = new Label("");
        grid.add(status, 1, 1);

        progressBar = new ProgressBar(0.0);
        progressBar.setPrefWidth(200);
        grid.add(progressBar, 1, 2, 2, 1);

        Label filesDownedLabel = new Label("Files done:");
        grid.add(filesDownedLabel, 0, 3);

        filesDowned = new Label("");
        grid.add(filesDowned, 1, 3);

        Label speedLabel = new Label("Speed:");
        grid.add(speedLabel, 0, 4);

        speed = new Label("");
        grid.add(speed, 1, 4);

        Label estimationDoneLabel = new Label("Time remaining:");
        grid.add(estimationDoneLabel, 0, 5);

        estimationDone = new Label("");
        grid.add(estimationDone, 1, 5);


        return grid;
    }

    public Label getStatus()
    {
        return status;
    }

    public Label getFilesDowned()
    {
        return filesDowned;
    }

    public Label getSpeed()
    {
        return speed;
    }

    public Label getEstimationDone()
    {
        return estimationDone;
    }

    public ProgressBar getProgressBar()
    {
        return progressBar;
    }

    public GridPane getStatsGrid()
    {
        return statsGrid;
    }
}
