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

    private ArrayList<JobListItem> items = new ArrayList<>();

    static int tes = 0;
    public synchronized void startNewJob(JobDescription jobDescription)
    {
        if(tes == 0)
        {
            items.add(new JobListItem()
                .setDescription(jobDescription.getThreadUrl().substring(0, 20))
                .setStatus("404"));
        }
        else if(tes == 1)
        {
            items.add(new JobListItem()
                    .setDescription(jobDescription.getThreadUrl().substring(0, 20))
                    .setStatus("505"));

            items.add(new JobListItem()
                    .setDescription(jobDescription.getThreadUrl().substring(0, 20))
                    .setStatus("505"));
        }
        else if(tes == 2)
        {
            items.remove(0);
        }
        else if(tes ==  3)
        {
            items.get(0).setStatus("606");
        }

        if(1 == 1)
        {
            tes++;
            if(tes == 4)
                tes = 0;

            autoUpdateItemList.notifyObservers(items);
            return;
        }


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

    public void removeAutoUpdateItem(JobListItem item)
    {
        items.remove(item);
        autoUpdateItemList.notifyObservers(items);
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
