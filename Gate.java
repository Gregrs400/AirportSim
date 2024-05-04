package FinalProject;

import java.util.Deque;
import java.util.LinkedList;
import java.util.ArrayList;

public class Gate
{

    private int seats;

    public int getSeats(){return seats;}

    public void setSeats(int seats) {
        this.seats = seats;
    }

    Deque<Plane> planeQueue = new LinkedList<>();

    public void addPlane(Plane plane)
    {

        planeQueue.add(plane);

        if(gs.equals(gateStatus.EMPTY))
        {

            setPlane(planeQueue.poll());

        }

    }

    public void nextPlane()
    {

        setPlane(planeQueue.poll());

    }

    public Plane getLastPlane(){return planeQueue.peekLast();}

    private String name;

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    private Plane plane;

    public void setPlane(Plane plane)
    {

            this.plane = plane;

    }

    public Gate(int numOfSeats, String name)
    {//begin Gate parameterized constructor

        setSeats(numOfSeats);
        setName(name);

    }//end Gate parameterized constructor

    private ArrayList<Passenger> paxAtGate = new ArrayList<>();

    public ArrayList<Passenger> getPaxAtGate(){return paxAtGate;}

    public void addPaxToGate(Passenger passenger)
    {

        paxAtGate.add(passenger);

    }

    enum gateStatus
    {

        EMPTY,
        OCCUPIED

    }

    gateStatus gs;

}
