package util.observable;

public class IdentObservableTest
{

    public static class IceCreamTruck
    {
        public String truckColor;

        public IceCreamTruck(String color)
        {
            truckColor = color;
        }
    }

    public static void main(String[] args)
    {
        // set up trucks
        IceCreamTruck blueTruck = new IceCreamTruck("blue");
        IceCreamTruck greenTruck = new IceCreamTruck("green");
        IdentObservable<IceCreamTruck, String> streetWatch = new IdentObservable<>();

        // se to it that children scream when a truck approaches
        streetWatch.addObserver((truck, sound) -> System.out.println("I hear a " + truck.truckColor + " truck go " + sound));

        // TRUCKS DRIVING BY
        streetWatch.notifyObservers(blueTruck, "DUDUDU");
        streetWatch.notifyObservers(greenTruck, "TUUUUUUTH");

    }
}
