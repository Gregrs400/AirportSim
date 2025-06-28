package AirportSim;

import java.text.DecimalFormat;

// Originally written by Osama Aljamal

public class Ticket {

    String ticketNum = "";

    private final Flight flight;

    private String seatCode;

    private final Airline airline;

    public Ticket(Airline airline, Flight flight) {
        ticketNum = formatter();
        this.airline = airline;
        this.flight = flight;
    }

    public String formatter() {

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        return "1" + df.format(Math.random() * 1000000000000L);
    }

    public Flight getFlight()
    {

        return flight;

    }

    public Airline getAirline()
    {

        return airline;

    }

    public String getSeatCode() { return seatCode; }

    public void setSeatCode(String seatCode){ this.seatCode = seatCode; }

}
