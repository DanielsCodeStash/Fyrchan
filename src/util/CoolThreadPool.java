package util;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class CoolThreadPool<T, F extends Callable<T>>
{

    private ExecutorService service;

    private ArrayList<F> tasks = new ArrayList<>();
    private List<Future<T>> futures = new ArrayList<>();

    public CoolThreadPool(int numThreads)
    {
        this.service = Executors.newFixedThreadPool(numThreads);
    }

    public void addTask(F task)
    {
        tasks.add(task);
    }

    public void startTasks()
    {
        for (F task : tasks)
        {
            Future<T> future = service.submit(task);
            futures.add(future);
        }
    }

    public boolean allTasksDone()
    {
        for (Future<T> future : futures)
        {
            if (!future.isDone())
                return false;
        }
        service.shutdown();
        return true;
    }

    public void cancelRemainingTasks()
    {
        service.shutdownNow();

    }

    public void awaitCancellation() throws InterruptedException
    {
        service.awaitTermination(100, TimeUnit.SECONDS);
    }


    public ArrayList<F> getTasks()
    {
        return tasks;
    }
}
