package AirportSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

public class Airport
{//begin Airport class

    //Random object used to create flights, picking from the destinations ArrayList randomly
        //declared outside main method to be used in any method

    private final Random random = new Random();

    private final Time time = new Time();

    private final ArrayList<Gate> gates = new ArrayList<>();

    private final ArrayList<ArrayList<Gate>> availableGates = new ArrayList<>();

    private final ArrayList<Passenger> paxInAirport = new ArrayList<>();

    private final ArrayList<Passenger> activePax = new ArrayList<>();

    private final int numOfGates;

    private final String airportLocation;

    private final String airportCode;

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    private double longitude;

    private int paxCounter = 0;

    private int paxGenerated = 0;
    private int paxBeingGenerated = 300;
    private int totalPaxCapacity = 0;

    public double getLongitude() {
        return longitude;
    }

    private final ArrayList<Flight> departures = new ArrayList<>();

    public ArrayList<Flight> getDepartures() {
        return departures;
    }

    private final ArrayList<Flight> arrivals = new ArrayList<>();

    public ArrayList<Flight> getArrivals() {
        return arrivals;
    }

    private HashMap<Airline, ArrayList<Flight>> flightsWithVacantSeats;

    public int getTotalPaxCapacity() { return totalPaxCapacity; }

    public void setTotalPaxCapacity(int totalPaxCapacity) { this.totalPaxCapacity = totalPaxCapacity; }

    public Airport(String location, String code, double latitude, double longitude, int numOfGates)
    {

        airportLocation = location;
        airportCode = code;
        this.latitude = latitude;
        this.longitude = longitude;
        this.numOfGates = numOfGates;
        for(int i = 0; i < numOfGates; i++)
        {//begin gate declaration loop

            gates.add(new Gate(50, i+1 + ""));

        }//end gate declaration loop
        for(int i = 0; i < 1440; i++)
        {

            availableGates.add(new ArrayList<>(gates));
            activePax.add(null);

        }

        flightsWithVacantSeats = new HashMap<>();

    }

    // Airport copy constructor

    public Airport(Airport anotherAirport)
    {

        this(anotherAirport.airportLocation, anotherAirport.airportCode, anotherAirport.getLatitude(),
                anotherAirport.getLongitude(), anotherAirport.numOfGates);

    }

    public void updateAirport(int min)
    {

        //actual airport operations loop

        //Passenger class object for accessing and updating passengers

        Passenger passengerUtility;

        if(paxGenerated < totalPaxCapacity)
        {//begin passenger arrival iterator

            int remainingCapacity = totalPaxCapacity - paxGenerated;
            paxBeingGenerated = Math.min(paxBeingGenerated, remainingCapacity);

            paxArrival(paxInAirport, paxBeingGenerated);

            paxGenerated += paxBeingGenerated;

        }//end passenger arrival iterator

        if(!paxInAirport.isEmpty())
        {//begin if passengers are in airport

            for (int paxElement = 0; paxElement < paxInAirport.size(); paxElement++)
            {//begin passenger status updater

                passengerUtility = paxInAirport.get(paxElement);

                Flight paxFlight = passengerUtility.getTicket().getFlight();

                Plane paxPlane = paxFlight.getPlane();

                Gate paxGate = paxFlight.getGate();

                if (!(passengerUtility.isAtGate()))
                {//begin if passenger needs to move

                    passengerUtility.move();

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

    }

    public void paxArrival(ArrayList<Passenger> paxInAirport, int pax)
    {

        for (int i = 0; i < pax; i++)
        {//begin for loop

            Airline ranAirline;
            ArrayList<Airline> airlines = new ArrayList<>(flightsWithVacantSeats.keySet());
            ArrayList<Flight> flights;
            Flight ranFlight;

            do
            {

                Airline[] vacantSeatAirlineArr = flightsWithVacantSeats.keySet().toArray(new Airline[0]);
                ranAirline = vacantSeatAirlineArr[random.nextInt(vacantSeatAirlineArr.length)];
                flights = flightsWithVacantSeats.get(ranAirline);
                ranFlight = flightsWithVacantSeats.get(ranAirline).get(random.nextInt(flights.size()));

                if (ranFlight.isSoldOut())
                {

                    flightsWithVacantSeats.get(ranAirline).remove(ranFlight);

                }
                if (flights.isEmpty())
                {

                    flightsWithVacantSeats.remove(ranAirline);

                }

            }while(ranFlight.isSoldOut());

            Airline airline = ranFlight.getAirline();
            Ticket ranTicket = airline.generateRandomTicket(ranFlight);

            Passenger passenger = new Passenger(ranTicket, "pax_" + this.airportCode + "_" + (paxCounter++));


            paxInAirport.add(passenger);

            ranFlight.getPaxWithTickets().add(passenger);

        }//end for loop

    }//end paxArrival

    public String getLocation(){ return airportLocation; }

    public boolean hasOpenGates(int minutes)
    {

        return !availableGates.get(minutes).isEmpty();

    }

    public void addToDepartures(Flight flight)
    {

        departures.add(flight);

        flightsWithVacantSeats.computeIfAbsent(flight.getAirline(), _ -> new ArrayList<>());
        flightsWithVacantSeats.get(flight.getAirline()).add(flight);

    }

    public void addToArrivals(Flight flight) { arrivals.add(flight); }


}//end Airport class
