package util.observable;

import java.util.ArrayList;
import java.util.List;

public class CoolObservable<T>
{
    private List<CoolObserver<T>> observers = new ArrayList<>();

    public synchronized void addObserver(CoolObserver<T> o)
    {
        if (o == null)
        {
            throw new NullPointerException();
        }
        if (!observers.contains(o))
        {
            observers.add(o);
        }
    }

    public void notifyObservers(T arg)
    {

        List<CoolObserver<T>> localObserverList;

        synchronized (this)
        {
            localObserverList = new ArrayList<>(observers);
        }

        for (CoolObserver<T> obs : localObserverList)
        {
            obs.update(arg);
        }
    }

    public synchronized void deleteObserver(CoolObserver<T> o)
    {
        observers.remove(o);
    }

    public synchronized void deleteObservers()
    {
        observers.clear();
    }

    public synchronized int countObservers()
    {
        return observers.size();
    }

}
