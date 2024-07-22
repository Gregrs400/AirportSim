package AirportSim;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;
import java.util.Queue;

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
        generateSeatLayout(seatLayoutStr, seatTemplateMap);

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
        setSeatLayout(planeTemplate.getSeatLayout());

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

    public PlaneSeat[][] getSeatLayout() { return seatLayout; }
    public void setSeatLayout(PlaneSeat[][] seatLayout) { this.seatLayout = seatLayout; }
    public void generateSeatLayout(String layoutString, Map<String, PlaneSeat> seatTemplateMap)
    {
        // Row 1: FF
        // Row 2-13: EEEE

        String[] layoutInstructions = layoutString.split(",");

        String lastLayoutInstruction = layoutInstructions[layoutInstructions.length-1];

        int seatLayoutRowLength;

        if (lastLayoutInstruction.charAt(5) == '-')
        {
            seatLayoutRowLength = Integer.parseInt(lastLayoutInstruction.substring(6, 7));
            seatLayout = new PlaneSeat[seatLayoutRowLength][];
        }
        else
        {
            seatLayoutRowLength = Integer.parseInt(lastLayoutInstruction.substring(4, 5));
            seatLayout = new PlaneSeat[seatLayoutRowLength][];
        }

        for(String layoutInstruction : layoutInstructions)
        {

            int seatNum = 1;
            int startingRow = Integer.parseInt(String.valueOf(layoutInstruction.charAt(4)))-1;
            int colonIndex = layoutInstruction.indexOf(':');
            String seatCodeString = layoutInstruction.substring(colonIndex+1);
            if (layoutInstruction.charAt(5) == '-')
            {

                int endingRow = Integer.parseInt(String.valueOf(layoutInstruction.charAt(6)))-1;
                int instructionNumOfRows = endingRow - startingRow + 1;
                for (int i = startingRow; i < instructionNumOfRows; i++)
                {
                    seatLayout[i] = new PlaneSeat[seatCodeString.length()-1];
                    for (int j = 0; j < seatCodeString.length(); j++)
                    {

                        String currentSeatCode = String.valueOf(seatCodeString.charAt(j));
                        seatLayout[i][j] = new PlaneSeat(seatTemplateMap.get(currentSeatCode), planeID+"_"+seatNum);
                        seatNum++;

                    }

                }

            }
            else
            {

                int seatCodeLength = seatCodeString.length();
                seatLayout[startingRow] = new PlaneSeat[seatCodeLength];

                for (int j = 0; j < seatCodeLength; j++)
                {

                    String currentSeatCode = String.valueOf(seatCodeString.charAt(j));
                    seatLayout[startingRow][j] = new PlaneSeat(seatTemplateMap.get(currentSeatCode));

                }

            }

        }

    }

}//end Plane class
