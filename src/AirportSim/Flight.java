package AirportSim;

import java.util.*;

public class Flight
{//begin Flight class

    //integer variables which store different times

    private int startTime, boardingDuration, departureTime, taxiingToRunwayDuration, ascentDuration, cruiseDuration,
            descentDuration, taxiingToGateDuration, arrivalTime, deboardingDuration, endTime, totalDuration;

    public int getDepartureTime() {return departureTime;}

    public void setDepartureTime(int departureTime) {this.departureTime = departureTime;}

    public int getArrivalTime() { return arrivalTime; }

    public void setArrivalTime(int arrivalTime) {this.arrivalTime = arrivalTime;}

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

    public void setPlane(Plane plane)
    {

        this.plane = plane;

    }

    private Airline airline;

    public Airline getAirline() {return airline;}

    public void setAirline(Airline airline) {this.airline = airline; }

    private Gate departureGate;

    public Gate getDepartureGate() {return departureGate;}

    public void setDepartureGate(Gate departureGate)
    {

        this.departureGate = departureGate;

    }

    private Gate arrivalGate;

    public Gate getArrivalGate() {return arrivalGate;}

    public void setArrivalGate(Gate arrivalGate)
    {

        this.arrivalGate = arrivalGate;

    }

    private final flightStatus fs;

    private final ArrayList<Passenger> paxWithTickets = new ArrayList<>();

    public ArrayList<Passenger> getPaxWithTickets() {return paxWithTickets;}

    private Passenger[][] seatingChart;
    public Passenger[][] getSeatingChart() { return seatingChart; }
    private ArrayList<String> unreservedSeats;

    private final ArrayList<Ticket> tickets;

    public ArrayList<Ticket> getTickets() { return tickets; }

    public ArrayList<String> getUnreservedSeats()
    {

        return unreservedSeats;

    }

    public int getStartTime() { return startTime; }

    public void setStartTime(int startTime) {
        this.startTime = startTime;
    }

    public int getBoardingDuration() {
        return boardingDuration;
    }

    public int getDeboardingDuration() {
        return deboardingDuration;
    }

    public int getTaxiingToRunwayDuration() {
        return taxiingToRunwayDuration;
    }

    public int getAscentDuration() {
        return ascentDuration;
    }

    public int getCruiseDuration() {
        return cruiseDuration;
    }

    public int getDescentDuration() {
        return descentDuration;
    }

    public int getTaxiingToGateDuration() {
        return taxiingToGateDuration;
    }

    public void setDeboardingDuration(int deboardingDuration) {
        this.deboardingDuration = deboardingDuration;
    }

    public void setEndTime(int endTime) {
        this.endTime = endTime;
    }

    public int getEndTime() {
        return endTime;
    }

    public int getTotalDuration() { return totalDuration; }

    //Flight parameterized constructor, assigning a plane, a destination, a number, and the departure time of each flight

    public Flight(Plane plane, Airport originAirport, Airport destination, int number)
    {//begin Flight parameterized constructor

        setPlane(plane);
        setOriginAirport(originAirport);
        setDestination(destination);
        setNumber(number);
        generateUnreservedSeats();
        fs = flightStatus.ONTIME;
        tickets = new ArrayList<>(plane.getPassengerCapacity());

    }//end Flight parameterized constructor

    public Flight(Plane plane, Airport originAirport, Airport destination, int number, int departureTime,
                  Gate departureGate, Gate arrivalGate)
    {//begin Flight parameterized constructor

        setPlane(plane);
        setOriginAirport(originAirport);
        setDestination(destination);
        setNumber(number);
        setDepartureTime(departureTime);
        setDepartureGate(departureGate);
        generateUnreservedSeats();
        fs = flightStatus.ONTIME;
        tickets = new ArrayList<>(plane.getPassengerCapacity());

    }//end Flight parameterized constructor

    // copy constructor

    public Flight(Flight anotherFlight)
    {

        this(anotherFlight.plane, anotherFlight.destination, anotherFlight.originAirport,
                anotherFlight.number, anotherFlight.departureTime, anotherFlight.departureGate,
                anotherFlight.arrivalGate);
        this.arrivalTime = anotherFlight.arrivalTime;
        generateUnreservedSeats();

    }

    public boolean isSoldOut()
    {//begin isSoldOut

        return tickets.isEmpty();

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
                "\n" + formatTime("Departure", departureTime) +
                "\n" + formatTime("Landing", arrivalTime) +
                "\nGate: " + departureGate.getName() +
                "\nFlight Status: " + fs);

    }
    
    public String formatTime(String status, int time)
    {//begin printTime

        String suffix, zero;

        int hour = time / 60;
        int min = time % 60;

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

    public void generateUnreservedSeats()
    {

        seatingChart = new Passenger[plane.getSeats().size()][];

        for (int rowNum = 0; rowNum < plane.getSeats().size(); rowNum++)
        {

            seatingChart[rowNum] = new Passenger[plane.getSeats().get(rowNum).size()];

            Arrays.fill(seatingChart[rowNum], null);

        }

        unreservedSeats = new ArrayList<>();

        for (ArrayList<PlaneSeat> row : plane.getSeats())
        {

            for (PlaneSeat col : row)
            {

                unreservedSeats.add(col.getSeatCode());

            }

        }

    }

    public void setFlightTimes(int[] flightTimes)
    {

        taxiingToRunwayDuration = flightTimes[0];
        ascentDuration = flightTimes[1];
        cruiseDuration = flightTimes[2];
        descentDuration = flightTimes[3];
        taxiingToGateDuration = flightTimes[4];
        boardingDuration = flightTimes[5];
        deboardingDuration = flightTimes[6];
        totalDuration = Arrays.stream(flightTimes).reduce(0, Integer::sum);

    }

}//end flight
