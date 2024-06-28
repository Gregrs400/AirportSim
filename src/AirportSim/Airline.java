package AirportSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;
import AirportSim.Time;

public class Airline
{//begin Airline class

    private final Random random = new Random();

    private final Time time = new Time();

    //String ArrayList of destinations offered by an airline from this airport

    private ArrayList<Airport> destinations = new ArrayList<>();

    public void setDestinations(ArrayList<Airport> destinations) {this.destinations = destinations;}

    //Integer ArrayList of Flight Numbers of flights created by the airline

    private ArrayList<Integer> flightNumbers = new ArrayList<>();

    public void setFlightNumbers(ArrayList<Integer> flightNumbers) {this.flightNumbers = flightNumbers;}

    //Flight object ArrayList storing the flights created by the airline

    private final ArrayList<Flight> flights = new ArrayList<>();

    public ArrayList<Flight> getFlights() {return flights;}

    //Plane object ArrayList storing the different plane models in an airline's fleet

    private final ArrayList<Plane> airlineFleet = new ArrayList<>();

    public ArrayList<Plane> getAirlineFleet() {return airlineFleet;}

    //Hashmap allowing retrieval of Flight objects when given flight numbers

    private final ArrayList<Plane> availablePlanes = new ArrayList<>();

    //Airline parameterized constructor

    public Airline(ArrayList<Airport> destinations)
    {//begin Airline parameterized constructor

        setDestinations(destinations);

    }//end Airline parameterized constructor

    public Airline(Airline anotherAirline)
    {

        this(anotherAirline.destinations);
        this.flightNumbers = anotherAirline.flightNumbers;

    }

    //addPlane to add a plane to an airline's fleet

    public void addPlane(Plane plane)
    {//begin addPlane

        airlineFleet.add(plane);
        availablePlanes.add(plane);

    }//end addPlane

    //generateFlight to create a flight for an airline, assigning each flight a plane, a destination, a flight number, and a departure time


    public Flight generateFlight()
    {

        Airport origin, destination;

        int flightNumber;

        //do-while loop to ensure each flight number is unique

        do
        {//begin do-while loop

            flightNumber = random.nextInt(9999)+1;

        }while(flightNumbers.contains(flightNumber)); //end do-while loop

        flightNumbers.add(flightNumber);

        Plane plane = availablePlanes.get(0);
        availablePlanes.remove(0);

        int originIndex = random.nextInt(destinations.size());

        int destinationIndex = random.nextInt(destinations.size());

        origin = destinations.get(originIndex);

        if (destinationIndex == originIndex)
        {
            while (destinationIndex == originIndex) {
                destinationIndex = random.nextInt(destinations.size());
            }
        }

        destination = destinations.get(destinationIndex);

        Flight flight = new Flight(plane, destination, origin, flightNumber);

        flights.add(flight);

        addFlightToDepartures(origin, flight);
        addFlightToArrivals(destination, flight);

        return flight;

    }
    public Flight generateFlight(Plane plane, Airport origin, int departHour, int departMin, int departTime, Gate gate)
    {//begin generateFlight

        Airport destination;

        //do-while loop to ensure each flight number is unique

        destination = destinations.get(random.nextInt(destinations.size()));

        Flight flight = new Flight(plane, origin, destination, generateFlightNumber(),
                                   departHour, departMin, departTime, gate);

        flights.add(flight);

        return flight;

    }

    public void generateFlights()
    {

        for (Plane plane : airlineFleet)
        {

            int originIndex = random.nextInt(airlineFleet.size()-1);
            int destIndex = random.nextInt(airlineFleet.size()-1);

            if (destIndex == originIndex)
            {

                while (destIndex == originIndex)
                {

                    destIndex = random.nextInt(airlineFleet.size()-1);

                }

            }

            Airport origin = destinations.get(originIndex);
            Airport destination = destinations.get(destIndex);

            Flight flight = new Flight(plane, origin, destination, generateFlightNumber());

            flights.add(flight);

        }

    }

    public int getPassengerTotal()
    {

        int passengerTotal = 0;

        for(Flight flight : flights)
        {

            Plane flightPlane = flight.getPlane();

            passengerTotal += flightPlane.getPassengers().size();


        }

        return passengerTotal;

    }

    public void removeFromDestinations(Airport destination)
    {//begin removeFromDestinations

        destinations.remove(destination);

    }//end removeFromDestinations

    public void addFlightToDepartures(Airport origin, Flight flight)
    {

        origin.getDepartures().add(flight);

    }

    public void addFlightToArrivals(Airport destination, Flight flight)
    {

        destination.getArrivals().add(flight);

    }

    public int generateFlightNumber()
    {

        int flightNumber;

        do
        {//begin do-while loop

            flightNumber = random.nextInt(9999)+1;

        }while(flightNumbers.contains(flightNumber)); //end do-while loop

        flightNumbers.add(flightNumber);

        return flightNumber;

    }

    public int generateFlightTime(int filedSpeed, Airport origin, Airport destination)
    {

        double differenceLat;
        double differenceLong;
        double numA;
        double numB;
        double pythagDistanceMiles;
        double pythagDistanceNM;
        int flightTime;

        differenceLat = destination.getLatitude() - origin.getLatitude();
        differenceLong = destination.getLongitude() - origin.getLongitude();

        numA = Math.abs(differenceLat) * 69;
        numB = Math.cos(differenceLat*(Math.PI / 180) * 69) * Math.abs(differenceLong);

        pythagDistanceMiles = Math.sqrt(Math.pow(numA, 2) + Math.pow(numB, 2));
        pythagDistanceNM = pythagDistanceMiles * 0.868976;

        flightTime = (int) pythagDistanceNM * (filedSpeed / 60);

        return flightTime;

    }

    public double milesToNauticalMiles(double miles){ return miles * 0.86897624; }

}//end Airline class
