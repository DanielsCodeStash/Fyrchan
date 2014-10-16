package shared;

public class JobInfo
{
    private String threadUrl;
    private String path;


    public String getThreadUrl()
    {
        return threadUrl;
    }

    public JobInfo setThreadUrl(String threadUrl)
    {
        this.threadUrl = threadUrl;
        return this;
    }


    public String getPath()
    {
        return path;
    }

    public JobInfo setPath(String path)
    {
        this.path = path;
        return this;
    }

    @Override
    public String toString()
    {
        return "JobInfo{" +
                "threadUrl='" + threadUrl + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
