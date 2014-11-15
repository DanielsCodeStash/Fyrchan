package controller;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;
import model.DownerModel;
import shared.JobDescription;
import util.observable.CoolObservable;

import java.util.HashMap;

public class ControllerManager
{
    private Fyrchan fyrchan;
    private DownerModel downerModel;

    private JobController jobController = null;
    private JobListController jobListController = null;


    private HashMap<String, JobDescription> threadUrlToJobDescription = new HashMap<>();

    public ControllerManager(DownerModel downerModel, Fyrchan fyrchan)
    {
        this.downerModel = downerModel;
        this.fyrchan = fyrchan;

        fyrchan.getPrimaryStage().setOnCloseRequest(event -> onApplicationClose());
    }

    public void onApplicationClose()
    {
        downerModel.notifyApplicationStop();
    }

    public void setSubControllers(JobController jobController, JobListController jobListController)
    {
        this.jobListController = jobListController;
        this.jobController = jobController;
    }


    public Fyrchan getFyrchan()
    {
        return fyrchan;
    }

    public DownerModel getDownerModel()
    {
        return downerModel;
    }


    public void notifyJobAdded(JobDescription jobDescription)
    {
        threadUrlToJobDescription.put(jobDescription.getThreadUrl(), jobDescription);
    }

    public void notifyJobRemoved(String threadUrl)
    {
        threadUrlToJobDescription.remove(threadUrl);
    }

    public void notifyTaskClicked(String threadUrl)
    {
        if (jobController != null)
        {
            JobDescription jobSelected  = threadUrlToJobDescription.get(threadUrl);
            if(jobSelected != null)
            {
                jobController.setJobDescriptionSelected(jobSelected);
            }
        }
        else
        {
            System.err.println("ControllerManager: Tried to notify task click without jobController present");
        }
    }
}
