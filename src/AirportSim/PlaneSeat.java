package AirportSim;

public class PlaneSeat extends Seat
{

    private Airline airline;
    private Plane plane;
    private PlaneSeatClass seatClass;
    private String planeSeatDescription;
    private String seatCode;
    private String col;


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

    public String getSeatCode(){ return seatCode; }
    public void setSeatCode(String seatCode){ this.seatCode = seatCode; }

    public void setCol(String col){ this.col = col; }

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

    public PlaneSeat(PlaneSeat seatTemplate, String differentID, int row, String col)
    {

        this(differentID, seatTemplate.getAirline(), seatTemplate.getSeatClass());
        this.setPlane(seatTemplate.getPlane());
        this.setPlaneSeatDescription(seatTemplate.getPlaneSeatDescription());
        setCol(col);
        setSeatCode(String.valueOf(row) + col);

    }

    public String toString()
    {

        return seatCode;

    }

}
