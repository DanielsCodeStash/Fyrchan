package model;

import model.downloading.JobRunner;
import shared.JobDescription;
import shared.JobListItem;
import shared.JobStats;
import util.observable.CoolObservable;
import util.observable.CoolObserver;

import java.util.ArrayList;
import java.util.HashMap;

public class DownerModel
{



    private TaskManager taskManager;



    public DownerModel()
    {
        taskManager = new TaskManager();
        new Thread(taskManager).start();
    }


    public synchronized void startNewJob(JobDescription jobDescription)
    {

        JobRunner activeJobRunner = new JobRunner(this, jobDescription);
        taskManager.addTask(activeJobRunner);

    }

    public void removeAutoUpdateItem(JobListItem item)
    {
        //items.remove(item);
        //autoUpdateItemList.notifyObservers(items);
    }


    public void setNewActiveJobStats(JobStats jobStats)
    {

    }

    public boolean isActiveJobRunning()
    {
        return false; //return activeJobRunner != null && activeJobRunner.getIsDownloadRunning();
    }

    public void cancelActiveJob()
    {
        //if (activeJobRunner != null)
        //    activeJobRunner.cancelDownload();

    }



    /***
     * Public add-observer functions
     */
    public void onActiveJobStatsChange(CoolObserver<JobStats> obs)
    {
        //activeJobStatusObservable.addObserver(obs);
    }

    public void onTaskListChange(CoolObserver<ArrayList<JobListItem>> obs)
    {
        taskManager.addTaskListObserver(obs);
    }

}
