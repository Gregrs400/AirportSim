package FinalProject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Airline
{//begin Airline class

    //String ArrayList of destinations offered by an airline from this airport

    private ArrayList<String> destinations = new ArrayList<>();

    public void setDestinations(ArrayList<String> destinations) {this.destinations = destinations;}

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

    //Airline parameterized constructor

    public Airline(ArrayList<String> destinations, ArrayList<Integer> flightNumbers)
    {//begin Airline parameterized constructor

        setDestinations(destinations);
        setFlightNumbers(flightNumbers);

    }//end Airline parameterized constructor

    //addPlane to add a plane to an airline's fleet

    public void addPlane(Plane plane)
    {//begin addPlane

        airlineFleet.add(plane);

    }//end addPlane

    //generateFlight to create a flight for an airline, assigning each flight a plane, a destination, a flight number, and a departure time

    public Flight generateFlight(Plane plane, String origin, int departHour, int departMin, int departTime, Gate gate)
    {//begin generateFlight

        Random random = new Random();

        String destination;

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

    //printFlightNumbers method to print any flight numbers in the flights ArrayList that match the destination in the call

    public void printFlightNumbers(String destination)
    {//begin printFlightNumbers

        for(Flight flight : flights)
        {//begin for loop

            if(flight.getDestination().equals(destination))
            {//begin if statement

                System.out.println(flight.getNumber());

            }//end if statement

        }//end for loop

    }//end printFlightNumbers

    public void printFlightsWithSameDest(String destination)
    {//begin printFlightsWithSameDest

        for(Flight flight : flights)
        {//begin for loop

            if(flight.getDestination().equals(destination))
            {//begin if statement

                flight.printFlight();

            }//end if statement

        }//end for loop

    }//end printFlightsWithSameDest

    public void printAllFlights()
    {

        for(Flight flight : flights)
        {

            flight.printFlight();

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

    public Passenger getPassengerOnFlight(int flightElement, int passengerElement)
    {

        return getFlights().get(flightElement).getPaxWithTickets().get(passengerElement);

    }

    public void printPaxWithSameDest(String destination)
    {

        for(Flight flight : flights)
        {

            if(flight.getDestination().equals(destination))
            {

                for(Passenger passenger : flight.getPaxWithTickets())
                {

                    passenger.printPassenger();

                }
            }
        }
    }

    public Flight paxFlightFromNum(Passenger passenger)
    {
        return getFlightNumToFlight().get(passenger.getFlightNumber());
    }

    public void removeFromDestinations(String destination)
    {//begin removeFromDestinations

        destinations.remove(destination);

    }//end removeFromDestinations

}//end Airline class
