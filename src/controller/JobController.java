package controller;

import javafx.application.Platform;
import javafx.stage.DirectoryChooser;
import model.BaseSettings;
import model.DownerModel;
import model.parsing.PathHandler;
import shared.JobDescription;
import shared.JobStats;
import shared.JobStatus;
import view.JobPanel;

import java.io.File;

public class JobController
{

    private JobPanel jobPanel;
    private Fyrchan fyrchan;
    private DownerModel downerModel;

    private boolean threadUrlValid = true;
    private JobStatus lastJobStatus = null;

    public JobController(JobPanel jobPanel, DownerModel downerModel, Fyrchan fyrchan)
    {
        this.jobPanel = jobPanel;
        this.fyrchan = fyrchan;
        this.downerModel = downerModel;

        jobPanel.getThreadUrl().setOnKeyReleased(event -> onThreadUrlKeyUp());
        jobPanel.getThreadName().setOnKeyReleased(event -> updatePathPreview());
        jobPanel.getResetButton().setOnAction(actionEvent -> resetThreadName());
        jobPanel.getBasePath().setOnKeyReleased(event -> updatePathPreview());
        jobPanel.getSelectDirButton().setOnAction(actionEvent -> selectBaseDir());
        jobPanel.getRunButton().setOnAction(actionEvent -> startDownload());

        downerModel.onActiveJobStatsChange(this::onJobStatsUpdate);
    }

    public void onStageShown()
    {
        jobPanel.getPreview().setText(getPreviewPath());
        jobPanel.getAutoUpdateCheckbox().setSelected(true);
    }



    public void onJobStatsUpdate(JobStats jobStats)
    {
        JobStatus newStatus = jobStats.getStatus();
        if (newStatus != lastJobStatus)
        {
            Platform.runLater(() -> jobPanel.setNewJobStatus(newStatus));
        }

    }

    public String getPath()
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
        File f = dirChooser.showDialog(fyrchan.getPrimaryStage());
        if (f != null)
        {
            jobPanel.getBasePath().setText(f.getPath());
            updatePathPreview();
        }
    }

    private void startDownload()
    {
        if (!downerModel.isActiveJobRunning())
        {
            downerModel.startNewJob(getJobData());
        }
        else
        {
            downerModel.cancelActiveJob();
        }

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
        return getPath() + "1.png";
    }

    public JobDescription getJobData()
    {
        JobDescription jobDescription = new JobDescription()
                .setPath(getPath())
                .setThreadUrl(jobPanel.getThreadUrl().getText());

        System.out.println(jobDescription);
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
