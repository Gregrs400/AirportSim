package AirportSim;

import java.util.*;

public class Plane implements MovingObject
{//begin Plane class

    private String planeID;

    private String modelName;

    public String getPlaneID(){ return planeID; }
    public void setPlaneID(String planeID){ this.planeID = planeID; }

    public String getModelName(){ return modelName; }
    //declaring int variable to store how many passengers can board plane, defining characteristic of each plane

    private final int passengerCapacity;

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    //declaring Passenger type ArrayList for storing the Passenger objects on the plane
    private ArrayList<Passenger> passengers = new ArrayList<>();

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    private int taxiingToRunwayDuration, ascentDuration, cruiseDuration, descentDuration, taxiingToGateDuration,
            passengerDeboardingDuration, passengerBoardingDuration;

    private int passengerBoardRate;

    private Airport currentAirport;

    public Airport getCurrentAirport() { return currentAirport; }

    public void setCurrentAirport(Airport currentAirport) { this.currentAirport = currentAirport; }

    private ArrayList<ArrayList<PlaneSeat>> seats;

    Deque<Flight> flightQueue = new LinkedList<>();

    public Deque<Flight> getFlightQueue() {return flightQueue; }

    private Flight currentFlight;

    public Flight getCurrentFlight() {return currentFlight;}

    public void setCurrentFlight(Flight currentFlight)
    {

        this.currentFlight = currentFlight;

    }

    public Flight getLastGeneratedFlight() { return flightQueue.getLast(); }

    @Override
    public void move()
    {

        if(ps.equals(PlaneStatus.TAXIING_TO_RUNWAY))
        {

            if(taxiingToRunwayDuration == 0)
            {

                ps = PlaneStatus.ASCENDING;
                ascentDuration--;

            }
            else
            {
                taxiingToRunwayDuration--;
            }

        }
        if(ps.equals(PlaneStatus.ASCENDING))
        {

            if(ascentDuration == 0)
            {

                ps = PlaneStatus.CRUISING;
                cruiseDuration--;

            }
            else
            {
                ascentDuration--;
            }

        }
        if(ps.equals(PlaneStatus.CRUISING))
        {

            if(cruiseDuration == 0)
            {

                ps = PlaneStatus.DESCENDING;
                descentDuration--;

            }
            else
            {
                cruiseDuration--;
            }

        }
        if (ps.equals(PlaneStatus.DESCENDING))
        {

            if(descentDuration == 0)
            {

                ps = PlaneStatus.TAXIING_TO_GATE;
                setCurrentAirport(currentFlight.getDestination());
                taxiingToGateDuration--;

            }
            else
            {
                descentDuration--;
            }

        }
        if(ps.equals(PlaneStatus.TAXIING_TO_GATE))
        {

            if(taxiingToGateDuration == 0)
            {
                ps = PlaneStatus.AT_ARRIVAL_GATE;
                currentFlight.getArrivalGate().setPlane(this);
            }
            else
            {
                taxiingToGateDuration--;
            }

        }
        if(ps.equals(PlaneStatus.AT_ARRIVAL_GATE))
        {

            if (passengerDeboardingDuration > 0)
            {

                if (!passengers.isEmpty()) {

                    int passengerDeboardRate = Math.ceilDiv(passengers.size(), passengerDeboardingDuration);

                    for (int i = 0; i < passengerDeboardRate; i++) {

                        if (passengers.isEmpty())
                            break;
                        else
                        {

                            Passenger currentPassenger = passengers.getFirst();
                            currentPassenger.deboardPlane();

                        }

                    }

                }

                passengerDeboardingDuration--;
                return;

            }

            // things that need to occur while plane is at gate:
            // baggage unloaded

            if (passengers.isEmpty() && currentFlight.getDestination() == this.getCurrentAirport() &&
                    flightQueue.peek() != null) {

                loadNextFlight();
                ps = PlaneStatus.AT_DEPART_GATE;
                return;

            }
        }
        if (ps.equals(PlaneStatus.AT_DEPART_GATE))
        {

            if (passengerBoardRate == 0)
            {

                passengerBoardRate = 3;
                currentFlight.getDepartureGate().setPlane(this);

            }

            if (passengerBoardingDuration > 0)  // boarding
            {

                if (passengers.size() < getCurrentFlight().getPaxWithTickets().size()) {

                    for (int i = 0; i < passengerBoardRate; i++) {

                        if (getCurrentFlight().getDepartureGate().getPaxAtGate().isEmpty()) {

                            break;

                        } else {

                            Passenger currentPassenger = getCurrentFlight().getDepartureGate().getPaxAtGate().getFirst();
                            currentPassenger.boardPlane();

                        }

                    }

                }

                passengerBoardingDuration--;

            }

            // plane refueled
            // baggage loaded

            if (passengers.size() == getCurrentFlight().getPaxWithTickets().size())
            {

                ps = PlaneStatus.TAXIING_TO_RUNWAY;

            }
        }

    }

    PlaneStatus ps;

    public Plane(int capacity)
    {//begin Plane parameterized constructor

        this.passengerCapacity = capacity;
        setPassengers(new ArrayList<>());

    }//end Plane parameterized constructor

    public Plane(String modelName, int capacity)
    {

        this.modelName = modelName;
        this.passengerCapacity = capacity;

    }

    public Plane(Plane planeTemplate)
    {//begin Plane parameterized constructor

        this(planeTemplate.getPlaneID(), planeTemplate.getPassengerCapacity());
        setPassengers(new ArrayList<>());
        ps = PlaneStatus.AT_DEPART_GATE;

    }//end Plane parameterized constructor

    //Plane parameterized constructor for use by airlines

    public Plane(Plane planeTemplate, String planeID)
    {

        this(planeID, planeTemplate.getPassengerCapacity());
        setPassengers(planeTemplate.getPassengers());
        ps = PlaneStatus.AT_DEPART_GATE;
        setSeats(planeTemplate.getSeats());

    }

    public void addFlightToQueue(Flight flight)
    {

        flightQueue.addLast(flight);

    }

    public ArrayList<ArrayList<PlaneSeat>> getSeats() { return seats; }
    public void setSeats(ArrayList<ArrayList<PlaneSeat>> seats) { this.seats = seats; }

    public void loadNextFlight()
    {

        setCurrentFlight(flightQueue.poll());
        taxiingToRunwayDuration = currentFlight.getTaxiingToRunwayDuration();
        ascentDuration = currentFlight.getAscentDuration();
        cruiseDuration = currentFlight.getCruiseDuration();
        descentDuration = currentFlight.getDescentDuration();
        taxiingToGateDuration = currentFlight.getTaxiingToGateDuration();
        passengerBoardingDuration = currentFlight.getBoardingDuration();
        passengerDeboardingDuration = currentFlight.getDeboardingDuration();
        int startTime = currentFlight.getStartTime();
        // System.out.println("Start time: " + startTime);
        Sim.addToObjectMovementSchedule(startTime, this);

    }

}//end Plane class
