package shared;

public enum JobStatus
{
    STARTING_UP, QUEUED, DOWNLOADING, DONE, ABORTING, ABORTED, HTTP404, ERROR, INPUT_ERROR, SLEEPING;

    public String toString()
    {
        switch (this)
        {
            case QUEUED:
                return "Queued";
            case STARTING_UP:
                return "Starting up";
            case DOWNLOADING:
                return "Downloading";
            case DONE:
                return "Done";
            case ABORTING:
                return "Aborting";
            case ABORTED:
                return "Aborted";
            case HTTP404:
                return "404";
            case ERROR:
                return "Error";
            case INPUT_ERROR:
                return "Input Error";
            case SLEEPING:
                return "Sleeping";

        }
        return "JobStatus toString failed LOL";
    }
}
