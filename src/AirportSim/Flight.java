package AirportSim;

import java.util.*;

public class Flight
{//begin Flight class

    //integer variables which store different times
    private int departureTime;

    public int getDepartureTime() {return departureTime;}

    public void setDepartureTime(int departureTime) {this.departureTime = departureTime;}
    private int departureTimeHour;

    public void setDepartureTimeHour(int departureTimeHour) {
        this.departureTimeHour = departureTimeHour;
    }

    private int departureTimeMin;

    public void setDepartureTimeMin(int departureTimeMin) {this.departureTimeMin = departureTimeMin;}

    private int landingTime;

    public void setLandingTime(int landingTime) {this.landingTime = landingTime;}

    private int landingTimeHour;

    public void setLandingTimeHour(int landingTimeHour) {this.landingTimeHour = landingTimeHour;}

    private int landingTimeMin;

    public void setLandingTimeMin(int landingTimeMin) {this.landingTimeMin = landingTimeMin;}

    //int variable number representing the flight number
    private int number;

    public int getNumber() {return number;}

    public void setNumber(int number) {this.number = number;}

    //String object destination representing the destination of the flight

    private Airport destination;

    public Airport getDestination() {return destination;}

    public void setDestination(Airport destination) {this.destination = destination;}

    private Airport originAirport;

    public void setOriginAirport(Airport originAirport) {this.originAirport = originAirport;}

    //Plane object being used for Flight parameterized constructor

    private Plane plane;

    public Plane getPlane() {return plane;}

    public void setPlane(Plane plane) {this.plane = plane;}

    private Gate gate;

    public Gate getGate() {return gate;}

    public void setGate(Gate gate)
    {

        this.gate = gate;

        gate.gs = Gate.gateStatus.OCCUPIED;

    }

    private final flightStatus fs;

    private final ArrayList<Passenger> paxWithTickets = new ArrayList<>();

    public ArrayList<Passenger> getPaxWithTickets() {return paxWithTickets;}

    //Flight parameterized constructor, assigning a plane, a destination, a number, and the departure time of each flight

    public Flight(Plane plane, Airport destination, Airport originAirport, int number)
    {//begin Flight parameterized constructor

        setPlane(plane);
        setDestination(destination);
        setOriginAirport(originAirport);
        setNumber(number);
        fs = flightStatus.ONTIME;

    }//end Flight parameterized constructor

    public Flight(Plane plane, Airport originAirport, Airport destination, int number, int departureTimeHour, int departureTimeMin, int departureTime, Gate gate)
    {//begin Flight parameterized constructor

        setPlane(plane);
        setOriginAirport(originAirport);
        setDestination(destination);
        setNumber(number);
        setDepartureTimeHour(departureTimeHour);
        setDepartureTimeMin(departureTimeMin);
        setDepartureTime(departureTime);
        setGate(gate);
        gate.addPlane(plane);
        fs = flightStatus.ONTIME;

    }//end Flight parameterized constructor

    // copy constructor

    public Flight(Flight anotherFlight)
    {

        this(anotherFlight.plane, anotherFlight.destination, anotherFlight.originAirport,
                anotherFlight.number, anotherFlight.departureTimeHour, anotherFlight.departureTimeMin,
                anotherFlight.departureTime, anotherFlight.gate);
        this.landingTime = anotherFlight.landingTime;
        this.landingTimeHour = anotherFlight.landingTimeHour;
        this.landingTimeMin = anotherFlight.landingTimeMin;
        this.gate = anotherFlight.gate;

    }

    public boolean isSoldOut()
    {//begin isSoldOut

        return paxWithTickets.size() >= plane.getPassengerCapacity();

    }//end isSoldOut


    //flight status enum to assign a flight status depending on the events of the flight

    enum flightStatus
    {//begin flightStatus enum

        // EARLY,
        ONTIME,
        // DELAYED,
        // CANCELLED

    }//end flightStatus enum

    public String toString()
    {

        return ("\nFlight Number: " + number + "\nOrigin: " + originAirport + "\nDestination: " + destination +
                "\n" + formatTime("Departure", departureTimeHour, departureTimeMin, departureTime) +
                "\n" + formatTime("Landing", landingTimeHour, landingTimeMin, landingTime) +
                "\nGate: " + gate.getName() +
                "\nFlight Status: " + fs);

    }
    
    public String formatTime(String status, int hour, int min, int time)
    {//begin printTime

        String suffix, zero;

        if (time > 719 && time < 1440)
            suffix = " PM";
        else
            suffix = " AM";

        if (min < 10)
            zero = "0";
        else
            zero = "";

        if (time < 60)
            hour = 12;

        if (hour > 12)
            hour -= 12;

        return status + " time: " + hour + ":" + zero + min + suffix;

    }//end printTime

}//end flight
