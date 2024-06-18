package AirportSim;

import java.util.ArrayList;
import java.util.Arrays;

public class Sim   // upper level class to enclose all objects
{

    static ArrayList<ArrayList<Flight>> movingPlanes = new ArrayList<>();

    static ArrayList<Airline> airlines = new ArrayList<>();

    static ArrayList<String> destinationCities = new ArrayList<>(Arrays.asList("Miami", "Charlotte", "Harrisburg", "Washington DC", "Baltimore", "Jacksonville",
            "Indianapolis", "Fort Lauderdale", "Savannah", "Roanoke", "Detroit", "Tampa", "Atlanta", "Orlando",
            "Los Angeles", "Denver", "San Francisco", "West Palm Beach", "San Jose", "Seattle", "Portland", "Dallas",
            "Austin", "Houston", "Daytona Beach", "Newark", "New York City", "Cincinnati", "Des Moines", "San Diego",
            "Minneapolis", "Phoenix", "Las Vegas", "Boston", "Philadelphia", "Nashville", "Chicago",
            "Key West", "New Orleans", "Birmingham", "Albuquerque", "Oklahoma City", "St. Louis", "Kansas City",
            "Milwaukee", "Boise", "San Antonio", "Jackson", "Memphis", "Huntsville", "Fort Meyers", "Louisville",
            "Green Bay"));

    static ArrayList<Airport> airports = new ArrayList<>();

    public static void main(String[] args)
    {

        for(int i = 0; i < 1440; i++)
        {

            movingPlanes.add(new ArrayList<>());

        }

        for (String city : destinationCities)
        {

            airports.add(new Airport(city, 50));

        }

        Airline airline = new Airline(airports);

        Plane plane1 = new Plane(50);
        airline.addPlane(plane1);

        //flight generation loop

        for (int min = 0; min < 1440; min++)
        {

            if(!(movingPlanes.get(min).isEmpty()))
            {//begin if statement checking for moving planes

                for(int j = 0; j < movingPlanes.get(min).size(); j++)
                {//begin for loop that moves planes in movingPlanes

                    Flight flight = movingPlanes.get(min).get(j);

                    Plane flightPlane = flight.getPlane();

                    flightPlane.movePlane();

                }//end for loop that moves planes in movingPlanes

            }//end if statement to move planes

        }

    }

}
