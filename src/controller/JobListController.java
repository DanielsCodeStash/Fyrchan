package controller;

import model.DownerModel;
import shared.JobListItem;
import view.joblist.JobListPanel;


public class JobListController
{
    private JobListPanel jobListPanel;
    private ControllerManager controllerManager;

    public JobListController(JobListPanel jobListPanel, ControllerManager controllerManager)
    {
        this.jobListPanel = jobListPanel;
        this.controllerManager = controllerManager;

        jobListPanel.onRemoveButtonClicked(this::removeButtonClicked);
        jobListPanel.onRowClicked(this::onRowClicked);
    }

    private void removeButtonClicked(JobListItem item)
    {
        controllerManager.notifyJobRemoved(item.getThreadUrl());
        controllerManager.getDownerModel().removeAutoUpdateItem(item);
    }

    private void onRowClicked(JobListItem item)
    {
        controllerManager.notifyTaskClicked(item.getThreadUrl());
        controllerManager.getDownerModel().setNewActiveThreadUrl(item.getThreadUrl());

    }



}
