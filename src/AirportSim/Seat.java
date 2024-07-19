package AirportSim;

public abstract class Seat
{

    private String seatID;

    private Passenger passenger;

    public Seat(String seatID)
    {

        this.seatID = seatID;
        vacantSeat();

    }

    public void occupySeat(Passenger passenger)
    {

        setPassenger(passenger);

    }

    public void vacantSeat()
    {

        setPassenger(null);

    }

    public void setPassenger(Passenger passenger)
    {

        this.passenger = passenger;

    }

}
