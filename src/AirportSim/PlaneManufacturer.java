package AirportSim;

import java.util.ArrayList;
import java.util.HashMap;

// object that is responsible for creating planes for airlines to buy and use

public class PlaneManufacturer
{//begin class PlaneManufacturer

    private String name;
    private final HashMap<String, ArrayList<Plane>> planeInventory =  new HashMap<>();
    private ArrayList<Plane> planes = new ArrayList<>();
    private final HashMap<String, Plane> planeCatalog = new HashMap<>();
    private final Airport location;

    public PlaneManufacturer(Airport location)
    {

        this.location = location;

    }

    // for adding planes to the sim

    public void createPlane(String modelName)
    {//begin method createPlane

        Plane template = planeCatalog.get(modelName);
        Plane plane = new Plane(template);
        plane.setCurrentAirport(location);
        ArrayList<Plane> planeModelInventory = planeInventory.get(modelName);
        planeModelInventory.add(plane);

    }//end method createPlane

    public ArrayList<Plane> getPlanes() { return planes; }

    public Plane getPlaneByModel(String model) // retrieve plane for Airline to copy to their fleet
    {

        return planes.stream()
                .filter(p -> model.equals(p.getModelName()))
                .findFirst()
                .orElse(null);

    }

    public HashMap<String, Plane> getPlaneCatalog() { return planeCatalog; }

    public void createPlaneModel(String modelName, int seatingCapacity)
    {

        Plane plane = new Plane(modelName, seatingCapacity);
        planeCatalog.put(modelName, plane);
        planeInventory.put(modelName, new ArrayList<>());

    }

    // configuring plane that already exists

    public void configurePlane(Plane model, String[] configuration, Airline airline)
    {

        ArrayList<Plane> planeModelInventory = planeInventory.get(model.getModelName());

        if (planeModelInventory.isEmpty())
            createPlane(model.getModelName());

        Plane planeToConfigure = planeModelInventory.getFirst();

        String planeID = configuration[1];
        planeToConfigure.setPlaneID(planeID);

        String seatingLayout = configuration[0];
        addSeatingToPlane(planeToConfigure, seatingLayout, airline);

        deliverPlane(airline, model, planeToConfigure);

    }

    public void deliverPlane(Airline airline, Plane model, Plane plane)
    {

        airline.addPlane(model, plane);
        removePlaneFromCatalog(plane);

    }

    private void removePlaneFromCatalog(Plane plane)
    {

        String planeModel = plane.getModelName();
        Plane model = planeCatalog.get(planeModel);
        ArrayList<Plane> planeModelInventory = planeInventory.get(planeModel);
        planeModelInventory.remove(plane);

    }

    public void addSeatingToPlane(Plane plane, String seatingLayoutString, Airline airline)
    {

        HashMap<String, PlaneSeatClass> planeSeatClassMap = airline.getPlaneSeatClassMap();

        // Row 1: FF
        // Row 2-13: EEEE

        ArrayList<ArrayList<PlaneSeat>> seats;

        seats = new ArrayList<>();

        String[] layoutInstructions = seatingLayoutString.split(",");

//        for (String instruction : layoutInstructions)
//        {
//
//            System.out.println(instruction);
//
//        }
        int rowNum = 1;
        String colString = "";

        for(String layoutInstruction : layoutInstructions)
        {

            int colonIndex = layoutInstruction.indexOf(':');
            String seatCodeString = layoutInstruction.substring(colonIndex+2);

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

                    String currentSeatClassCode = String.valueOf(seatCodeString.charAt(j));

                    int currentColIndex = j;

                    if (currentColIndex > 25)
                    {
                        do {
                            colString += 'Z';
                            currentColIndex = (int) (Math.log(currentColIndex) / Math.log(26));
                        } while (currentColIndex > 25);
                    }

                    colString = String.valueOf(((char) ('A' + currentColIndex)));

                    String seatCode = rowNum+colString;

                    // set plane seat of plane to plane seat type in template; may not be necessary
                    // more realistic for airline to configure seating for each plane after purchase
                    // having the string that is referenced for creating the seat layout is very realistic

                    seats.getLast().add(
                            new PlaneSeat(
                                    plane.getPlaneID()+"_"+rowNum+colString,
                                    airline,
                                    planeSeatClassMap.get(currentSeatClassCode),
                                    seatCode)
                    );

                }

                rowNum++;

            }

        }

        plane.setSeats(seats);

    }

}//end class PlaneManufacturer
