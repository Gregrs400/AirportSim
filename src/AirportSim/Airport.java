package AirportSim;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Airport
{//begin Airport class

    //Random object used to create flights, picking from the destinations ArrayList randomly
        //declared outside main method to be used in any method

    static Random random = new Random();

    static ArrayList<Gate> gates = new ArrayList<>();

    static ArrayList<ArrayList<Flight>> movingPlanes = new ArrayList<>();

    static ArrayList<ArrayList<Gate>> availableGates = new ArrayList<>();

    static int numOfGates;

    public static void main(String[] args)
    {//begin main method

        numOfGates = 50;

        //create gates

        for(int i = 0; i < numOfGates; i++)
        {//begin gate declaration loop

            gates.add(new Gate(50, i+1 + ""));

        }//end gate declaration loop

        Airline airlineOne = getAirline();

        for(int i = 0; i < 2880; i++)
        {

            movingPlanes.add(new ArrayList<>());
                availableGates.add(new ArrayList<>(gates));

        }

        String airportLocation = "Seattle";

        airlineOne.removeFromDestinations(airportLocation);

        //creating a plane, plane1, with 50 passenger capacity

        Plane plane1 = new Plane(50);

        //adding plane1 to airlineOne's fleet

        airlineOne.addPlane(plane1);

        //creating a plane, plane2, with 100 passenger capacity

        Plane plane2 = new Plane(100);

        //adding plane2 to airlineOne's fleet

        airlineOne.addPlane(plane2);

        ArrayList<Passenger> paxInAirport = new ArrayList<>();

        newDay(airlineOne, airportLocation, paxInAirport);

        System.out.println("\nNum of Flights: " + airlineOne.getFlights().size());

        System.out.println("Total pax: " + airlineOne.getPassengerTotal());

    }//end main method

    private static Airline getAirline() {
        ArrayList<Integer> flightNumbers = new ArrayList<>();

        //String ArrayList alOneDestinations, representing the destinations served by Airline One

        //Adding the destinations for Airline One to the alOneDestinations ArrayList

        ArrayList<String> alOneDestinations = new ArrayList<>(Arrays.asList("Miami", "Charlotte", "Harrisburg", "Washington DC", "Baltimore", "Jacksonville",
                "Indianapolis", "Fort Lauderdale", "Savannah", "Roanoke", "Detroit", "Tampa", "Atlanta", "Orlando",
                "Los Angeles", "Denver", "San Francisco", "West Palm Beach", "San Jose", "Seattle", "Portland", "Dallas",
                "Austin", "Houston", "Daytona Beach", "Newark", "New York City", "Cincinnati", "Des Moines", "San Diego",
                "Minneapolis", "Phoenix", "Las Vegas", "Boston", "Philadelphia", "Nashville", "Chicago",
                "Key West", "New Orleans", "Birmingham", "Albuquerque", "Oklahoma City", "St. Louis", "Kansas City",
                "Milwaukee", "Boise", "San Antonio", "Jackson", "Memphis", "Huntsville", "Fort Meyers", "Louisville",
                "Green Bay"));

        //Creating the Airline object for Airline One, passing the destinations and flightNumbers ArrayLists

        return new Airline(alOneDestinations, flightNumbers);

    }

    public static void newDay(Airline airLine, String origin, ArrayList<Passenger> paxInAirport)
    {//begin newDay

        //Passenger class object for accessing and updating passengers

        Passenger passengerUtility;

        //flight generation loop

        for (int minutes = 600; minutes < 1000; minutes++)
        {//begin flight generation loop

            while(!(availableGates.get(minutes).isEmpty()))
            {//begin flight generator

                genFlightAndInfo(airLine, origin, minutes+15, availableGates.get(minutes).get(0));

            }//end flight generator

        }//end flight generation loop

        //actual airport operations loop

        int paxGenerated, paxBeingGenerated;

        paxGenerated = 0;

        paxBeingGenerated = 300;

        int totalPaxCapacity = airLine.getFlights().size() * 50;

        for (int min = 0; min < 1440; min++)
        {//begin outer for loop

            if((paxGenerated < totalPaxCapacity) && min < 1000)
            {//begin passenger arrival iterator

                if(totalPaxCapacity - paxGenerated < paxBeingGenerated)
                    paxBeingGenerated = totalPaxCapacity - paxGenerated;

                paxArrival(airLine, paxInAirport, paxBeingGenerated);

                paxGenerated += paxBeingGenerated;

            }//end passenger arrival iterator

            if(!paxInAirport.isEmpty())
            {//begin if passengers are in airport

                for (int paxElement = 0; paxElement < paxInAirport.size(); paxElement++)
                {//begin passenger status updater

                    passengerUtility = paxInAirport.get(paxElement);

                    Flight paxFlight = airLine.paxFlightFromNum(passengerUtility);

                    Plane paxPlane = paxFlight.getPlane();

                    Gate paxGate = paxFlight.getGate();

                    if (!(passengerUtility.isAtGate()))
                    {//begin if passenger needs to move

                        passengerUtility.movePassenger();

                    }//end if passenger needs to move
                    else if(paxGate.getPaxAtGate().size() < paxGate.getSeats())
                            paxGate.addPaxToGate(passengerUtility);

                    if (passengerUtility.isAtGate() && ((min > (paxFlight.getDepartureTime() - 15)) && min < paxFlight.getDepartureTime()))
                    {//begin if passenger can board plane

                        paxPlane.addPaxToPlane(passengerUtility);

                        paxGate.getPaxAtGate().remove(passengerUtility);

                        paxInAirport.remove(passengerUtility);

                    }//end if passenger can board plane

                }//end passenger status updater

            }//end if passengers are in airport

            if(!(movingPlanes.get(min).isEmpty()))
            {//begin if statement checking for moving planes

                for(int j = 0; j < movingPlanes.get(min).size(); j++)
                {//begin for loop that moves planes in movingPlanes

                    Flight flight = movingPlanes.get(min).get(j);

                    Plane flightPlane = flight.getPlane();

                    flightPlane.movePlane();

                }//end for loop that moves planes in movingPlanes

            }//end if statement to move planes

        }//end outer for loop

    }//end newDay

    public static void paxArrival(Airline airline, ArrayList<Passenger> paxInAirport, int pax)
    {

        ArrayList<Flight> airlineFlights = airline.getFlights();

        for (int i = 0; i < pax; i++)
        {//begin for loop

            Flight ranFlight;

            do
            {

                ranFlight = airlineFlights.get(random.nextInt(airline.getFlights().size()));

            }while(ranFlight.isSoldOut());

            Passenger passenger = new Passenger(ranFlight.getNumber(), ranFlight.getDestination(), (ranFlight.getNumber() +
                    "_" + (ranFlight.getPaxWithTickets().size() + 1)));

            paxInAirport.add(passenger);

            ranFlight.getPaxWithTickets().add(passenger);

        }//end for loop

    }//end paxArrival

    public static int getHour(int minutes)
    {

        if(minutes > 1440)
            minutes -= 1440;

        return minutes / 60;

    }

    public static int getMin(int minutes)
    {

        if(minutes > 1440)
            minutes -= 1440;

        return minutes % 60;

    }

    //method for creating flights, and setting the other fields associated with the Flight

    public static void genFlightAndInfo(Airline airLine, String origin, int minutes, Gate gate)
    {//begin genFlightAndInfo

        Plane plane = new Plane(airLine.getAirlineFleet().get(0).getPassengerCapacity());

        Flight flight = airLine.generateFlight(plane, origin, getHour(minutes), getMin(minutes), minutes, gate);

        int flightTime = 0;

        int departureTime = flight.getDepartureTime();

        int landingTime = (departureTime + flightTime);

        flight.setLandingTime(landingTime);

        flight.setLandingTimeHour(getHour(landingTime));

        flight.setLandingTimeMin(getMin(landingTime));

        plane.setFlight(flight);

        plane.setFlightTimes(flightTime);

        for(int i = departureTime-15; i < departureTime; i++)
        {

            availableGates.get(i).remove(gate);

        }

        for(int i = departureTime; i <= landingTime; i++)
        {//begin movingPlanes addition for loop

            movingPlanes.get(i).add(flight);

        }//end movingPlanes addition for loop

    }//end genFlightAndInfo

}//end Airport class
