package util;

import java.util.Date;

public class Stopwatch
{
    public static void main(String[] args) throws InterruptedException
    {
        Stopwatch stopwatch = new Stopwatch();
        stopwatch.setTotalNumLaps(20000);
        for (int i = 0; i < 20000; i++)
        {
            Thread.sleep(100);
            stopwatch.lap();
            System.out.println(stopwatch.getLapReport());

        }
    }


    long start = -1;
    long end = -1;

    int totalNumLaps;
    int lapNum = 0;
    long lastLapTime;

    public Stopwatch()
    {
        start = getCurrentTime();
    }

    public void start()
    {
        start = getCurrentTime();
    }

    public void stop()
    {
        end = getCurrentTime();
    }

    public long getRuntime()
    {
        return getCurrentTime() - start;
    }

    public String getEndReport()
    {
        if (end == -1)
            end = getCurrentTime();


        long totalTime = end - start;
        return "Elapsed time: " + getFormattedTime(totalTime) + " ";
    }

    public void setTotalNumLaps(int totalNumLaps)
    {
        this.totalNumLaps = totalNumLaps;
    }

    public void lap()
    {
        this.lapNum++;
        lastLapTime = new Date().getTime();
    }

    public String getLapReport()
    {
        long avgLapTime = (getCurrentTime() - start) / lapNum;
        long calcRemaining = (totalNumLaps - lapNum) * avgLapTime;


        return lapNum + " / " + totalNumLaps + "\t\t| " + getFormattedTime(calcRemaining) + "\t\t| " + getFormattedTime(avgLapTime);
    }

    private long getCurrentTime()
    {
        return new Date().getTime();
    }

    public static String getFormattedTime(long time)
    {
        int mins = (int) time / (1000 * 60);
        long remaining = time - mins * (1000 * 60);

        int secs = (int) remaining / 1000;
        remaining = remaining - secs * 1000;
        long millis = remaining;


        boolean includeMins = false;
        boolean includeSecs = false;

        if (mins != 0)
        {
            includeMins = true;
        }
        if (secs != 0 || mins != 0)
        {
            includeSecs = true;
        }

        String out = "";
        if (includeMins)
        {
            out += mins + " m ";
        }
        if (includeSecs)
        {
            if (secs < 10)
                out += "0";

            out += secs + " s ";
        }


        if (includeSecs)
        {
            if (millis < 100)
                out += "0";
            if (millis < 10)
                out += "0";
        }

        out += millis + " ms";


        return out;

    }

}
