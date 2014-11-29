package util;

import java.util.Date;

public class PeriodicAlarm
{

    private int msBetweenActivations;
    private long lastActivation;

    public static void main(String[] args) throws InterruptedException
    {
        PeriodicAlarm alarm = new PeriodicAlarm(2000);

        int numActivations = 0;
        while (numActivations < 4)
        {
            if (alarm.isActive())
            {
                alarm.reset();
                numActivations++;
            }


            Thread.sleep(35);
        }

    }

    public PeriodicAlarm(int msBetweenActivations)
    {
        this.msBetweenActivations = msBetweenActivations;
        this.lastActivation = new Date().getTime();
    }

    public void reset()
    {
        this.lastActivation = new Date().getTime();
    }

    public boolean isActive()
    {
        return lastActivation + msBetweenActivations < new Date().getTime();
    }
}
