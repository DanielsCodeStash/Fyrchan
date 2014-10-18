package model.downloading;

import model.DownerModel;
import model.StatsHandler;
import shared.JobDescription;

import java.util.concurrent.atomic.AtomicBoolean;

public class JobRunner
{

    public AtomicBoolean jobRunning;
    private StatsHandler statsHandler;
    private DownerModel downerModel;


    public JobRunner(DownerModel downerModel)
    {
        this.downerModel = downerModel;
        jobRunning = new AtomicBoolean(false);

    }

    public void startDownloads(JobDescription jobDescription)
    {
        jobRunning.set(true);
        statsHandler = new StatsHandler(downerModel);
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

    public boolean getIsDownloadRunning()
    {
        return jobRunning.get();
    }


}
