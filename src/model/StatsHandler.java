package model;

import org.joda.time.DateTime;
import shared.JobStats;
import shared.JobStatus;
import util.PeriodicAlarm;
import util.observable.IdentObservable;
import util.observable.IdentObserver;

import java.text.DecimalFormat;

public class StatsHandler
{


    private IdentObservable<StatsHandler, JobStatus> statusChange = new IdentObservable<>();
    private IdentObservable<StatsHandler, JobStats> statsChange = new IdentObservable<>();

    private int numFilesExisting = 0;
    private int filesToDownload = 0;
    private long totalDownedSizeKB = 0;
    private int filesDownloaded = 0;
    private JobStatus status;

    private DateTime timeStarted;
    private PeriodicAlarm autoUpdateAlarm = new PeriodicAlarm(1000);


    private DecimalFormat df;

    public StatsHandler()
    {
        df = new DecimalFormat("###.#");
        setNewStatus(JobStatus.QUEUED);

    }

    public JobStatus getJobStatus()
    {
        return status;
    }

    public synchronized void notifyDownloadStart(String url)
    {
        if (status == JobStatus.QUEUED || status == JobStatus.STARTING_UP)
        {
            setNewStatus(JobStatus.RUNNING);
        }
    }

    public synchronized void notifyDownloadDone(String url, double filesize)
    {
        //System.out.println(new DateTime().toString() + " | " + url + " done. Size: " + filesize);

        filesDownloaded++;
        totalDownedSizeKB += (filesize / 1000);
        if (filesDownloaded == filesToDownload)
        {
            setNewStatus(JobStatus.DONE);
        }
    }

    public synchronized void notifyStartingUp()
    {
        setNewStatus(JobStatus.STARTING_UP);
    }

    public synchronized void notifyAllDownloadsAborted()
    {
        setNewStatus(JobStatus.ABORTED);
    }

    public synchronized void notifyAllDownloadsDone()
    {

        if (status == JobStatus.ABORTING)
        {
            setNewStatus(JobStatus.ABORTED);
        }
        else
        {
            setNewStatus(JobStatus.DONE);
        }
        sendStatsMessage();
    }

    public synchronized void notifyFileExists(String url)
    {
        notifyDownloadDone(url, 0);
        numFilesExisting++;
    }

    private synchronized JobStats getJobStats()
    {

        String files = "";
        String speed = "";
        String time = "";
        double doneNum = 0;

        if (filesToDownload != 0 && filesDownloaded != 0)
        {


            files = filesDownloaded + "";

            boolean showRemaining = status != JobStatus.DONE;
            boolean showExisting = numFilesExisting != 0;
            boolean showParenthesis = showRemaining || showExisting;

            if (showParenthesis)
            {
                files += "\t(";
                if (showRemaining)
                {
                    files += (filesToDownload - filesDownloaded) + " remaining";
                }
                if (showExisting)
                {
                    if (showRemaining)
                    {
                        files += ", ";
                    }

                    files += numFilesExisting + " existing";
                }
                files += ")";
            }


            doneNum = (double) filesDownloaded / filesToDownload;

            long timeElapsed = (new DateTime().getMillis() - timeStarted.getMillis());
            long timePerFile = timeElapsed / filesDownloaded;
            long timeRemaining = timePerFile * (filesToDownload - filesDownloaded);
            time = ((int) timeRemaining / 1000) + "s \t(" + ((int) timeElapsed / 1000) + "s elapsed)";


            double secsElapsed = timeElapsed / (double) 1000;
            double dlSpeedKB;
            if (secsElapsed > 0.01)
                dlSpeedKB = totalDownedSizeKB / secsElapsed;
            else
                dlSpeedKB = 0;

            double totalDownedMB = totalDownedSizeKB / (double) 1000;

            speed = df.format(dlSpeedKB) + " kb/s \t (" + df.format(totalDownedMB) + " MB downloaded)";

        }


        JobStats jobStats = new JobStats()
                .setStatus(status)
                .setFiles(files)
                .setSpeed(speed)
                .setTime(time)
                .setPercentDone(doneNum);


        return jobStats;

    }

    public synchronized void statsHeartbeat()
    {
        if(autoUpdateAlarm.isActive())
        {
            sendStatsMessage();
        }
    }

    public synchronized void forceStatsUpdate()
    {
        sendStatsMessage();
    }

    public void sendStatsMessage()
    {
        statsChange.notifyObservers(this, getJobStats());
        autoUpdateAlarm.reset();
    }

    public synchronized void setFilesToDownload(int numFiles)
    {
        this.filesToDownload = numFiles;
    }

    public synchronized void startTimeCounter()
    {
        timeStarted = new DateTime();
    }


    public void addStatsChangeObs(IdentObserver<StatsHandler, JobStats> obs)
    {
        statsChange.addObserver(obs);
    }

    public void addStatusChangeObs(IdentObserver<StatsHandler, JobStatus> obs)
    {
        statusChange.addObserver(obs);
    }

    private void setNewStatus(JobStatus newStatus)
    {
        if(this.status != newStatus)
        {
            this.status = newStatus;
            statusChange.notifyObservers(this, newStatus);
        }
    }

}
