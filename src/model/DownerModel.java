package model;

import model.downloading.JobRunner;
import shared.JobInfo;
import shared.JobStats;
import util.observable.CoolObservable;
import util.observable.CoolObserver;

import java.util.HashMap;

public class DownerModel
{
    private JobInfo activeJobInfo = null;
    private JobStats activeJobStats = null;
    private JobRunner activeJobRunner = null;

    private CoolObservable<JobStats> activeJobStatusObservable;

    private HashMap<String, JobRunner> urlToJobRunner = new HashMap<>();

    public DownerModel()
    {
        activeJobStatusObservable = new CoolObservable<>();
    }

    public synchronized void startNewJob(JobInfo jobInfo)
    {
        if (urlToJobRunner.containsKey(jobInfo.getThreadUrl()))
        {
            activeJobRunner = urlToJobRunner.get(jobInfo.getThreadUrl());
        }
        else
        {
            activeJobRunner = new JobRunner(this);
            urlToJobRunner.put(jobInfo.getThreadUrl(), activeJobRunner);
        }

        activeJobInfo = jobInfo;
        activeJobRunner.startDownloads(jobInfo);
    }

    public void onActiveJobStatsChange(CoolObserver<JobStats> obs)
    {
        activeJobStatusObservable.addObserver(obs);
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

}
