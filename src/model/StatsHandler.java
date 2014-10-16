package model;

import org.joda.time.DateTime;
import shared.JobStats;
import shared.JobStatus;

import java.text.DecimalFormat;

public class StatsHandler
{

    private DownerModel downerModel;

    private int numFilesExisting = 0;
    private int filesToDownload = 0;
    private long totalDownedSizeKB = 0;
    private int filesDownloaded = 0;
    private JobStatus status = JobStatus.STARTING_UP;

    private DateTime timeStarted;
    private DateTime lastUpdate = null;
    private JobStats jobStats = new JobStats();


    private DecimalFormat df;

    public StatsHandler(DownerModel downerModel)
    {
        this.downerModel = downerModel;
        updateStatsData();
        df = new DecimalFormat("###.#");
    }


    public synchronized void notifyDownloadStart(String url)
    {
        if (status == JobStatus.NOT_STARTED || status == JobStatus.STARTING_UP)
        {
            status = JobStatus.RUNNING;
            updateStatsData();
        }
    }

    public synchronized void notifyDownloadDone(String url, double filesize)
    {
        //System.out.println(new DateTime().toString() + " | " + url + " done. Size: " + filesize);

        filesDownloaded++;
        totalDownedSizeKB += (filesize / 1000);
        if (filesDownloaded == filesToDownload)
        {
            status = JobStatus.DONE;
        }

        updateStatsData();
    }

    public synchronized void notifyAllDownloadsAborted()
    {
        status = JobStatus.ABORTING;
        updateStatsData();
    }

    public synchronized void notifyAllDownloadsDone()
    {
        if (status == JobStatus.ABORTING)
        {
            status = JobStatus.ABORTED;
        }
        else
        {
            status = JobStatus.DONE;
        }
        updateStatsData();
    }

    public synchronized void notifyFileExists(String url)
    {
        notifyDownloadDone(url, 0);
        numFilesExisting++;
    }

    private synchronized void updateStatsData()
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


        jobStats
                .setStatus(status)
                .setFiles(files)
                .setSpeed(speed)
                .setTime(time)
                .setPercentDone(doneNum);

        lastUpdate = new DateTime();
        downerModel.setNewActiveJobStats(jobStats);

    }

    public synchronized void statsHeartbeat()
    {
        if (lastUpdate.getMillis() < new DateTime().getMillis() - 1000)
        {
            updateStatsData();
        }
    }

    public synchronized void setFilesToDownload(int numFiles)
    {
        this.filesToDownload = numFiles;
    }

    public synchronized void startTimeCounter()
    {
        timeStarted = new DateTime();
    }


}
