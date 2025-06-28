package AirportSim;

public abstract class Seat
{

    private final String seatID;

    private Passenger passenger;

    public Seat(String seatID)
    {

        this.seatID = seatID;
        vacateSeat();

    }

    public void occupySeat(Passenger passenger) { setPassenger(passenger); }

    public void vacateSeat() { setPassenger(null); }

    public void setPassenger(Passenger passenger) { this.passenger = passenger; }

    public String getSeatID(){ return seatID; }

}
