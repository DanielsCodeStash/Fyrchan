package model.downloading;

import model.BaseSettings;
import model.StatsHandler;
import model.parsing.ThreadParser;
import org.jsoup.nodes.Document;
import shared.JobDescription;
import util.ConHandler;
import util.CoolThreadPool;
import util.Funtastic;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

public class ThreadDowner implements Runnable

{

    private JobDescription jobDescription;
    private StatsHandler statsHandler;
    private Funtastic onDownloadDone;
    private AtomicBoolean downloadRunning;


    public ThreadDowner(JobDescription jobDescription, StatsHandler statsHandler, Funtastic onDownloadDone, AtomicBoolean downloadRunning)
    {
        this.jobDescription = jobDescription;
        this.statsHandler = statsHandler;
        this.onDownloadDone = onDownloadDone;
        this.downloadRunning = downloadRunning;
    }


    private void download() throws IOException, InterruptedException
    {

        Document threadDocument = ConHandler.getConnection(jobDescription.getThreadUrl()).get();
        ArrayList<String> fileUrls = ThreadParser.getFileUrls(threadDocument);

        if (fileUrls.isEmpty())
        {
            System.out.println("No files yo.");
            return;
        }
        else
        {
            File dir = new File(jobDescription.getOutputPath());
            if (!dir.isDirectory())
            {
                boolean res = dir.mkdirs();
                if (!res)
                    System.err.println("Could not create dir");
            }

            int numFiles = fileUrls.size();


            System.out.println("Downloading " + numFiles + " files.");
            statsHandler.setFilesToDownload(numFiles);

            if (numFiles != fileUrls.size())
            {
                System.out.println("Downloading " + numFiles + " of " + fileUrls.size() + " files, the rest exists already.");
            }

        }


        CoolThreadPool<Boolean, SingleFileDowner> service = new CoolThreadPool<>(BaseSettings.numGatherThreads);
        for (int i = 0; i < fileUrls.size(); i++)
        {
            String fileUrl = fileUrls.get(i);
            String outputPath = getOutFilePath(fileUrl, jobDescription.getOutputPath(), i);

            SingleFileDowner imgDowner = new SingleFileDowner(fileUrl, outputPath, statsHandler, downloadRunning);
            service.addTask(imgDowner);
        }

        statsHandler.startTimeCounter();
        service.startTasks();


        while (!service.allTasksDone())
        {
            if (!downloadRunning.get())
            {
                service.cancelRemainingTasks();
                service.awaitCancellation();
            }
            else
            {
                Thread.sleep(50);
                statsHandler.statsHeartbeat();
            }
        }

        onDownloadDone.runFun();
        System.out.println("Threaddowner done");

    }

    private String getOutFilePath(String fileUrl, String path, int i)
    {
        String filetype = fileUrl.substring(fileUrl.lastIndexOf('.'));
        return path + i + filetype;
    }


    @Override
    public void run()
    {
        try
        {
            Thread.currentThread().setName("ThreadDowner");
            download();

        } catch (IOException | InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
