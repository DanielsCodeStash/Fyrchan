package model;

import model.downloading.JobRunner;
import shared.JobDescription;
import shared.JobListItem;
import shared.JobStats;
import shared.JobStatus;
import util.observable.CoolObservable;
import util.observable.CoolObserver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

public class DownerModel
{



    private TaskManager taskManager;

    private HashMap<StatsHandler, String> statsHandlerToThreadUrl = new HashMap<>();
    private HashMap<String, StatsHandler> threadUrlToStatsHandler = new HashMap<>();
    private String activeThreadUrl = null;

    private CoolObservable<JobStats> activeStatsChange = new CoolObservable<>();
    private CoolObservable<JobStatus> activeStatusChange = new CoolObservable<>();

    public DownerModel()
    {
        taskManager = new TaskManager();
        new Thread(taskManager).start();
    }

    public synchronized void startNewJob(JobDescription jobDescription)
    {
        if(threadUrlToStatsHandler.containsKey(jobDescription.getThreadUrl()))
        {
            setNewActiveThreadUrl(jobDescription.getThreadUrl());
            System.out.println("Tried to add an already existing thread");
            // TODO: if auto-update - elevate priority
            return;
        }

        JobRunner runner = new JobRunner(this, jobDescription);
        StatsHandler handler = runner.getStatsHandler();

        handler.addStatsChangeObs(this::onStatsUpdate);
        handler.addStatusChangeObs(this::onStatusUpdate);

        statsHandlerToThreadUrl.put(handler, jobDescription.getThreadUrl());
        threadUrlToStatsHandler.put(jobDescription.getThreadUrl(), handler);
        activeThreadUrl = jobDescription.getThreadUrl();

        handler.forceStatsUpdate();
        taskManager.addTask(runner);

    }

    public void removeAutoUpdateItem(JobListItem item)
    {
        StatsHandler handler = threadUrlToStatsHandler.get(item.getThreadUrl());
        threadUrlToStatsHandler.remove(item.getThreadUrl());
        statsHandlerToThreadUrl.remove(handler);

        if(activeThreadUrl == null || activeThreadUrl.equals(item.getThreadUrl()))
        {
            Set<String> urls = threadUrlToStatsHandler.keySet();
            if(!urls.isEmpty())
            {
                String newUrl = urls.iterator().next();
                setNewActiveThreadUrl(newUrl);
            }
            else
            {
                activeThreadUrl = null;
                activeStatsChange.notifyObservers(null);
            }
        }

        taskManager.removeTask(item);
    }


    public void setNewActiveThreadUrl(String newActiveThreadUrl)
    {
        System.out.println("new active ThreadUrl: " + newActiveThreadUrl);
        if(activeThreadUrl == null || !activeThreadUrl.equals(newActiveThreadUrl))
        {
            StatsHandler handler = threadUrlToStatsHandler.get(newActiveThreadUrl);
            if(handler != null)
            {
                activeThreadUrl = newActiveThreadUrl;
                handler.forceStatsUpdate();
            }
        }
    }

    private void onStatsUpdate(StatsHandler handler, JobStats stats)
    {
        if(handlerIsFromActiveThread(handler))
        {
            activeStatsChange.notifyObservers(stats);
        }
    }

    private void onStatusUpdate(StatsHandler handler, JobStatus status)
    {
        if(handlerIsFromActiveThread(handler))
            activeStatusChange.notifyObservers(status);

    }

    private boolean handlerIsFromActiveThread(StatsHandler handler)
    {
        if(activeThreadUrl == null)
            return false;

        String handlersThreadUrl = statsHandlerToThreadUrl.get(handler);
        if(handlersThreadUrl == null)
            return false;

        return handlersThreadUrl.equals(activeThreadUrl);
    }

    public void addActiveStatsObserver(CoolObserver<JobStats> obs)
    {
        activeStatsChange.addObserver(obs);
    }

    public void addActiveStatusObserver(CoolObserver<JobStatus> obs)
    {
        activeStatusChange.addObserver(obs);
    }

    public void addTaskListObserver(CoolObserver<ArrayList<JobListItem>> obs)
    {
        taskManager.addTaskListObserver(obs);
    }

}
