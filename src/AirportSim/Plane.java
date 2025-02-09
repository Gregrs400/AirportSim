package AirportSim;

import java.util.*;

public class Plane
{//begin Plane class

    private String planeID;

    public String getPlaneID(){ return planeID; }
    public void setPlaneID(String planeID){ this.planeID = planeID; }

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
            passengerDeboardingDuration, baggageUnloadingDuration, planeRefuelingDuration, baggageLoadingDuration,
            passengerBoardingDuration, atGateDuration;

    private Airport currentAirport;

    public Airport getCurrentAirport() { return currentAirport; }

    public void setCurrentAirport(Airport currentAirport) { this.currentAirport = currentAirport; }

    private ArrayList<ArrayList<PlaneSeat>> seats;

    Queue<Flight> flightQueue = new LinkedList<>();

    private Flight currentFlight;

    public Flight getCurrentFlight(){return currentFlight;}

    public void setCurrentFlight(Flight currentFlight)
    {

        this.currentFlight = currentFlight;

    }

    public void setFlightTimes(int ascentTime, int cruiseTime, int descentTime)
    {

        ascentDuration = ascentTime;
        cruiseDuration = cruiseTime;
        descentDuration = descentTime;

    }

    public Flight getLatestFlight() { return flightQueue.peek(); }

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

    public planeStatus getPlaneStatus(){ return ps; }

    public Plane(int capacity)
    {//begin Plane parameterized constructor

        this.passengerCapacity = capacity;
        setPassengers(new ArrayList<>());

    }//end Plane parameterized constructor


    public Plane(Plane planeTemplate, String seatLayoutStr, Map<String, PlaneSeat> seatTemplateMap)
    {//begin Plane parameterized constructor

        this(planeTemplate.getPlaneID(), planeTemplate.getPassengerCapacity());
        setPassengers(new ArrayList<>());
        ps = planeStatus.AT_DEPART_GATE;
        generateSeats(seatLayoutStr, seatTemplateMap);

    }//end Plane parameterized constructor

    //Plane parameterized constructor for use by airlines
    public Plane(String id, int capacity)
    {//begin Plane parameterized constructor

        setPlaneID(id);
        this.passengerCapacity = capacity;
        setPassengers(new ArrayList<>());
        ps = planeStatus.AT_DEPART_GATE;

    }//end Plane parameterized constructor

    // Plane copy constructor

    public Plane(Plane anotherPlane)
    {

        this(anotherPlane.getPlaneID(), anotherPlane.getPassengerCapacity());
        setPassengers(anotherPlane.getPassengers());
        ps = anotherPlane.getPlaneStatus();

    }

    public Plane(Plane planeTemplate, String planeID)
    {

        this(planeID, planeTemplate.getPassengerCapacity());
        setPassengers(planeTemplate.getPassengers());
        ps = planeStatus.AT_DEPART_GATE;
        setSeats(planeTemplate.getSeats());

    }

    public void generateCommuteTimes()
    {



    }
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
                setCurrentAirport(currentFlight.getDestination());
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
        if(ps.equals(planeStatus.AT_GATE))
        {

            if (passengers.size() > 0)
            {

                int counter = 0;
                while (counter < 3 && !passengers.isEmpty())
                {

                    // passenger get up from seat
                    // passenger get off plane and into gate

                }

            }

            // things that need to occur while plane is at gate:
            // passengers deboarded  (passengerCapacity / 3)
            // baggage unloaded

            if (passengers.isEmpty() && currentFlight.getDestination() == this.getCurrentAirport())
            {
                generateCommuteTimes();
                flightQueue.poll();
                setCurrentFlight(flightQueue.peek());
            }
            // plane refueled
            // baggage loaded
            // passengers boarded

        }

    }

    public ArrayList<ArrayList<PlaneSeat>> getSeats() { return seats; }
    public void setSeats(ArrayList<ArrayList<PlaneSeat>> seats) { this.seats = seats; }
    public void generateSeats(String layoutString, Map<String, PlaneSeat> seatTemplateMap)
    {
        // Row 1: FF
        // Row 2-13: EEEE

        seats = new ArrayList<>();

        String[] layoutInstructions = layoutString.split(",");

        for (String instruction : layoutInstructions)
        {

            System.out.println(instruction);

        }
        int rowNum = 1;
        String colString = "";
        String seatCode;

        for(String layoutInstruction : layoutInstructions)
        {

            int colonIndex = layoutInstruction.indexOf(':');
            String seatCodeString = layoutInstruction.substring(colonIndex+2);

            System.out.println(seatCodeString.length());

            int startingRow;
            int endingRow;
            int instructionNumOfRows;
            if (layoutInstruction.contains("-"))
            {

                startingRow = Integer.parseInt(layoutInstruction.substring(4, layoutInstruction.indexOf('-')));
                endingRow = Integer.parseInt(layoutInstruction.substring(layoutInstruction.indexOf('-') + 1, layoutInstruction.indexOf(':')));
                instructionNumOfRows = endingRow - startingRow + 1;

            }
            else
            {

                instructionNumOfRows = 1;

            }
            for (int i = 0; i < instructionNumOfRows; i++)
            {

                seats.add(new ArrayList<>());

                for (int j = 0; j < seatCodeString.length(); j++)
                {

                    String currentSeatCode = String.valueOf(seatCodeString.charAt(j));

                    int currentColIndex = j;

                    if (currentColIndex > 25)
                    {
                        do {
                            colString += 'Z';
                            currentColIndex = (int) (Math.log(currentColIndex) / Math.log(26));
                        } while (currentColIndex > 25);
                    }

                    colString = String.valueOf(((char) ('A' + currentColIndex)));

                    seats.getLast().add(new PlaneSeat(seatTemplateMap.get(currentSeatCode), planeID+"_"+rowNum+colString, rowNum, colString));

                }

                rowNum++;

            }

        }

    }

}//end Plane class
