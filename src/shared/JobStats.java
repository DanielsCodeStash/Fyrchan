package shared;

public class JobStats
{
    public JobStatus status;
    public String files;
    public String speed;
    public String time;
    public double percentDone;

    public JobStatus getStatus()
    {
        return status;
    }

    public JobStats setStatus(JobStatus status)
    {
        this.status = status;
        return this;

    }

    public String getFiles()
    {
        return files;
    }

    public JobStats setFiles(String files)
    {
        this.files = files;
        return this;
    }

    public String getSpeed()
    {
        return speed;
    }

    public JobStats setSpeed(String speed)
    {
        this.speed = speed;
        return this;
    }

    public String getTime()
    {
        return time;
    }

    public JobStats setTime(String time)
    {
        this.time = time;
        return this;
    }


    @Override
    public String toString()
    {
        return "JobStats{" +
                "status='" + status + '\'' +
                ", files='" + files + '\'' +
                ", speed='" + speed + '\'' +
                ", time='" + time + '\'' +
                '}';
    }

    public double getPercentDone()
    {
        return percentDone;
    }

    public JobStats setPercentDone(double percentDone)
    {
        this.percentDone = percentDone;
        return this;
    }
}