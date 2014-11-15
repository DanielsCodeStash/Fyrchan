package controller;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.Separator;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import model.*;
import util.ResourceUtil;
import view.joblist.JobListPanel;
import view.JobPanel;
import view.StatsPanel;

import java.io.InputStream;
import java.util.concurrent.ConcurrentLinkedQueue;


public class Fyrchan extends Application
{

    // panels
    private JobPanel jobPanel;
    private StatsPanel statsPanel;
    private JobListPanel jobListPanel;

    // controllers
    private ControllerManager controllerManager;
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
        this.primaryStage = primaryStage;

        // initiate the applications model, view and controller
        downerModel = new DownerModel();

        jobPanel = new JobPanel(downerModel);
        statsPanel = new StatsPanel(downerModel);
        jobListPanel = new JobListPanel(downerModel);

        controllerManager = new ControllerManager(downerModel, this);
        jobController = new JobController(jobPanel, controllerManager);
        jobListController = new JobListController(jobListPanel, controllerManager);
        controllerManager.setSubControllers(jobController, jobListController);

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

        // set scene  options
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
        stage.getIcons().add(ResourceUtil.getImage("icon.png"));
        stage.setScene(scene);

    }

    public void setSceneOptions(Scene scene)
    {
        scene.getStylesheets().add("/view/fyrstyle.css");
    }

    public MenuBar getMenuBar()
    {
        MenuBar menuBar = new MenuBar();
        menuBar.setId("menu");

        Menu fileMenu = new Menu("File");
        Menu aboutMenu = new Menu("About");

        MenuItem tricked1 = new MenuItem("Tricked, nothing here.");
        MenuItem tricked2 = new MenuItem("Tricked, nothing here either.");

        fileMenu.getItems().add(tricked1);
        aboutMenu.getItems().add(tricked2);

        menuBar.getMenus().add(fileMenu);
        menuBar.getMenus().add(aboutMenu);

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
