package FinalProject;

import java.util.ArrayList;
import java.util.Random;

public class Passenger extends Person
{//begin Passenger class

    Random random = new Random();

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

    public ArrayList<Integer> getBags() {return bags;}

    public void setBags(ArrayList<Integer> bags) {this.bags = bags;}

    //placeholder int variable for Flight Numbers before they are passed in during passenger construction

    private int flightNumber = 0;

    public int getFlightNumber() {return flightNumber;}

    private String destination;

    public String getDestination() {return destination;}

    private int curbToCheckIn, checkInToSecurity, securityToGate, gateToPlane;

    public int getCurbToCheckIn() {return curbToCheckIn;}

    public int getCheckInToSecurity() {return checkInToSecurity;}

    public int getSecurityToGate() {return securityToGate;}

    public int getGateToPlane() {return gateToPlane;}

    private boolean atGate = false;

    public boolean isAtGate() {return atGate;}

    public void setAtGate(boolean atGate) {this.atGate = atGate;}

    enum airportTravel
    {//begin airportTravel enum

        DROPPED_OFF,
        CHECKING_IN,
        CHECKED_IN,
        AT_SECURITY,
        THROUGH_SECURITY,
        WALKING_TO_GATE,
        AT_GATE,
        BOARDING_PLANE

    }//end airportTravel enum

    airportTravel at;

    private Gate gate;

    private Ticket ticket;


    //Passenger parameterized constructor, assigning each passenger a flight number, an id, and 1-3 bags inclusive

    public Passenger(int flightNumber, String destination, String id, Gate gate)
    {//begin Passenger parameterized constructor

        this.flightNumber = flightNumber;
        this.destination = destination;
        setId(id);
        bags = generatePassengerBags();
        commuteThroughAirport();
        this.gate = gate;
        ticket = new Ticket();

        curbToCheckIn = getCurbToCheckIn();
        checkInToSecurity = getCheckInToSecurity();
        securityToGate = getSecurityToGate();
        gateToPlane = getGateToPlane();

        at = airportTravel.DROPPED_OFF;


    }//end Passenger parameterized constructor

    //Passenger no args constructor to access values of a passenger

    public Passenger()
    {//begin Passenger default no args constructor

    }//end Passenger default no args constructor

    //printPassenger to print information about a passenger

    public void printPassenger()
    {//begin printPassenger

        System.out.println("\nPassenger Flight Number: " + flightNumber);

        System.out.println("Passenger ID: " + getId());

        System.out.println("Ticket Number: " + ticket.ticketNum);

        System.out.println("Passenger destination: " + destination);

        for(int i = 0; i < bags.size(); i++)
        {//begin for loop to print all passenger's bag weights

            System.out.println("Bag " + (i+1) + " Weight: " + bags.get(i) + " pounds");

        }//end for loop to print all passenger's bag weights


    }//end printPassenger

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

        }//end if passenger is through security but not gotten to gate

    }//end movePassenger

    //prints the enum currently set to the Passenger object

    public void printPaxAirportStatus()
    {//begin printPaxAirportStatus

        System.out.println(at);

    }//end printPaxAirportStatus

}//end Passenger class
