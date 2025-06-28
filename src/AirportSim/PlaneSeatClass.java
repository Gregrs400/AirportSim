package AirportSim;

import java.util.ArrayList;

public class PlaneSeatClass
{

    private final String className;
    private ArrayList<String> amenities;
    private final String classCode;

    public PlaneSeatClass(String className, ArrayList<String> amenities, String classCode)
    {

        this.className = className;
        this.amenities = amenities;
        this.classCode = classCode;

    }

    public String getClassCode(){ return classCode; }
}
