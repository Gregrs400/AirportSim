package AirportSim;

import java.util.ArrayList;

public class PlaneSeat extends Seat
{

    private Airline airline;
    private Plane plane;
    private PlaneSeatClass seatClass;

    public void setAirline(Airline airline){ this.airline = airline; }
    public void setPlane(Plane plane){ this.plane = plane; }
    public void setSeatClass(PlaneSeatClass seatClass){ this.seatClass = seatClass; }

    public PlaneSeat(String seatID, Airline airline, Plane plane, PlaneSeatClass seatClass)
    {

        super(seatID);
        setAirline(airline);
        setPlane(plane);
        setSeatClass(seatClass);

    }

}
