package AirportSim;

import java.util.ArrayList;
import java.util.Random;

public class Passenger extends Person implements MovingObject
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

    private final Airport destination;

    private int curbToCheckIn, checkInToSecurity, securityToGate, gateToPlane;

    private PlaneSeat reservedSeat;

    public int getCurbToCheckIn() {return curbToCheckIn;}

    public int getCheckInToSecurity() {return checkInToSecurity;}

    public int getSecurityToGate() {return securityToGate;}

    public int getGateToPlane() {return gateToPlane;}

    public boolean isAtGate() {return at == airportTravel.AT_GATE;}

    enum airportTravel
    {//begin airportTravel enum

        DROPPED_OFF,
        CHECKED_IN,
        THROUGH_SECURITY,
        AT_GATE,
        BOARDING_PLANE

    }//end airportTravel enum

    airportTravel at;

    private Ticket ticket;

    public Ticket getTicket() {
        return ticket;
    }

    public void setTicket(Ticket ticket) { this.ticket = ticket;}

    //Passenger parameterized constructor, assigning each passenger a flight number, an id, and 1-3 bags inclusive

    public Passenger(Ticket ticket, String id)
    {//begin Passenger parameterized constructor

        setTicket(ticket);
        Flight flight = ticket.getFlight();
        this.flightNumber = flight.getNumber();
        this.destination = flight.getDestination();
        setId(id);
        bags = generatePassengerBags();
        commuteThroughAirport();
        reservePlaneSeat(ticket.getSeatCode());

        curbToCheckIn = getCurbToCheckIn();
        checkInToSecurity = getCheckInToSecurity();
        securityToGate = getSecurityToGate();
        gateToPlane = getGateToPlane();

        at = airportTravel.DROPPED_OFF;

    }//end Passenger parameterized constructor

    public Passenger(Passenger originalPassenger)
    {

        this(originalPassenger.ticket, originalPassenger.getId());
        this.reservedSeat = originalPassenger.reservedSeat;

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
        gateToPlane = random.nextInt(5)+5;

    }//end commuteThroughAirport

    /* movePassenger to get passengers to their gate through decrementing
    the above variables and switching enums when each field equals 0. The method
    is called for a passenger while they have not made it to their gate. */

    @Override
    public void move()
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
                ticket.getFlight().getDepartureGate().addPaxToGate(this);

            }

        }//end if passenger is through security but not gotten to gate
        if (at.equals(airportTravel.AT_GATE) &&
                ticket.getFlight().getDepartureGate().getPlane() == ticket.getFlight().getPlane())
        {//begin if passenger gets to the gate

            at = airportTravel.BOARDING_PLANE;

        }//end if passenger gets to the gate
        if(at.equals(airportTravel.BOARDING_PLANE))
        {//begin if passenger is boarding

            gateToPlane--;

            if (gateToPlane == 0)
            {

                enterPlane();

            }

        }//end if passenger is boarding

    }//end movePassenger

    public void reservePlaneSeat(String seatCode)  // adding passenger to flight's seating chart
    {

        Flight flight = ticket.getFlight();  // get the passenger's flight
        Airline flightAirline = ticket.getAirline();  // get airline from ticket
        flightAirline.assignPaxToSeat(this, flight, seatCode);  // airline adding passenger to seating chart for flight

    }

    public void boardPlane()
    {

        // passenger is removed from gate and added to planeConnection

        Gate paxGate = ticket.getFlight().getDepartureGate();
        paxGate.getPaxAtGate().remove(this);
        paxGate.getPlaneConnection().add(this);
        at = airportTravel.BOARDING_PLANE;

    }

    public void enterPlane()
    {

        // passenger is removed from planeConnection and added to plane

        ticket.getFlight().getDepartureGate().getPlaneConnection().remove(this);

        // passenger sits in their seat

        int[] paxSeatCoords = ticket.getAirline().fromSeatCodeToCoords(ticket.getSeatCode());

        int rowIndex = paxSeatCoords[0];
        int colIndex = paxSeatCoords[1];

        Plane currentPlane = ticket.getFlight().getPlane();

        reservedSeat = currentPlane.getSeats().get(rowIndex).get(colIndex);

        reservedSeat.occupySeat(this);

        currentPlane.getPassengers().add(this);

    }

    public void deboardPlane()
    {

        // passenger is removed from plane and added to planeConnection

        reservedSeat.setPassenger(null);
        reservedSeat = null;
        Plane paxPlane = ticket.getFlight().getPlane();
        Gate paxGate = ticket.getFlight().getArrivalGate();
        paxPlane.getPassengers().remove(this);
        paxGate.getPlaneConnection().add(this);

    }

}//end Passenger class
