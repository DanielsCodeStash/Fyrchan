package shared;

public class JobDescription
{
    private String threadUrl;
    private String threadName;
    private String baseOutputPath;
    private String outputPath;
    private String evaluatedName;
    private boolean autoUpdate;


    public String getThreadUrl()
    {
        return threadUrl;
    }

    public JobDescription setThreadUrl(String threadUrl)
    {
        this.threadUrl = threadUrl;
        return this;
    }


    public String getOutputPath()
    {
        return outputPath;
    }

    public JobDescription setOutputPath(String outputPath)
    {
        this.outputPath = outputPath;
        return this;
    }

    public String getThreadName()
    {
        return threadName;
    }

    public JobDescription setThreadName(String threadName)
    {
        this.threadName = threadName;
        return this;
    }

    public String getBaseOutputPath()
    {
        return baseOutputPath;
    }

    public JobDescription setBaseOutputPath(String baseOutputPath)
    {
        this.baseOutputPath = baseOutputPath;
        return this;
    }

    public boolean getAutoUpdate()
    {
        return autoUpdate;
    }

    public JobDescription setAutoUpdate(boolean autoUpdate)
    {
        this.autoUpdate = autoUpdate;
        return this;
    }

    @Override
    public String toString()
    {
        return "JobDescription{" +
                "threadUrl='" + threadUrl + '\'' +
                ", threadName='" + threadName + '\'' +
                ", baseOutputPath='" + baseOutputPath + '\'' +
                ", outputPath='" + outputPath + '\'' +
                ", autoUpdate=" + autoUpdate +
                ", evaluatedName=" + evaluatedName +
                '}';
    }

    public String getEvaluatedName()
    {
        return evaluatedName;
    }

    public JobDescription setEvaluatedName(String evaluatedName)
    {
        this.evaluatedName = evaluatedName;
        return this;
    }

    public boolean isAutoUpdate()
    {
        return autoUpdate;
    }
}
