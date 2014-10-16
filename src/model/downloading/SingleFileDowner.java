package model.downloading;

import model.StatsHandler;
import org.jsoup.Connection;
import util.ConHandler;

import java.io.File;
import java.io.FileOutputStream;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicBoolean;

public class SingleFileDowner implements Callable<Boolean>
{
    private String url;
    private String outputUrl;
    private StatsHandler statsHandler;

    private AtomicBoolean keepDownloadRunning;

    public SingleFileDowner(String url, String outputUrl, StatsHandler statsHandler, AtomicBoolean keepDownloadRunning)
    {
        this.url = url;
        this.outputUrl = outputUrl;
        this.statsHandler = statsHandler;
        this.keepDownloadRunning = keepDownloadRunning;
    }


    @Override
    public Boolean call()
    {
        Connection con = ConHandler.getConnection(url);
        try
        {
            if (keepDownloadRunning.get())
            {
                statsHandler.notifyDownloadStart(url);

                File outFile = new File(outputUrl);
                if (!outFile.exists())
                {
                    Connection.Response resultImageResponse = con.ignoreContentType(true).execute();
                    byte[] byteBody = resultImageResponse.bodyAsBytes();

                    FileOutputStream out = (new FileOutputStream(new java.io.File(outputUrl)));
                    out.write(byteBody);
                    out.close();

                    statsHandler.notifyDownloadDone(url, byteBody.length);
                }
                else
                {
                    statsHandler.notifyFileExists(url);
                }
            }

        } catch (Exception e)
        {
            e.printStackTrace();
        }
        return null;
    }

}
