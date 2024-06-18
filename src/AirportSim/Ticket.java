package AirportSim;

import java.text.DecimalFormat;

// Originally written by Osama Aljamal

public class Ticket {

    String ticketNum = "";

    private Flight flight;

    public Ticket(Flight flight) {
        ticketNum = formatter();
        this.flight = flight;
    }

    public Ticket(Ticket anotherTicket)
    {

        this(anotherTicket.getFlight());

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

}
