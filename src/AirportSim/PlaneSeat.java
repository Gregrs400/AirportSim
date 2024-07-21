package AirportSim;

import java.util.ArrayList;

public class PlaneSeat extends Seat
{

    private Airline airline;
    private Plane plane;
    private PlaneSeatClass seatClass;
    private String planeSeatDescription;


    public Airline getAirline(){ return airline; }
    public void setAirline(Airline airline){ this.airline = airline; }

    public Plane getPlane() { return plane; }
    public void setPlane(Plane plane){ this.plane = plane; }

    public PlaneSeatClass getSeatClass(){ return seatClass; }
    public void setSeatClass(PlaneSeatClass seatClass){ this.seatClass = seatClass; }

    public String getPlaneSeatDescription(){ return planeSeatDescription; }
    public void setPlaneSeatDescription(String planeSeatDescription)
    {
        this.planeSeatDescription = planeSeatDescription;
    }

    public PlaneSeat(String seatID, Airline airline, PlaneSeatClass seatClass)
    {

        super(seatID);
        setAirline(airline);
        setSeatClass(seatClass);

    }

    public PlaneSeat(PlaneSeat anotherPlaneSeat)
    {

        this(anotherPlaneSeat.getSeatID(), anotherPlaneSeat.getAirline(), anotherPlaneSeat.getSeatClass());
        this.setPlane(anotherPlaneSeat.getPlane());
        this.setPlaneSeatDescription(anotherPlaneSeat.getPlaneSeatDescription());

    }

    public PlaneSeat(PlaneSeat seatTemplate, String differentID)
    {

        this(differentID, seatTemplate.getAirline(), seatTemplate.getSeatClass());
        this.setPlane(seatTemplate.getPlane());
        this.setPlaneSeatDescription(seatTemplate.getPlaneSeatDescription());

    }

}
