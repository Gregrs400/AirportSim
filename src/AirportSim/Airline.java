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

    private final HashMap<Integer, Flight> flightNumToFlight = new HashMap<>();

    public HashMap<Integer, Flight> getFlightNumToFlight() {return flightNumToFlight;}

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

        flightNumToFlight.put(flight.getNumber(), flight);

        return flight;

    }
    public Flight generateFlight(Plane plane, Airport origin, int departHour, int departMin, int departTime, Gate gate)
    {//begin generateFlight

        Airport destination;

        int flightNumber;

        //do-while loop to ensure each flight number is unique

        do
        {//begin do-while loop

            flightNumber = random.nextInt(9999)+1;

        }while(flightNumbers.contains(flightNumber)); //end do-while loop

        flightNumbers.add(flightNumber);

        destination = destinations.get(random.nextInt(destinations.size()));

        Flight flight = new Flight(plane, destination, origin, flightNumber, departHour, departMin, departTime, gate);

        flights.add(flight);

        flightNumToFlight.put(flight.getNumber(), flight);

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

    public Flight paxFlightFromNum(Passenger passenger)
    {
        return getFlightNumToFlight().get(passenger.getFlightNumber());
    }

    public void removeFromDestinations(Airport destination)
    {//begin removeFromDestinations

        destinations.remove(destination);

    }//end removeFromDestinations

}//end Airline class
