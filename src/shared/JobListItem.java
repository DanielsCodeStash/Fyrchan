package shared;

public class JobListItem
{
    public String description;
    public String status;

    public String getDescription()
    {
        return description;
    }

    public JobListItem setDescription(String description)
    {
        this.description = description;
        return this;
    }

    public String getStatus()
    {
        return status;
    }

    public JobListItem setStatus(String status)
    {
        this.status = status;
        return this;
    }
}
