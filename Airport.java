package FinalProject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Airport
{//begin Airport class

    //Random object used to create flights, picking from the destinations ArrayList randomly
        //declared outside main method to be used in any method

    static Random random = new Random();

    static ArrayList<ArrayList<ArrayList<String>>> destMap = new ArrayList<>();

    static ArrayList<ArrayList<Integer>> flightTimes = new ArrayList<>();

    static ArrayList<Gate> gates = new ArrayList<>();

    static ArrayList<ArrayList<Flight>> movingPlanes = new ArrayList<>();

    static ArrayList<ArrayList<Gate>> availableGates = new ArrayList<>();

    static int numOfGates;

    public static void main(String[] args)
    {//begin main method

        numOfGates = 50;

        Airline airlineOne = getAirline();

        //determining location of airport and making flight times to other airports

        for(int i = 0; i < 9; i++) {

            destMap.add(new ArrayList<>());

            for(int j = 0; j < 16; j++)
            {

                destMap.get(i).add(new ArrayList<>());

            }

        }

        for(int i = 0; i < 2880; i++)
        {

            movingPlanes.add(new ArrayList<>());
            availableGates.add(new ArrayList<>());

        }

        fillDestMap(destMap);

        String airportLocation = "Seattle";

        int[] originCoords = findAirportCoords(airportLocation);

        airlineOne.removeFromDestinations(airportLocation);

        int[] mapSize = {destMap.size(), destMap.get(0).size()};

        generateFlightTimesAL(originCoords, mapSize);

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

        //create gates

        for(int i = 0; i < numOfGates; i++)
        {//begin gate declaration loop

            gates.add(new Gate(50, i+1 + ""));

        }//end gate declaration loop

        //Passenger class object for accessing and updating passengers

        Passenger passengerUtility;

        int minutes = 600;
        //flight generation loop

        for (int i = 0; (minutes < 1000); i++)
        {//begin flight generation loop

            if(i < gates.size())
            {//begin flight generator before all gates have been assigned

                genFlightAndInfo(airLine, origin, minutes, gates.get(i));

                if(i > 0)
                    for (int j = 0; j < i; j++)
                    {//begin flight generator when previous gates will be empty

                        if(minutes > gates.get(j).getLastPlane().getFlight().getDepartureTime() + 15)
                            genFlightAndInfo(airLine, origin, minutes, gates.get(j));

                    }//end flight generator when previous gates will be empty

            }//end flight generator before all gates have been assigned

            if(i > gates.size())
            {//begin if all gates have been assigned a plane

                for(Gate gate : gates)
                {//begin additional flight creator

                    if (!gate.planeQueue.isEmpty())
                    {

                        int gateFlightDepTime = gate.planeQueue.peekLast().getFlight().getDepartureTime();

                        if(minutes > (gateFlightDepTime)+15)
                        {//begin flight generator when gate will be empty for 15 min

                            genFlightAndInfo(airLine, origin, minutes, gate);

                        }//end flight generator when gate will be empty for 15 min

                    }

                }//end additional flight creator

            }//end if all gates have been assigned a plane

            minutes += random.nextInt(10)+1;

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

    public static void fillDestMap(ArrayList<ArrayList<ArrayList<String>>> destMap)
    {//begin fillDestMap

        // "Miami", "Charlotte", "Harrisburg", "Washington DC", "Baltimore", "Jacksonville",

        destMap.get(8).get(12).add("Miami");
        destMap.get(5).get(12).add("Charlotte");
        destMap.get(3).get(13).add("Harrisburg");
        destMap.get(4).get(13).add("Washington DC");
        destMap.get(4).get(13).add("Baltimore");
        destMap.get(8).get(12).add("Jacksonville");

        // "Indianapolis", "Fort Lauderdale", "Savannah", "Roanoke", "Detroit", "Tampa", "Atlanta", "Orlando",

        destMap.get(4).get(11).add("Indianapolis");
        destMap.get(8).get(12).add("Fort Lauderdale");
        destMap.get(7).get(12).add("Savannah");
        destMap.get(4).get(12).add("Roanoke");
        destMap.get(3).get(11).add("Detroit");
        destMap.get(7).get(12).add("Tampa");
        destMap.get(6).get(11).add("Atlanta");
        destMap.get(7).get(12).add("Orlando");

        // "Los Angeles", "Denver", "San Francisco", "West Palm Beach", "San Jose", "Seattle", "Portland", "Dallas",

        destMap.get(6).get(2).add("Los Angeles");
        destMap.get(4).get(5).add("Denver");
        destMap.get(4).get(0).add("San Francisco");
        destMap.get(8).get(12).add("West Palm Beach");
        destMap.get(4).get(1).add("San Jose");
        destMap.get(1).get(1).add("Seattle");
        destMap.get(1).get(0).add("Portland");
        destMap.get(6).get(8).add("Dallas");

        // "Austin", "Houston", "Daytona Beach", "Newark", "New York City", "Cincinnati", "Des Moines", "San Diego",

        destMap.get(7).get(7).add("Austin");
        destMap.get(7).get(8).add("Houston");
        destMap.get(7).get(12).add("Daytona Beach");
        destMap.get(3).get(14).add("Newark");
        destMap.get(3).get(14).add("New York City");
        destMap.get(4).get(11).add("Cincinnati");
        destMap.get(3).get(8).add("Des Moines");
        destMap.get(6).get(2).add("San Diego");

        // "Minneapolis", "Phoenix", "Las Vegas", "Boston", "Philadelphia", "Nashville", "Chicago",

        destMap.get(2).get(9).add("Minneapolis");
        destMap.get(6).get(3).add("Phoenix");
        destMap.get(5).get(3).add("Las Vegas");
        destMap.get(3).get(15).add("Boston");
        destMap.get(3).get(13).add("Philadelphia");
        destMap.get(5).get(10).add("Nashville");
        destMap.get(3).get(10).add("Chicago");

        // "Key West", "New Orleans", "Birmingham", "Albuquerque", "Oklahoma City", "St. Louis", "Kansas City",

        destMap.get(8).get(12).add("Key West");
        destMap.get(7).get(9).add("New Orleans");
        destMap.get(6).get(10).add("Birmingham");
        destMap.get(5).get(5).add("Albuquerque");
        destMap.get(5).get(7).add("Oklahoma City");
        destMap.get(4).get(9).add("St. Louis");
        destMap.get(4).get(8).add("Kansas City");

        // "Milwaukee", "Boise", "San Antonio", "Jackson", "Memphis", "Huntsville", "Fort Meyers", "Louisville",

        destMap.get(2).get(10).add("Milwaukee");
        destMap.get(2).get(2).add("Boise");
        destMap.get(6).get(9).add("Jackson");
        destMap.get(5).get(9).add("Memphis");
        destMap.get(5).get(10).add("Huntsville");
        destMap.get(8).get(12).add("Fort Meyers");
        destMap.get(4).get(11).add("Louisville");

        // "Green Bay"

        destMap.get(2).get(10).add("Green Bay");

    }//end fillDestMap

    public static int[] findAirportCoords(String city)
    {

        int[] airportCoords = new int[2];

        int destRow = -1;
        int destColumn = -1;

        int row = 0;
        int column;

        do
        {

            column = 0;

            do
            {

                if(destMap.get(row).get(column).contains(city))
                {

                    destRow = row;
                    destColumn = column;

                    airportCoords[0] = row;
                    airportCoords[1] = column;

                }

                column++;

            }while(destColumn == -1 && column < destMap.get(row).size());

            row++;

        }while(destRow == -1 && row < destMap.size());

        return airportCoords;

    }

    public static int generateLandingTime(String destination)
    {

        int[] destCoords = findAirportCoords(destination);

        return flightTimes.get(destCoords[0]).get(destCoords[1]);

    }

    public static void generateFlightTimesAL(int[] originCoords, int[] mapSize)
    {

        for(int i = 0; i < mapSize[0]; i++)
        {

            flightTimes.add(new ArrayList<>());

        }
        for(int row = 0; row < mapSize[0]; row++)
        {

            for (int col = 0; col < mapSize[1]; col++)
            {

                 int flightDistanceLat = Math.abs((row-originCoords[0]));
                 int flightDistanceLong = Math.abs((col-originCoords[1]));

                 int flightTime = (int) Math.round(Math.sqrt(Math.pow(((30) * flightDistanceLat),2)+(Math.pow(((30) * flightDistanceLong),2))));

                 if(flightTime == 0)
                     flightTime = 30;

                 flightTimes.get(row).add(flightTime);

            }

        }

    }

    //method for creating flights, and setting the other fields associated with the Flight

    public static void genFlightAndInfo(Airline airLine, String origin, int minutes, Gate gate)
    {//begin genFlightAndInfo

        Plane plane = new Plane(airLine.getAirlineFleet().get(0).getPassengerCapacity());

        Flight flight = airLine.generateFlight(plane, origin, getHour(minutes), getMin(minutes), minutes, gate);

        int flightTime = generateLandingTime(flight.getDestination());

        int departureTime = flight.getDepartureTime();

        int landingTime = (departureTime + flightTime);

        flight.setLandingTime(landingTime);

        flight.setLandingTimeHour(getHour(landingTime));

        flight.setLandingTimeMin(getMin(landingTime));

        plane.setFlight(flight);

        plane.setFlightTimes(flightTime);

        for(int i = departureTime; i <= landingTime; i++)
        {//begin movingPlanes addition for loop

            movingPlanes.get(i).add(flight);

        }//end movingPlanes addition for loop

    }//end genFlightAndInfo

}//end Airport class
