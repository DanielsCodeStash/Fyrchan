package model.downloading;

import model.BaseSettings;
import model.StatsHandler;
import shared.JobDescription;
import shared.JobStatus;

import java.util.Date;
import java.util.concurrent.atomic.AtomicBoolean;

public class JobRunner
{

    public AtomicBoolean jobRunning;
    private StatsHandler statsHandler;
    private JobDescription jobDescription;
    private boolean firstRun = true;

    private boolean autoUpdate;
    private Long lastFinished = null;

    public JobRunner(JobDescription jobDescription)
    {
        this.jobDescription = jobDescription;
        jobRunning = new AtomicBoolean(false);
        statsHandler = new StatsHandler();
        autoUpdate = jobDescription.getAutoUpdate();

    }

    public void startDownloads()
    {
        if (!firstRun)
        {
            statsHandler.performBeforeUpdateReset();
        }

        jobRunning.set(true);
        statsHandler.notifyStartingUp();
        ThreadDowner threadDowner = new ThreadDowner(jobDescription, statsHandler, this::onDownloadDone, jobRunning);
        new Thread(threadDowner).start();

        System.out.println("Thread download started");

    }

    public void onDownloadDone()
    {
        jobRunning.set(false);

        JobStatus s = statsHandler.getJobStatus();
        if (s == JobStatus.HTTP404 || s == JobStatus.INPUT_ERROR || s == JobStatus.ERROR)
        {
            autoUpdate = false;
        }

        if (autoUpdate)
        {
            lastFinished = new Date().getTime();
            statsHandler.notifyNewStatus(JobStatus.SLEEPING);
        }
        firstRun = false;
    }

    public void cancelDownload()
    {
        jobRunning.set(false);
        statsHandler.notifyAllDownloadsAborted();
    }

    public JobStatus getJobStatus()
    {
        return statsHandler.getJobStatus();
    }

    public boolean getIsDownloadRunning()
    {
        return jobRunning.get();
    }

    public JobDescription getJobDescription()
    {
        return jobDescription;
    }

    public StatsHandler getStatsHandler()
    {
        return statsHandler;
    }

    public boolean isAutoUpdate()
    {
        return autoUpdate;
    }

    public long getMsToNextUpdate()
    {
        if (!autoUpdate || lastFinished == null)
            return -1;

        return (lastFinished + BaseSettings.msBetweenAutomaticUpdate) - new Date().getTime();
    }

    public boolean needUpdate()
    {
        if (!autoUpdate)
            return false;

        if (new Date().getTime() > lastFinished + BaseSettings.msBetweenAutomaticUpdate)
            return true;

        return false;
    }

}
