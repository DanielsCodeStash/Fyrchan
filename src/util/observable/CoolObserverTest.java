package util.observable;

public class CoolObserverTest
{


    public static void main(String[] args) throws InterruptedException
    {

        CoolObserver<String> barn = arg -> {
            System.out.println("Jag hör en glassbil som låter " + arg);
        };

        CoolObservable<String> glassbilen = new CoolObservable<>();
        glassbilen.addObserver(barn);

        glassbilen.notifyObservers("DUDU");

    }
}
