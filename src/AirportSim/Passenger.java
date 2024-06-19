package AirportSim;

import java.util.ArrayList;
import java.util.Random;

public class Passenger extends Person
{//begin Passenger class

    private final Random random = new Random();

    //Integer ArrayList storing the weights of passengers' bags

    private ArrayList<Integer> bags = new ArrayList<>();

    //generatePassengerBags to assign a Passenger object bags

    public ArrayList<Integer> generatePassengerBags()
    {//begin getPassengerBags

        ArrayList<Integer> passengerBags = new ArrayList<>();

        //assigning random int between 1 and 3 inclusive, determining the amount of times the inner loop will run, and the number of bags that will be created

        int numOfBags = random.nextInt(3) + 1;

        //inner loop, "j" representing the creation of a passenger's bag, and the element of that bag in the bags ArrayList
        //bagWeight represents the weight of a passenger's bag in pounds, 50 being the upper limit. All bags are between 20 and 50 pounds inclusive at the moment

        for (int i = 0; i < numOfBags; i++)
        {//begin for loop to generate bag weights

            int bagWeight;

            bagWeight = random.nextInt(31)+20;

            passengerBags.add(bagWeight);

        }//end for loop to generate bag weights

        return passengerBags;

    }//end getPassengerBags

    //placeholder int variable for Flight Numbers before they are passed in during passenger construction

    private final int flightNumber;

    public int getFlightNumber() {return flightNumber;}

    private final Airport destination;

    private int curbToCheckIn, checkInToSecurity, securityToGate, gateToPlane;

    private final int airportArrivalTime;

    public int getCurbToCheckIn() {return curbToCheckIn;}

    public int getCheckInToSecurity() {return checkInToSecurity;}

    public int getSecurityToGate() {return securityToGate;}

    public int getGateToPlane() {return gateToPlane;}

    public boolean isAtGate() {return at == airportTravel.AT_GATE;}

    enum airportTravel
    {//begin airportTravel enum

        DROPPED_OFF,
        // CHECKING_IN,
        CHECKED_IN,
        // AT_SECURITY,
        THROUGH_SECURITY,
        // WALKING_TO_GATE,
        AT_GATE,
        // BOARDING_PLANE

    }//end airportTravel enum

    airportTravel at;

    private final Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }

    //Passenger parameterized constructor, assigning each passenger a flight number, an id, and 1-3 bags inclusive

    public Passenger(Flight flight, String id)
    {//begin Passenger parameterized constructor

        this.flightNumber = flight.getNumber();
        this.destination = flight.getDestination();
        setId(id);
        bags = generatePassengerBags();
        commuteThroughAirport();
        ticket = new Ticket(flight);

        curbToCheckIn = getCurbToCheckIn();
        checkInToSecurity = getCheckInToSecurity();
        securityToGate = getSecurityToGate();
        gateToPlane = getGateToPlane();

        at = airportTravel.DROPPED_OFF;

        int arrivalTime = flight.getDepartureTime()-(curbToCheckIn + checkInToSecurity + securityToGate + gateToPlane);
        if (arrivalTime < 0){ arrivalTime = 0; }

        this.airportArrivalTime = arrivalTime;


    }//end Passenger parameterized constructor

    public Passenger(Passenger originalPassenger)
    {

        this(originalPassenger.ticket.getFlight(), originalPassenger.getId());

    }

    //printPassenger to print information about a passenger

    public String toString()
    {//begin Passenger toString

        String paxInfo = "\nPassenger Flight Number: " + flightNumber + "\nPassenger ID: " + getId() +
        "\nTicket Number: " + ticket.ticketNum + "\nPassenger destination: " + destination;

        StringBuilder paxBags = new StringBuilder();

        for(int i = 0; i < bags.size(); i++)
        {//begin for loop to print all passenger's bag weights

            paxBags.append("\nBag ").append(i + 1).append(" Weight: ").append(bags.get(i)).append(" pounds");

        }//end for loop to print all passenger's bag weights

        return paxInfo + paxBags;

    }//end Passenger toString()

    /* commuteThroughAirport generates durations for each stage of
    a passenger getting from the curb to the gate of their airport */

    public void commuteThroughAirport()
    {//begin commuteThroughAirport

        curbToCheckIn = random.nextInt(11)+5;
        checkInToSecurity = random.nextInt(6)+5;
        securityToGate = random.nextInt(31)+15;
        gateToPlane = random.nextInt(11)+5;

    }//end commuteThroughAirport

    /* movePassenger to get passengers to their gate through decrementing
    the above variables and switching enums when each field equals 0. The method
    is called for a passenger while they have not made it to their gate. */

    public void movePassenger()
    {//begin movePassenger

        if(at.equals(airportTravel.DROPPED_OFF))
        {//begin if passenger has been dropped off but not checked in

            curbToCheckIn--;

            if(curbToCheckIn == 0)
            {//begin if passenger has made it to check in

                at = airportTravel.CHECKED_IN;

            }//end if passenger has made it to check in

        }//end if passenger has been dropped off but not checked in

        if(at.equals(airportTravel.CHECKED_IN))
        {//begin if passenger has checked in but not been through security

            checkInToSecurity--;

            if(checkInToSecurity == 0)
            {//begin if passenger gets through security

                at = airportTravel.THROUGH_SECURITY;

            }//end if passenger gets through security

        }//end if passenger has checked in but not been through security

        if(at.equals(airportTravel.THROUGH_SECURITY))
        {//begin if passenger is through security but not gotten to gate

            securityToGate--;

            if(securityToGate == 0)
            {

                at = airportTravel.AT_GATE;

            }

        }//end if passenger is through security but not gotten to gate

    }//end movePassenger

}//end Passenger class
