package view;

import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.*;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import model.BaseSettings;
import model.DownerModel;
import shared.JobStatus;

public class JobPanel
{

    private TextField threadUrl;
    private TextField threadName;
    private Label preview;
    private TextField basePath;

    private Button runButton;
    private Button resetButton;
    private Button selectDirButton;

    private GridPane jobGrid;
    private DownerModel downerModel;
    private CheckBox autoUpdateCheckbox;


    public JobPanel(DownerModel downerModel)
    {
        this.downerModel = downerModel;
        this.jobGrid = constructJobGrid();
    }


    private GridPane constructJobGrid()
    {
        GridPane grid = new GridPane();
        //grid.setGridLinesVisible(true);
        grid.setAlignment(Pos.TOP_LEFT);

        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(15, 15, 15, 15));
        grid.setPrefWidth(800);

        // column settings
        grid.getColumnConstraints().add(new ColumnConstraints(120)); // col 1
        ColumnConstraints constraints = new ColumnConstraints(); // col 2
        constraints.setPrefWidth(600);
        grid.getColumnConstraints().add(constraints);
        ColumnConstraints c = new ColumnConstraints(); // col 3
        c.setMinWidth(70);
        grid.getColumnConstraints().add(c);

        // tile
        Text sceneTitle = new Text("Fyrchan - based downloader");
        sceneTitle.setId("welcome-text");
        grid.add(sceneTitle, 0, 0, 2, 1);

        // thread URL
        Label threadUrlLabel = new Label("Thread URL:");
        grid.add(threadUrlLabel, 0, 1);
        threadUrl = new TextField();
        grid.add(threadUrl, 1, 1, 2, 1);

        // thread name
        Label threadNameLabel = new Label("Thread name:");
        grid.add(threadNameLabel, 0, 2);
        threadName = new TextField();
        threadName.setText(BaseSettings.baseFolderFormat);
        grid.add(threadName, 1, 2);
        resetButton = new Button("Reset");
        resetButton.setMaxWidth(100);
        grid.add(resetButton, 2, 2);

        // base path
        Label pathName = new Label("Base path:");
        grid.add(pathName, 0, 3);
        basePath = new TextField();
        basePath.setText(BaseSettings.baseOutDir);
        grid.add(basePath, 1, 3);
        selectDirButton = new Button("Select");
        selectDirButton.setMaxWidth(100);
        grid.add(selectDirButton, 2, 3);

        // path preview
        Label previewLabel = new Label("Path preview:");
        grid.add(previewLabel, 0, 4);
        preview = new Label();
        grid.add(preview, 1, 4);


        HBox actionContainer = new HBox();
        actionContainer.setAlignment(Pos.BOTTOM_LEFT);

        // start button
        runButton = new Button();
        runButton.setId("downloadButton");
        runButton.setText("Start new download");
        actionContainer.getChildren().add(runButton);


        // auto update checkbox, style in css is ignored
        autoUpdateCheckbox = new CheckBox();
        autoUpdateCheckbox.setPadding(new Insets(0, 0, 30, 30));
        autoUpdateCheckbox.setFont(Font.font(19));
        actionContainer.getChildren().add(autoUpdateCheckbox);

        Label la = new Label("Auto Update");
        la.setPadding(new Insets(0, 0, 0, -2));
        la.setFont(Font.font(18));
        actionContainer.getChildren().add(la);

        grid.add(actionContainer, 1, 5);

        return grid;
    }



    public TextField getThreadUrl()
    {
        return threadUrl;
    }

    public TextField getThreadName()
    {
        return threadName;
    }

    public Label getPreview()
    {
        return preview;
    }

    public TextField getBasePath()
    {
        return basePath;
    }

    public Button getRunButton()
    {
        return runButton;
    }

    public GridPane getJobGrid()
    {
        return jobGrid;
    }


    public Button getResetButton()
    {
        return resetButton;
    }

    public Button getSelectDirButton()
    {
        return selectDirButton;
    }

    public CheckBox getAutoUpdateCheckbox()
    {
        return autoUpdateCheckbox;
    }
}
