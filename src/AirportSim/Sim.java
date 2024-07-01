package AirportSim;

import java.util.ArrayList;
import java.util.Scanner;

public class Sim   // upper level class to enclose all objects
{

    static ArrayList<ArrayList<Flight>> movingPlanes = new ArrayList<>();

    static ArrayList<Airline> airlines = new ArrayList<>();

    static Scanner airportFileReader = new Scanner("airportNamesAndCoords.txt");

    static ArrayList<Airport> airports = new ArrayList<>();

    public static void main(String[] args)
    {

        for(int i = 0; i < 1440; i++)
        {

            movingPlanes.add(new ArrayList<>());

        }

        while (airportFileReader.hasNext())
        {

            String airportFileLine = airportFileReader.nextLine();
            String[] airportItems = airportFileLine.split(",");
            Airport airport = new Airport(airportItems[0], Double.parseDouble(airportItems[1]),
                                  Double.parseDouble(airportItems[2]), 50);
            airports.add(airport);


        }

        Airline airlineOne = new Airline(airports);
        airlines.add(airlineOne);

        Plane plane1 = new Plane(50);
        airlineOne.addPlane(plane1);

        //simulation loop

        for (int day = 0; day < 5; day++)
        {

            //flight generation loop

            for(Airline airline : airlines)
            {

                airline.generateFlights(day);

            }

            for (int min = 0; min < 1440; min++) {

                if (!(movingPlanes.get(min).isEmpty())) {//begin if statement checking for moving planes

                    for (int j = 0; j < movingPlanes.get(min).size(); j++) {//begin for loop that moves planes in movingPlanes

                        Flight flight = movingPlanes.get(min).get(j);

                        Plane flightPlane = flight.getPlane();

                        flightPlane.movePlane();

                    }//end for loop that moves planes in movingPlanes

                }//end if statement to move planes

            }

        }

    }

}
