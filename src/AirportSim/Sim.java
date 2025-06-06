package AirportSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Sim   // upper level class to enclose all objects
{

    static ArrayList<ArrayList<MovingObject>> movingObjects = new ArrayList<>();

    static ArrayList<Airline> airlines = new ArrayList<>();

    static File airportFile = new File("airportNamesAndCoords");

    static Scanner airportFileReader;

    static {
        try {
            airportFileReader = new Scanner(airportFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static ArrayList<Airport> airports = new ArrayList<>();

    public static void main(String[] args)
    {

        for (int i = 0; i < 1440; i++)
        {

            movingObjects.add(new ArrayList<>());

        }

        while (airportFileReader.hasNext())
        {

            String airportFileLine = airportFileReader.nextLine();
            String[] airportItems = airportFileLine.split(",");
            Airport airport = new Airport(airportItems[0], airportItems[1], Double.parseDouble(airportItems[2]),
                                  Double.parseDouble(airportItems[3]), 50);
            airports.add(airport);

        }

        Airline airlineOne = new Airline(airports);
        airlines.add(airlineOne);

        Plane plane1 = new Plane(50);

        Plane plane2 = new Plane(100);

        String airlineOnePlane1SeatLayoutStr = "Row 1: FF,Row 2-13: EEEE";

        PlaneSeatClass airlineOneEconomyClass = new PlaneSeatClass("airlineOneEconomy",
                new ArrayList<>(List.of("Seat")), "E");
        PlaneSeatClass airlineOneFirstClass = new PlaneSeatClass("airlineOneFirst",
                new ArrayList<>(List.of("Lie-flat Seat", "Pillows", "Blanket")), "F");

        PlaneSeat airlineOneEconomySeat = new PlaneSeat("a1sampleEconomy", airlineOne, airlineOneEconomyClass);
        airlineOne.addPlaneSeat("Economy", airlineOneEconomySeat);
        PlaneSeat airlineOneFirstSeat = new PlaneSeat("a1sampleFirst", airlineOne, airlineOneFirstClass);
        airlineOne.addPlaneSeat("First", airlineOneFirstSeat);

        Map<String, PlaneSeat> airlineOneSeatTemplateMap = new HashMap<>();
        airlineOneSeatTemplateMap.put(airlineOneEconomySeat.getSeatClass().getClassCode(), airlineOneEconomySeat);
        airlineOneSeatTemplateMap.put(airlineOneFirstSeat.getSeatClass().getClassCode(), airlineOneFirstSeat);

        Plane airlineOnePlane1Template = new Plane(plane1, airlineOnePlane1SeatLayoutStr, airlineOneSeatTemplateMap);

        for (int i = 0; i < 1; i++)
        {

            airlineOne.addPlane(new Plane(airlineOnePlane1Template, "a1p1_" + i));

        }

        Passenger user = new Passenger(airlineOne.getFlights().get(1), "user");

        user.reservePlaneSeat("1A");

        //simulation loop

        for (int day = 0; day < 5; day++)
        {

            //flight generation loop

            for (Airline airline : airlines)
            {

                airline.generateFlights(day);

            }

            for (int min = 0; min < 1440; min++)
            {

                if (!(movingObjects.get(min).isEmpty()))
                {//begin if statement checking for moving objects

                    for (int j = 0; j < movingObjects.get(min).size(); j++)
                    {//begin for loop that moves objects in movingObjects

                        movingObjects.get(min).get(j).move();

                    }//end for loop that moves planes in movingObjects

                }//end if statement to move moving objects

            }

        }

    }

    public static void addToMovingObjects(int startTime, int duration, MovingObject object)
    {

        int i = startTime;

        while (duration > 0)
        {

            if (i == 1441)
            {
               i = 0;
            }

            movingObjects.get(i).add(object);

            i++;
            duration--;

        }

    }

}
