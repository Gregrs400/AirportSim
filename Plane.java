package FinalProject;

import java.util.ArrayList;
import java.util.Random;

public class Plane
{//begin Plane class

    //creating Random object for determining passenger's number of bags and each bag's weight
    private Random random = new Random();

    //declaring int variable to store how many passengers can board plane, defining characteristic of each plane
    private int passengerCapacity;

    public int getPassengerCapacity() {
        return passengerCapacity;
    }

    public void setPassengerCapacity(int passengerCapacity) {
        this.passengerCapacity = passengerCapacity;
    }

    //declaring Passenger type ArrayList for storing the Passenger objects on the plane
    private ArrayList<Passenger> passengers = new ArrayList<>();

    public ArrayList<Passenger> getPassengers() {
        return passengers;
    }

    public void setPassengers(ArrayList<Passenger> passengers) {
        this.passengers = passengers;
    }

    private int seatsTaken;

    public int getSeatsTaken() {
        return seatsTaken;
    }

    public void setSeatsTaken(int seatsTaken) {
        this.seatsTaken = seatsTaken;
    }

    private int gateOccupationTime = 15;

    private int taxiingToRunwayTime, takeingOffTime, enRouteTime, descendingTime, landingTime, taxiingToGateTime;

    private Flight flight;

    public Flight getFlight(){return flight;}

    public void setFlight(Flight flight)
    {

        this.flight = flight;

    }

    private Gate gate;

    public void setGate(Gate gate)
    {

        this.gate = gate;

    }

    private Airline airline;

    public void setAirline(Airline airline)
    {

        this.airline = airline;

    }

    private boolean print = false;

    public void setPrint(boolean print) {
        this.print = print;
    }

    public void setFlightTimes(int flightTime)
    {

        int ascentDescentTime;

        enRouteTime = flightTime;

        if(enRouteTime <= 60)
        {
            int taxiingTime = 1;

            ascentDescentTime = Math.round(enRouteTime / 6);

            taxiingToRunwayTime = taxiingTime;
            enRouteTime = enRouteTime-taxiingToRunwayTime;

            takeingOffTime = ascentDescentTime;
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

            takeingOffTime = ascentDescentTime;
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
        TAKEING_OFF,
        EN_ROUTE,
        DESCENDING,
        LANDING,
        LANDED,
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

    public Plane()
    {//begin Plane no args constructor

    }//end Plane no args constructor

    public void addPaxToPlane(Passenger passenger)
    {//begin addPaxToPlane

        passengers.add(passenger);

    }//end addPaxToPlane

    public boolean isFull()
    {//begin isFull

        if(passengers.size() == passengerCapacity)
        {
            return true;
        }
        else
        {
            return false;
        }

    }//end isFull

    public void movePlane()
    {

        if(ps.equals(planeStatus.READY_TO_TAXI))
        {

            ps = planeStatus.TAXIING;

            if(print == true)
                System.out.println(ps);

            taxiingToRunwayTime--;

        }

        if(gate.gs.equals(Gate.gateStatus.OCCUPIED))
        {

            gateOccupationTime--;

            if(gateOccupationTime == 0)
            {

                gate.gs = Gate.gateStatus.EMPTY;

                gate.nextPlane();

            }


        }

        if(ps.equals(planeStatus.TAXIING))
        {

            if(taxiingToRunwayTime == 0)
            {

                ps = planeStatus.TAKEING_OFF;

                if(print == true)
                    System.out.println(ps);

                takeingOffTime--;

            }
            else
            {

                taxiingToRunwayTime--;

            }

        }
        if(ps.equals(planeStatus.TAKEING_OFF))
        {

            if(takeingOffTime == 0)
            {

                ps = planeStatus.EN_ROUTE;

                if(print == true)
                System.out.println(ps);

                enRouteTime--;

            }
            else
            {

                takeingOffTime--;

            }

        }
        if(ps.equals(planeStatus.EN_ROUTE))
        {

            if(enRouteTime == 0)
            {

                ps = planeStatus.DESCENDING;

                if(print == true)
                    System.out.println(ps);

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

                if(print == true)
                    System.out.println(ps);

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

                ps = planeStatus.LANDED;

                if(print == true)
                    System.out.println(ps);

                ps = planeStatus.TAXIING_TO_GATE;

                if(print == true)
                    System.out.println(ps);

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

                if(print == true)
                    System.out.println(ps);

                if(print == true)
                    System.out.println("Flight " + flight.getNumber() + " made it to the gate.");



            }
            else
            {

                taxiingToGateTime--;

            }

        }

    }


    //fillPlane method which fills plane to passengerCapacity with passengers
        //may be removed after time is implemented

//    public void fillPlane(int flightNumber)
//    {//begin fillPlane
//
//    //outer loop, "i" representing the element of the passenger ArrayList, as well as the id of the passenger
//
//        for(int i = 0; i < passengerCapacity; i++)
//        {//begin outer for loop
//
//            ArrayList<Integer> passengerBags = new ArrayList<>();
//
//            //assigning random int between 1 and 3 inclusive, determining the amount of times the inner loop will run, and the number of bags that will be created
//
//            int jCounter = random.nextInt(3) + 1;
//
//            //inner loop, "j" representing the creation of a passenger's bag, and the element of that bag in the bags ArrayList
//            //bagWeight represents the weight of a passenger's bag in pounds, 50 being the upper limit. All bags are between 20 and 50 pounds inclusive at the moment
//
//            for (int j = 0; j < jCounter; j++)
//            {//begin inner for loop
//
//                int bagWeight = 0;
//
//                bagWeight = random.nextInt(30)+20;
//
//                passengerBags.add(bagWeight);
//
//            }//end inner for loop
//
//            //creating the Passenger objects and adding them to the passengers ArrayList
//
//            Passenger passenger = new Passenger(flightNumber, flightNumber + "_" + (i+1), passengerBags);
//
//            passengers.add(passenger);
//
//        }//end outer for loop
//
//    }//end fillPlane

}//end Plane class
