package util.observable;

public interface IdentObserver<F, T>
{

    void update(F source, T arg);
}
