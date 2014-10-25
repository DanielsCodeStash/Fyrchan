package shared;

import java.util.concurrent.atomic.AtomicInteger;

public class JobListItem
{
    private static AtomicInteger idTicker = new AtomicInteger(0);

    public int id;
    public String description;
    public String status;

    public JobListItem()
    {
        this.id = idTicker.getAndIncrement();
    }

    public static AtomicInteger getIdTicker()
    {
        return idTicker;
    }

    public static void setIdTicker(AtomicInteger idTicker)
    {
        JobListItem.idTicker = idTicker;
    }

    public JobListItem setId(int id)
    {
        this.id = id;
        return this;
    }

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

    @Override
    public boolean equals(Object o)
    {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        JobListItem item = (JobListItem) o;

        if (id != item.id) return false;
        if (description != null ? !description.equals(item.description) : item.description != null) return false;
        if (status != null ? !status.equals(item.status) : item.status != null) return false;

        return true;

    }

    public JobListItem copy()
    {
        return new JobListItem()
                    .setId(id)
                    .setDescription(description)
                    .setStatus(status);
    }


    public int getId()
    {
        return id;
    }

    @Override
    public String toString()
    {
        return "\nJobListItem{" +
                "id=" + id +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                "}";
    }
}
