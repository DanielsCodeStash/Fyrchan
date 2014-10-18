package controller;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.DownerModel;
import view.JobListPanel;
import view.JobPanel;
import view.StatsPanel;

public class Fyrchan extends Application
{

    private JobPanel jobPanel;
    private JobController jobController;
    private StatsPanel statsPanel;
    private DownerModel downerModel;

    private VBox mainPanel;

    private Scene primaryScene;
    private Stage primaryStage;


    public static void main(String[] args)
    {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception
    {
        // initiate the applications model, view and controller
        downerModel = new DownerModel();
        jobPanel = new JobPanel(downerModel);
        statsPanel = new StatsPanel(downerModel);
        jobController = new JobController(jobPanel, downerModel, this);

        BorderPane mainPane = new BorderPane();


        // initiate main gui-parts
        this.primaryStage = primaryStage;
        mainPanel = new VBox();
        primaryScene = new Scene(mainPane);

        // set scene and stage options
        setSceneOptions(primaryScene);
        setStageOptions(primaryStage, primaryScene);

        // app sub-panels to our main panel
        mainPanel.getChildren().add(getMenuBar());
        mainPanel.getChildren().add(jobPanel.getJobGrid());
        mainPanel.getChildren().add(getSeparator());
        //mainPanel.getChildren().add(statsPanel.getStatsGrid());

        BorderPane pane = new BorderPane();
        pane.setId("ttest");
        pane.setLeft(statsPanel.getStatsGrid());
        pane.setRight(JobListPanel.testListView());
        pane.setMaxHeight(2000);

        mainPanel.getChildren().add(pane);

        mainPane.setTop(mainPanel);
        mainPane.setCenter(pane);

        primaryStage.setOnShown(windowEvent -> {
            jobController.onStageShown();
        });

        // show everything
        primaryStage.show();


    }

    public void setStageOptions(Stage stage, Scene scene)
    {
        stage.setTitle("Fyrchan");
        stage.getIcons().add(new Image("file:res/icon.png"));
        stage.setScene(scene);
    }

    public void setSceneOptions(Scene scene)
    {
        scene.getStylesheets().add(Fyrchan.class.getResource("../view/fyrstyle.css").toExternalForm());
    }

    public MenuBar getMenuBar()
    {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menu");
        Menu m = new Menu("File");
        Menu mm = new Menu("About");
        menuBar.getMenus().add(m);
        menuBar.getMenus().add(mm);
        return menuBar;
    }

    public Separator getSeparator()
    {
        Separator separator = new Separator();
        separator.setId("separator");
        return separator;
    }

    public Stage getPrimaryStage()
    {
        return primaryStage;
    }

    public StatsPanel getStatsPanel()
    {
        return statsPanel;
    }

    public JobPanel getJobPanel()
    {
        return jobPanel;
    }
}
