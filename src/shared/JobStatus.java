package shared;

public enum JobStatus
{
    STARTING_UP, QUEUED, RUNNING, DONE, ABORTING, ABORTED, HTTP404;

    public String toString()
    {
        switch (this)
        {
            case QUEUED: return "Queued";
            case STARTING_UP: return "Starting up";
            case RUNNING: return "Running";
            case DONE: return "Done";
            case ABORTING: return "Aborting";
            case ABORTED: return "Aborted";
            case HTTP404: return "404";
        }
        return "JobStatus toString failed LOL";
    }
}
