package AirportSim;

import java.text.DecimalFormat;

// Originally written by Osama Aljamal

public class Ticket {

    String ticketNum = "";

    public Ticket() {
        ticketNum = formatter();
    }

    public String formatter() {

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        return "1" + df.format(Math.random() * 1000000000000L);
    }

}
