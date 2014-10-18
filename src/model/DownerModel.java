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
    private JobDescription activeJobDescription = null;
    private JobStats activeJobStats = null;
    private JobRunner activeJobRunner = null;

    private CoolObservable<JobStats> activeJobStatusObservable = new CoolObservable<>();
    private CoolObservable<ArrayList<JobListItem>> autoUpdateItemList = new CoolObservable<>();

    private HashMap<String, JobRunner> urlToJobRunner = new HashMap<>();


    public synchronized void startNewJob(JobDescription jobDescription)
    {
        if (urlToJobRunner.containsKey(jobDescription.getThreadUrl()))
        {
            activeJobRunner = urlToJobRunner.get(jobDescription.getThreadUrl());
        }
        else
        {
            activeJobRunner = new JobRunner(this);
            urlToJobRunner.put(jobDescription.getThreadUrl(), activeJobRunner);
        }

        activeJobDescription = jobDescription;
        activeJobRunner.startDownloads(jobDescription);
    }




    public void setNewActiveJobStats(JobStats jobStats)
    {
        this.activeJobStats = jobStats;
        activeJobStatusObservable.notifyObservers(jobStats);
    }

    public boolean isActiveJobRunning()
    {
        return activeJobRunner != null && activeJobRunner.getIsDownloadRunning();
    }

    public void cancelActiveJob()
    {
        if (activeJobRunner != null)
            activeJobRunner.cancelDownload();
    }


    /***
     * Public add-observer functions
     */
    public void onActiveJobStatsChange(CoolObserver<JobStats> obs)
    {
        activeJobStatusObservable.addObserver(obs);
    }

    public void onAutoUpdateListChange(CoolObserver<ArrayList<JobListItem>> obs)
    {
        autoUpdateItemList.addObserver(obs);
    }

}
