package AirportSim;

public class PlaneSeat extends Seat
{

    private Airline airline;
    private Plane plane;
    private PlaneSeatClass seatClass;
    private String planeSeatDescription;
    private String seatCode;


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

    public PlaneSeat(String seatID, Airline airline, PlaneSeatClass seatClass)
    {

        super(seatID);
        setAirline(airline);
        setSeatClass(seatClass);

    }

    public PlaneSeat(PlaneSeat seatTemplate, String differentID, int row, String col)
    {

        this(differentID, seatTemplate.getAirline(), seatTemplate.getSeatClass());
        this.setPlane(seatTemplate.getPlane());
        this.setPlaneSeatDescription(seatTemplate.getPlaneSeatDescription());
        setSeatCode((row) + col);

    }

    public String toString()
    {

        return seatCode;

    }

}
