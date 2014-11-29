package model;

import javafx.beans.property.SimpleStringProperty;
import model.downloading.JobRunner;
import shared.JobDescription;
import shared.JobListItem;
import shared.JobStatus;
import util.observable.CoolObservable;
import util.observable.CoolObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.atomic.AtomicBoolean;

public class TaskManager implements Runnable
{

    private CoolObservable<ArrayList<JobListItem>> jobList = new CoolObservable<>();

    private ArrayList<JobListItem> jobListItems = new ArrayList<>();
    private HashMap<String, JobListItem> threadUrlToJobListItem = new HashMap<>();
    private ConcurrentLinkedQueue<JobRunner> taskQue = new ConcurrentLinkedQueue<>();

    private AtomicBoolean running = new AtomicBoolean(true);
    private JobRunner runningJob = null;
    private AtomicBoolean taskListUpdatePending = new AtomicBoolean(false);
    private int timeSinceLastUpdate = 0;

    private void taskLoop() throws InterruptedException
    {
        while (running.get())
        {
            if (taskListUpdatePending.get())
            {
                updateTaskList();
                taskListUpdatePending.set(false);
            }

            if (runningJob == null || !runningJob.getIsDownloadRunning())
            {
                JobRunner waitingJob = getWaitingJobRunner();
                if (waitingJob != null)
                {
                    waitingJob.startDownloads();
                    runningJob = waitingJob;
                }

            }

            if(timeSinceLastUpdate++ > 10)
            {
                taskListUpdatePending.set(true);
                timeSinceLastUpdate = 0;
            }
            Thread.sleep(100);

        }
    }

    private JobRunner getWaitingJobRunner()
    {
        for (JobRunner jobRunner : taskQue)
        {
            if (jobRunner.getJobStatus() == JobStatus.QUEUED)
            {
                taskQue.remove(jobRunner);
                taskQue.add(jobRunner);
                return jobRunner;
            }
        }

        for (JobRunner jobRunner : taskQue)
        {
            if (jobRunner.needUpdate())
            {
                taskQue.remove(jobRunner);
                taskQue.add(jobRunner);
                return jobRunner;
            }
        }

        return null;
    }

    public void onTaskStatusChange(StatsHandler handler, JobStatus status)
    {
        setUpdatePending();
    }

    public void setUpdatePending()
    {
        taskListUpdatePending.set(true);
    }


    public synchronized void addTask(JobRunner job)
    {

        JobDescription jobDescription = job.getJobDescription();

        JobListItem item = new JobListItem()
                .setDescription(jobDescription.getEvaluatedName())
                .setStatus(job.getJobStatus().toString())
                .setThreadUrl(job.getJobDescription().getThreadUrl());

        taskQue.add(job);
        threadUrlToJobListItem.put(job.getJobDescription().getThreadUrl(), item);
        jobListItems.add(item);
        job.getStatsHandler().addStatusChangeObs(this::onTaskStatusChange);
        setUpdatePending();

        System.out.println("adding " + job.toString());

    }


    public synchronized void updateTaskList()
    {

        for (JobRunner jr : taskQue)
        {
            JobListItem item = threadUrlToJobListItem.get(jr.getJobDescription().getThreadUrl());

            String rightText = "";
            rightText += jr.getJobStatus().toString();
            if(jr.isAutoUpdate() && jr.getMsToNextUpdate() > 0)
            {
                String countText = " " +  jr.getMsToNextUpdate() / 1000 + "s";
                rightText += countText;
                item.setSleepingNr(countText);
            }
            else
            {
                item.setSleepingNr(null);
            }

            item.setStatus(rightText);
        }
        jobList.notifyObservers(jobListItems);
    }

    public synchronized void addTaskListObserver(CoolObserver<ArrayList<JobListItem>> obs)
    {
        jobList.addObserver(obs);
    }

    public synchronized void removeTask(JobListItem leavingTask)
    {
        threadUrlToJobListItem.remove(leavingTask.getThreadUrl());

        for (JobListItem existingItem : jobListItems)
        {
            if (existingItem.getId() == leavingTask.getId())
            {
                jobListItems.remove(existingItem);
                break;
            }
        }

        for (JobRunner runner : taskQue)
        {
            if (runner.getJobDescription().getThreadUrl().equals(leavingTask.getThreadUrl()))
            {
                runner.cancelDownload();
                taskQue.remove(runner);
                break;
            }
        }

        setUpdatePending();
    }


    @Override
    public void run()
    {
        try
        {
            Thread.currentThread().setName("TaskManager");
            taskLoop();

        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }

    public void notifyApplicationStop()
    {
        running.set(false);
    }
}
