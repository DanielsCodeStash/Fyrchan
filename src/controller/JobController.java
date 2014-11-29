package controller;

import javafx.stage.DirectoryChooser;
import model.BaseSettings;
import model.parsing.PathHandler;
import shared.JobDescription;
import view.JobPanel;

import java.io.File;

public class JobController
{

    private JobPanel jobPanel;
    private ControllerManager controllerManager;

    private boolean threadUrlValid = true;

    public JobController(JobPanel jobPanel, ControllerManager controllerManager)
    {
        this.jobPanel = jobPanel;
        this.controllerManager = controllerManager;

        jobPanel.getThreadUrl().setOnKeyReleased(event -> onThreadUrlKeyUp());
        jobPanel.getThreadName().setOnKeyReleased(event -> updatePathPreview());
        jobPanel.getResetButton().setOnAction(actionEvent -> resetThreadName());
        jobPanel.getBasePath().setOnKeyReleased(event -> updatePathPreview());
        jobPanel.getSelectDirButton().setOnAction(actionEvent -> selectBaseDir());
        jobPanel.getRunButton().setOnAction(actionEvent -> startDownload());
    }

    public void onStageShown()
    {
        jobPanel.getPreview().setText(getPreviewPath());
        jobPanel.getAutoUpdateCheckbox().setSelected(true);
    }

    public void setJobDescriptionSelected(JobDescription jobDescription)
    {
        jobPanel.getThreadUrl().setText(jobDescription.getThreadUrl());
        jobPanel.getAutoUpdateCheckbox().setSelected(jobDescription.getAutoUpdate());
        jobPanel.getBasePath().setText(jobDescription.getBaseOutputPath());
    }


    public PathHandler.EvaluatedPath getPath()
    {
        String basePath = jobPanel.getBasePath().getText();
        String threadName = jobPanel.getThreadName().getText();
        String threadUrlStr = jobPanel.getThreadUrl().getText();

        return PathHandler.getPath(basePath, threadName, threadUrlStr);

    }


    private void selectBaseDir()
    {
        DirectoryChooser dirChooser = new DirectoryChooser();
        dirChooser.setTitle("Select directory");
        File f = dirChooser.showDialog(controllerManager.getFyrchan().getPrimaryStage());
        if (f != null)
        {
            jobPanel.getBasePath().setText(f.getPath());
            updatePathPreview();
        }
    }

    private void startDownload()
    {
        JobDescription newJob = getJobData();
        controllerManager.getDownerModel().startNewJob(newJob);
        controllerManager.notifyJobAdded(newJob);
        jobPanel.getThreadUrl().setText("");
    }

    private void resetThreadName()
    {
        jobPanel.getThreadName().setText(BaseSettings.baseFolderFormat);
        updatePathPreview();
    }

    public void updatePathPreview()
    {
        jobPanel.getPreview().setText(getPreviewPath());
    }

    public String getPreviewPath()
    {
        return getPath().getFullPath() + "1.png";
    }

    public JobDescription getJobData()
    {
        PathHandler.EvaluatedPath path = getPath();

        JobDescription jobDescription = new JobDescription()
                .setOutputPath(path.getFullPath())
                .setThreadUrl(jobPanel.getThreadUrl().getText())
                .setAutoUpdate(jobPanel.getAutoUpdateCheckbox().isSelected())
                .setBaseOutputPath(jobPanel.getBasePath().getText())
                .setEvaluatedName(path.getEvaluatedName())
                .setThreadName(jobPanel.getThreadName().getText());

        return jobDescription;
    }

    public void onThreadUrlKeyUp()
    {
        validateThreadUrl();
        updatePathPreview();
    }


    public void validateThreadUrl()
    {
        String threadUrlStr = jobPanel.getThreadUrl().getText();
        boolean newUrlValid = PathHandler.urlIsValid(threadUrlStr);

        if ((newUrlValid && !threadUrlValid) || threadUrlStr.isEmpty())
        {
            threadUrlValid = true;
            jobPanel.getThreadUrl().getStyleClass().remove("invalidInput");
        }
        else if (!newUrlValid && threadUrlValid)
        {
            threadUrlValid = false;
            jobPanel.getThreadUrl().getStyleClass().add("invalidInput");
        }
    }

}
