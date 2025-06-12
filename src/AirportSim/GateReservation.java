package AirportSim;

public class GateReservation
{

    private Plane plane;
    private int startTime;
    private int endTime;

    public void setPlane(Plane plane) { this.plane = plane; }

    public void setStartTime(int startTime) { this.startTime = startTime; }

    public void setEndTime(int endTime) { this.endTime = endTime; }

    public GateReservation (Plane plane, int startTime, int endTime)
    {

        setPlane(plane);
        setStartTime(startTime);
        setEndTime(endTime);

    }



}
