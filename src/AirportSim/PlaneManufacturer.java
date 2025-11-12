package AirportSim;

import java.util.ArrayList;

// object that is responsible for creating planes for airlines to buy and use

public class PlaneManufacturer
{//begin class PlaneManufacturer

    private String name;
    private String id;
    private ArrayList<Plane> planes;

    // for adding planes to the sim

    public void createPlane(String planeID, int seatingCapacity)
    {//begin method createPlane

        Plane plane = new Plane(planeID, seatingCapacity);
        planes.add(plane);

    }//end method createPlane

    public ArrayList<Plane> getPlanes() { return planes; }

}//end class PlaneManufacturer
