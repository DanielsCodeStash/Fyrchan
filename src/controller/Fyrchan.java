package controller;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import model.*;
import view.joblist.JobListPanel;
import view.JobPanel;
import view.StatsPanel;

public class Fyrchan extends Application
{

    // panels
    private JobPanel jobPanel;
    private StatsPanel statsPanel;
    private JobListPanel jobListPanel;

    // controllers
    private JobController jobController;
    private JobListController jobListController;

    // model
    private DownerModel downerModel;

    // main structure blocks
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
        jobListPanel = new JobListPanel(downerModel);

        jobController = new JobController(jobPanel, downerModel, this);
        jobListController = new JobListController(jobListPanel, downerModel, this);


        // initiate main containers
        BorderPane mainPane = new BorderPane();
        VBox upperContainer = new VBox();
        BorderPane lowerContainer = new BorderPane();

        // add leaf-panels to our main containers
        upperContainer.getChildren().add(getMenuBar());
        upperContainer.getChildren().add(jobPanel.getJobGrid());
        upperContainer.getChildren().add(getSeparator());

        lowerContainer.setLeft(statsPanel.getStatsGrid());
        lowerContainer.setRight(jobListPanel.getListContainer());
        lowerContainer.setMaxHeight(2000);

        mainPane.setTop(upperContainer);
        mainPane.setCenter(lowerContainer);

        // set scene and stage options
        this.primaryStage = primaryStage;
        primaryScene = new Scene(mainPane);
        setSceneOptions(primaryScene);
        setStageOptions(primaryStage, primaryScene);

        // set up alerts for controllers that needs to know when the stage has been rendered
        primaryStage.setOnShown(windowEvent -> jobController.onStageShown());

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
