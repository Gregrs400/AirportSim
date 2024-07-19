package AirportSim;

public abstract class Seat
{

    private String seatID;

    private boolean occupied;

    private Passenger passenger;

    public Seat(String seatID)
    {

        this.seatID = seatID;
        vacantSeat();

    }

    public void occupySeat(Passenger passenger)
    {

        occupied = true;
        setPassenger(passenger);

    }

    public void vacantSeat()
    {

        occupied = false;
        setPassenger(null);

    }

    public void setPassenger(Passenger passenger)
    {

        this.passenger = passenger;

    }

}
