package AirportSim;

import java.util.ArrayList;
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

    private double latitude;

    public double getLatitude() {
        return latitude;
    }

    private double longitude;

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

    public Airport(String location, double latitude, double longitude, int numOfGates)
    {

        airportLocation = location;
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

    }

    // Airport copy constructor

    public Airport(Airport anotherAirport)
    {

        this(anotherAirport.airportLocation, anotherAirport.getLatitude(),
                anotherAirport.getLongitude(), anotherAirport.numOfGates);

    }

    public void updateAirport()
    {

        //actual airport operations loop

        //Passenger class object for accessing and updating passengers

        Passenger passengerUtility;

        int paxGenerated = 0;
        int paxBeingGenerated = 300;
        int totalPaxCapacity = 0;

        for (Flight flight : departures)
        {

            totalPaxCapacity += flight.getPlane().getPassengerCapacity();

        }

        for (Flight flight : arrivals)
        {

            totalPaxCapacity += flight.getPlane().getPassengerCapacity();

        }

        for (int min = 0; min < 1440; min++)
        {//begin outer for loop

            if((paxGenerated < totalPaxCapacity) && min < 1000)
            {//begin passenger arrival iterator

                if(totalPaxCapacity - paxGenerated < paxBeingGenerated)
                    paxBeingGenerated = totalPaxCapacity - paxGenerated;

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

        }//end outer for loop

    }

    public void paxArrival(ArrayList<Passenger> paxInAirport, int pax)
    {

        ArrayList<Flight> flightsWithEmptySeats = new ArrayList<>(departures);

        for (int i = 0; i < pax; i++)
        {//begin for loop

            Flight ranFlight;

            do
            {

                ranFlight = flightsWithEmptySeats.get(random.nextInt(flightsWithEmptySeats.size()));

                if (ranFlight.isSoldOut())
                {

                    flightsWithEmptySeats.remove(ranFlight);

                }

            }while(ranFlight.isSoldOut());

            Passenger passenger = new Passenger(ranFlight, (ranFlight.getNumber() +
                    "_" + (ranFlight.getPaxWithTickets().size() + 1)));

            paxInAirport.add(passenger);

            ranFlight.getPaxWithTickets().add(passenger);

        }//end for loop

    }//end paxArrival

    //method for creating flights, and setting the other fields associated with the Flight

    public void genFlightAndInfo(Airline airLine, Airport origin, int minutes, Gate gate)
    {//begin genFlightAndInfo

        Plane plane = new Plane(airLine.getAirlineFleet().get(0).getPassengerCapacity());

        Flight flight = airLine.generateFlight(plane, origin, time.getHour(minutes), time.getMin(minutes), minutes, gate);

        int flightTime = 0;

        int departureTime = flight.getDepartureTime();

        int landingTime = (departureTime + flightTime);

        flight.setLandingTime(landingTime);

        flight.setLandingTimeHour(time.getHour(landingTime));

        flight.setLandingTimeMin(time.getMin(landingTime));

        plane.setCurrentFlight(flight);

        plane.setFlightTimes(flightTime);

        for(int i = departureTime-15; i < departureTime; i++)
        {

            availableGates.get(i).remove(gate);

        }

    }//end genFlightAndInfo

    public String getLocation(){ return airportLocation; }

    public boolean hasOpenGates(int minutes)
    {

        return !availableGates.get(minutes).isEmpty();

    }

}//end Airport class
