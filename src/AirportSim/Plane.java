package AirportSim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;

public class Plane
{//begin Plane class

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
            gateToGateDuration;

    private Flight currentFlight;

    public Flight getCurrentFlight(){return currentFlight;}

    private PlaneSeat[][] seatLayout;

    public void setCurrentFlight(Flight currentFlight)
    {

        this.currentFlight = currentFlight;

    }

    Queue<Flight> flightQueue = new LinkedList<>();

    public void setFlightTimes(int ascentTime, int cruiseTime, int descentTime)
    {

        ascentDuration = ascentTime;
        cruiseDuration = cruiseTime;
        descentDuration = descentTime;

    }

    enum planeStatus
    {

        AT_DEPART_GATE,
        READY_TO_TAXI,
        TAXIING,
        TAKING_OFF,
        EN_ROUTE,
        DESCENDING,
        // LANDED,
        TAXIING_TO_GATE,
        AT_GATE

    }

    planeStatus ps;

    //Plane parameterized constructor for use by airlines
    public Plane(int capacity)
    {//begin Plane parameterized constructor

        this.passengerCapacity = capacity;
        setPassengers(passengers);
        ps = planeStatus.AT_DEPART_GATE;

    }//end Plane parameterized constructor

    public void addPaxToPlane(Passenger passenger)
    {//begin addPaxToPlane

        passengers.add(passenger);

    }//end addPaxToPlane

    public void addFlightToQueue(Flight flight)
    {

        flightQueue.add(flight);

    }

    public void movePlane()
    {

        if(ps.equals(planeStatus.READY_TO_TAXI))
        {

            ps = planeStatus.TAXIING;

            taxiingToRunwayDuration--;

        }

        if(ps.equals(planeStatus.TAXIING))
        {

            if(taxiingToRunwayDuration == 0)
            {

                ps = planeStatus.TAKING_OFF;

                ascentDuration--;

            }
            else
            {

                taxiingToRunwayDuration--;

            }

        }
        if(ps.equals(planeStatus.TAKING_OFF))
        {

            if(ascentDuration == 0)
            {

                ps = planeStatus.EN_ROUTE;

                cruiseDuration--;

            }
            else
            {

                ascentDuration--;

            }

        }
        if(ps.equals(planeStatus.EN_ROUTE))
        {

            if(cruiseDuration == 0)
            {

                ps = planeStatus.DESCENDING;

                descentDuration--;

            }
            else
            {

                cruiseDuration--;

            }

        }
        if (ps.equals(planeStatus.DESCENDING))
        {

            if(descentDuration == 0)
            {

                ps = planeStatus.TAXIING_TO_GATE;

                taxiingToGateDuration--;

            }
            else
            {

                descentDuration--;

            }

        }
        if(ps.equals(planeStatus.TAXIING_TO_GATE))
        {

            if(taxiingToGateDuration == 0)
            {

                ps = planeStatus.AT_GATE;


            }
            else
            {

                taxiingToGateDuration--;

            }

        }

    }

    public void setSeatLayout(String layoutString)
    {



    }

}//end Plane class
