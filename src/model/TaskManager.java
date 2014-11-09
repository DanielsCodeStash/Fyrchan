package model;

import model.downloading.JobRunner;
import shared.JobDescription;
import shared.JobListItem;
import shared.JobStatus;
import util.Funtastic;
import util.observable.CoolObservable;
import util.observable.CoolObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

public class TaskManager implements Runnable
{

    private CoolObservable<ArrayList<JobListItem>> jobList = new CoolObservable<>();

    private ArrayList<JobListItem> jobListItems = new ArrayList<>();
    private HashMap<String, JobListItem> threadUrlToJobListItem = new HashMap<>();


    private ConcurrentLinkedQueue<JobRunner> taskQue = new ConcurrentLinkedQueue<>();




    private boolean running = true;


    private void taskLoop() throws InterruptedException
    {
        while (running)
        {
            if(!taskQue.isEmpty())
            {
                JobRunner jobRunner = taskQue.poll();

                if (jobRunner.getJobStatus() == JobStatus.NOT_STARTED)
                {
                    jobRunner.startDownloads();
                    System.out.println("Running job");
                }

                taskQue.add(jobRunner);
            }
            updateTaskList();
            Thread.sleep(100);

        }
    }



    public synchronized void addTask(JobRunner job )
    {

        JobDescription jobDescription = job.getJobDescription();
        JobListItem item = new JobListItem()
                .setDescription(jobDescription.getThreadUrl().substring(1, 20))
                .setStatus(job.getJobStatus().toString());

        taskQue.add(job);
        threadUrlToJobListItem.put(job.getJobDescription().getThreadUrl(), item);
        jobListItems.add(item);

        System.out.println("adding " + job.toString());

    }


    public synchronized void updateTaskList()
    {

        for(JobRunner jr : taskQue)
        {
            JobListItem item = threadUrlToJobListItem.get(jr.getJobDescription().getThreadUrl());
            item.setStatus(jr.getJobStatus().toString());
        }
        jobList.notifyObservers(jobListItems);
    }

    public synchronized  void addTaskListObserver(CoolObserver<ArrayList<JobListItem>> obs)
    {
        jobList.addObserver(obs);
    }


    @Override
    public void run()
    {
        try
        {
            taskLoop();
        } catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
}
