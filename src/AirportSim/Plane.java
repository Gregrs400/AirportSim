package AirportSim;

import java.util.ArrayList;

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

    private int taxiingToRunwayTime, takingOffTime, enRouteTime, descendingTime, landingTime, taxiingToGateTime;

    private Flight flight;

    public Flight getFlight(){return flight;}

    public void setFlight(Flight flight)
    {

        this.flight = flight;

    }

    public void setFlightTimes(int flightTime)
    {

        int ascentDescentTime;

        enRouteTime = flightTime;

        if(enRouteTime <= 60)
        {
            int taxiingTime = 1;

            ascentDescentTime = Math.round((float) enRouteTime / 6);

            taxiingToRunwayTime = taxiingTime;
            enRouteTime = enRouteTime-taxiingToRunwayTime;

            takingOffTime = ascentDescentTime;
            enRouteTime = enRouteTime-ascentDescentTime;

            descendingTime = ascentDescentTime;
            enRouteTime = enRouteTime-ascentDescentTime;

            landingTime = 3;
            descendingTime = descendingTime-landingTime;

            taxiingToGateTime = taxiingTime;
            enRouteTime = enRouteTime-taxiingToGateTime;

        }

        if(enRouteTime > 60)
        {
            int taxiingTime = 5;

            taxiingToRunwayTime = taxiingTime;
            enRouteTime = enRouteTime-taxiingToRunwayTime;

            ascentDescentTime = 15;

            takingOffTime = ascentDescentTime;
            enRouteTime = enRouteTime-ascentDescentTime;

            descendingTime = ascentDescentTime;
            enRouteTime = enRouteTime-ascentDescentTime;

            landingTime = 5;
            descendingTime = descendingTime-landingTime;

            taxiingToGateTime = taxiingTime;
            enRouteTime = enRouteTime-taxiingToGateTime;

        }

    }

    enum planeStatus
    {

        AT_DEPART_GATE,
        READY_TO_TAXI,
        TAXIING,
        TAKING_OFF,
        EN_ROUTE,
        DESCENDING,
        LANDING,
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

    public void movePlane()
    {

        if(ps.equals(planeStatus.READY_TO_TAXI))
        {

            ps = planeStatus.TAXIING;

            taxiingToRunwayTime--;

        }

        if(ps.equals(planeStatus.TAXIING))
        {

            if(taxiingToRunwayTime == 0)
            {

                ps = planeStatus.TAKING_OFF;

                takingOffTime--;

            }
            else
            {

                taxiingToRunwayTime--;

            }

        }
        if(ps.equals(planeStatus.TAKING_OFF))
        {

            if(takingOffTime == 0)
            {

                ps = planeStatus.EN_ROUTE;

                enRouteTime--;

            }
            else
            {

                takingOffTime--;

            }

        }
        if(ps.equals(planeStatus.EN_ROUTE))
        {

            if(enRouteTime == 0)
            {

                ps = planeStatus.DESCENDING;

                descendingTime--;

            }
            else
            {

                enRouteTime--;

            }

        }
        if (ps.equals(planeStatus.DESCENDING))
        {

            if(descendingTime == 0)
            {

                ps = planeStatus.LANDING;

                landingTime--;

            }
            else
            {

                descendingTime--;

            }

        }
        if(ps.equals(planeStatus.LANDING))
        {

            if(landingTime == 0)
            {

                ps = planeStatus.TAXIING_TO_GATE;

                taxiingToGateTime--;

            }
            else
            {

                landingTime--;

            }

        }
        if(ps.equals(planeStatus.TAXIING_TO_GATE))
        {

            if(taxiingToGateTime == 0)
            {

                ps = planeStatus.AT_GATE;


            }
            else
            {

                taxiingToGateTime--;

            }

        }

    }

}//end Plane class
