package model.downloading;

import model.DownerModel;
import model.StatsHandler;
import shared.JobDescription;
import shared.JobStatus;

import java.util.concurrent.atomic.AtomicBoolean;

public class JobRunner
{

    public AtomicBoolean jobRunning;
    private StatsHandler statsHandler;
    private DownerModel downerModel;
    private JobDescription jobDescription;


    public JobRunner(DownerModel downerModel, JobDescription jobDescription)
    {
        this.downerModel = downerModel;
        this.jobDescription = jobDescription;
        jobRunning = new AtomicBoolean(false);
        statsHandler = new StatsHandler();

    }

    public void startDownloads()
    {
        jobRunning.set(true);
        statsHandler.notifyStartingUp();
        ThreadDowner threadDowner = new ThreadDowner(jobDescription, statsHandler, this::onDownloadDone, jobRunning);
        new Thread(threadDowner).start();

        System.out.println("Thread download started");

    }

    public void onDownloadDone()
    {
        jobRunning.set(false);
        statsHandler.notifyAllDownloadsDone();
        System.out.println("Thread download done.");
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

    public boolean needUpdate()
    {
        return false;
    }

    public void update()
    {

    }
}
