package shared;

public class JobDescription
{
    private String threadUrl;
    private String path;


    public String getThreadUrl()
    {
        return threadUrl;
    }

    public JobDescription setThreadUrl(String threadUrl)
    {
        this.threadUrl = threadUrl;
        return this;
    }


    public String getPath()
    {
        return path;
    }

    public JobDescription setPath(String path)
    {
        this.path = path;
        return this;
    }

    @Override
    public String toString()
    {
        return "JobDescription{" +
                "threadUrl='" + threadUrl + '\'' +
                ", path='" + path + '\'' +
                '}';
    }
}
