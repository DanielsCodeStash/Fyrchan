package controller;

import model.DownerModel;
import view.joblist.JobListPanel;


public class JobListController
{
    private JobListPanel jobListPanel;
    private DownerModel downerModel;
    private Fyrchan fyrchan;

    public JobListController(JobListPanel jobListPanel, DownerModel downerModel, Fyrchan fyrchan)
    {
        this.jobListPanel = jobListPanel;
        this.downerModel = downerModel;
        this.fyrchan = fyrchan;
    }

}
