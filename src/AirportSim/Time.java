package AirportSim;

public class Time
{

    public int getHour(int minutes)
    {

        if(minutes > 1440)
            minutes -= 1440;

        return minutes / 60;

    }

    public int getMin(int minutes)
    {

        if(minutes > 1440)
            minutes -= 1440;

        return minutes % 60;

    }

}
