package AirportSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Sim   // upper level class to enclose all objects
{

    static ArrayList<MovingObject> movingObjects = new ArrayList<>();

    static ArrayList<ArrayList<MovingObject>> objectMovementSchedule = new ArrayList<>();

    static ArrayList<Airline> airlines = new ArrayList<>();

    static File airportFile = new File("airportNamesAndCoords");

    static Scanner airportFileReader;

    static String airlineSeatClasses;

    static PlaneManufacturer planeManufacturerOne;

    static {
        try {
            airportFileReader = new Scanner(airportFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static ArrayList<Airport> airports = new ArrayList<>();

//----------------------Main Method---------------------------------

    public static void main(String[] args)
    {

        initializeObjectMovementSchedule();
        initializeAirports();
        initializeAirlines();
        initializePlaneManufacturers();
        initializePlanes();
        acquirePlanes();

        runUpdateLoop(1);

    }

//----------------------Other Methods---------------------------------

    public static void addToObjectMovementSchedule(int startTime, MovingObject object)
    {

        objectMovementSchedule.get(startTime).add(object);

    }

    public static void runUpdateLoop(int days)
    {

        //simulation loop

        for (int day = 0; day < days; day++)
        {

            //flight generation loop

//            for (Airline airline : airlines)
//            {
//
//                airline.generateFlights(day);
//
//            }
//
//            // System.out.println("Flights generated: " + airlineOne.getFlights().size());
//
//            // sim update loop
//
//            for (int min = 0; min < 1440; min++)
//            {
//
//                for (Airport airport : airports)
//                {
//
//                    if(!airport.getDepartures().isEmpty())
//                        airport.updateAirport(min);
//
//                }
//
//                ArrayList<MovingObject> objectsReadyToMove = objectMovementSchedule.get(min);
//
//                if (!(objectsReadyToMove.isEmpty()))
//                {
//
//                    movingObjects.addAll(objectsReadyToMove);
//
//                }
//
//                if (!(movingObjects.isEmpty()))
//                {//begin if statement checking for moving objects
//
//                    for (MovingObject movingObject : movingObjects)
//                    {//begin for loop that moves objects in movingObjects
//
//                        movingObject.move();
//
//                    }//end for loop that moves planes in movingObjects
//
//                }//end if statement to move moving objects
//
//            }

        }

    }

    public static void initializeObjectMovementSchedule()
    {

        for (int i = 0; i < 1440; i++)
        {

            objectMovementSchedule.add(new ArrayList<>());

        }

    }

    public static void initializeAirports()
    {

//        while (airportFileReader.hasNext())
//        {
//
//            String airportFileLine = airportFileReader.nextLine();
//            String[] airportItems = airportFileLine.split(",");
//            Airport airport = new Airport(airportItems[0], airportItems[1], Double.parseDouble(airportItems[2]),
//                    Double.parseDouble(airportItems[3]), 50);
//            airports.add(airport);
//
//        }

    }

    public static void initializeAirlines()
    {

        String airlineOneSeatClasses = "Economy (E): Seat;First (F): Lie-flat Seat,Pillows,Blanket";

        Airline airlineOne = new Airline("Airline One", "AL1", airports, airlineOneSeatClasses);
        airlines.add(airlineOne);

    }

    public static void initializePlaneManufacturers()
    {

        planeManufacturerOne = new PlaneManufacturer();

    }

    public static void initializePlanes()
    {

        planeManufacturerOne.createPlaneModel("plane1", 50);

        for (int i = 0; i < 100; i++)
        {

            planeManufacturerOne.createPlane("plane1");

        }

    }

    public static void acquirePlanes()
    {

        Airline airlineOne = airlines.getFirst();

        HashMap<String, Plane> modelCatalog = planeManufacturerOne.getPlaneCatalog();
        int modelCatalogLength = modelCatalog.size();
        String[] planeModels = modelCatalog.keySet().toArray(new String[modelCatalogLength]);
        String desiredModelName = planeModels[0];
        Plane desiredModel = modelCatalog.get(desiredModelName);

        String airlineOnePlane1SeatLayoutStr = "Row 1: FF,Row 2-13: EEEE";

        airlineOne.createPlaneConfiguration(airlineOnePlane1SeatLayoutStr, desiredModel);

        for (int i = 0; i < 100; i++)
        {

            airlineOne.requestPlane(desiredModelName, planeManufacturerOne);

        }

        System.out.println("test");

    }

}
