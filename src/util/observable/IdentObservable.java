package util.observable;

import java.util.ArrayList;
import java.util.List;

public class IdentObservable<F, T>
{
    private List<IdentObserver<F, T>> observers = new ArrayList<>();

    public synchronized void addObserver(IdentObserver<F, T> o)
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

    public void notifyObservers(F source, T arg)
    {

        List<IdentObserver<F, T>> localObserverList;

        synchronized (this)
        {
            localObserverList = new ArrayList<>(observers);
        }

        for (IdentObserver<F, T> obs : localObserverList)
        {
            obs.update(source, arg);
        }
    }

    public synchronized void deleteObserver(IdentObserver<F, T> o)
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
