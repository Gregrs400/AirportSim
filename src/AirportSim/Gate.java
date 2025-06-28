package AirportSim;

import java.util.Deque;
import java.util.LinkedList;
import java.util.ArrayList;

public class Gate
{

    Plane plane;

    private final ArrayList<Passenger> planeConnection;

    public ArrayList<Passenger> getPlaneConnection() { return planeConnection; }

    private String name;

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public Plane getPlane(){ return plane; }

    public void setPlane(Plane plane){ this.plane = plane; }
    public Gate(String name)
    {//begin Gate parameterized constructor

        setName(name);
        planeConnection = new ArrayList<>();

    }//end Gate parameterized constructor

    private final ArrayList<Passenger> paxAtGate = new ArrayList<>();

    public ArrayList<Passenger> getPaxAtGate(){return paxAtGate;}

    public void addPaxToGate(Passenger passenger)
    {

        paxAtGate.add(passenger);

    }

    public String toString()
    {

        return name;

    }

}
