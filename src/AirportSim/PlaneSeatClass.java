package AirportSim;

import java.util.ArrayList;

public class PlaneSeatClass
{

    private String className;
    private ArrayList<String> amenities = new ArrayList<>();

    public PlaneSeatClass(String className, ArrayList<String> amenities)
    {

        this.className = className;
        this.amenities = amenities;

    }
}
