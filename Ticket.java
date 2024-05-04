package FinalProject;

import java.text.DecimalFormat;


public class Ticket {

    String ticketNum = "";

    public Ticket() {
        ticketNum = formatter(ticketNum);
    }

    public String getTicketNum() {
        return ticketNum;
    }

    public void setTicketNum(String ticketNum) {
        formatter(ticketNum);
        this.ticketNum = ticketNum;
    }

    public String formatter(String ticketNum) {

        DecimalFormat df = new DecimalFormat("#");
        df.setMaximumFractionDigits(0);
        ticketNum = "1" + String.valueOf(df.format(Math.random() *1000000000000L));
        return ticketNum;
    }



}
