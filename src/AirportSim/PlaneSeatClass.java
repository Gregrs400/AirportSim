package AirportSim;

import java.util.ArrayList;

public class PlaneSeatClass
{

    private String className;
    private ArrayList<String> amenities = new ArrayList<>();
    private String classCode;

    public PlaneSeatClass(String className, ArrayList<String> amenities, String classCode)
    {

        this.className = className;
        this.amenities = amenities;
        this.classCode = classCode;

    }
}
