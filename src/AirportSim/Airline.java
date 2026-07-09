package AirportSim;

import java.util.*;

public class Airline
{//begin Airline class

    private String name;

    private String airlineAbbreviation;

    private final Random random = new Random();

    private final HashMap<Airport, ArrayList<Airport>> destinations = new HashMap<>();

    //Airport ArrayList of all destinations of an airline

    private final ArrayList<Airport> allDestinations = new ArrayList<>();

    //Integer ArrayList of Flight Numbers of flights created by the airline

    private final ArrayList<Integer> flightNumbers = new ArrayList<>();

    //Flight object ArrayList storing the flights created by the airline

    private final ArrayList<Flight> flights = new ArrayList<>();

    public ArrayList<Flight> getFlights() {return flights;}

    //Plane object ArrayList storing the different plane models in an airline's fleet

    private final HashMap<Plane, ArrayList<Plane>> fleet = new HashMap<>();

    private final HashMap<String, PlaneSeatClass> planeSeatClassMap = new HashMap<>();

    public HashMap<String, PlaneSeatClass> getPlaneSeatClassMap() { return planeSeatClassMap; }

    public HashMap<Plane, String[]> planeConfigurations = new HashMap<>();

    //Airline parameterized constructor

    public Airline(String name, String abbreviation, ArrayList<Airport> destinationList, String seatClassesString) // all destinations available from all airports
    {//begin Airline parameterized constructor

        this.name = name;

        this.airlineAbbreviation = abbreviation;

        for (Airport origin : destinationList)
        {

            ArrayList<Airport> originAirportDestinations = new ArrayList<>();

            for (Airport destination : destinationList)
            {

                if (destination != origin)
                {

                    originAirportDestinations.add(destination);

                }

            }

            destinations.put(origin, originAirportDestinations);

        }

        allDestinations.addAll(destinationList);

        decodeSeatClasses(seatClassesString);

    }//end Airline parameterized constructor

    //addPlane to add a plane to an airline's fleet

    public void addPlane(Plane model, Plane plane)
    {//begin addPlane

        fleet.get(model).add(plane);

    }//end addPlane

    //generateFlight to create a flight for an airline, assigning each flight a plane, a destination, a flight number, and a departure time

    public Flight generateFlight(Plane plane)
    {

        Airport origin, destination;
        int originIndex;

        if (!plane.getFlightQueue().isEmpty()) { origin = plane.getLastGeneratedFlight().getDestination(); }
        else
        {
            originIndex = random.nextInt(allDestinations.size());
            origin = allDestinations.get(originIndex);
        }
        int destIndex = random.nextInt(destinations.get(origin).size());
        destination = destinations.get(origin).get(destIndex);

        if (destination == origin)
        {

            while (destination == origin)
            {

                destIndex = random.nextInt(destinations.get(origin).size()-1);
                destination = destinations.get(origin).get(destIndex);

            }

        }

        Flight flight = new Flight(plane, origin, destination, generateFlightNumber());
        flight.setAirline(this);

        ArrayList<Ticket> flightTickets = flight.getTickets();

        ArrayList<String> seatCodes = new ArrayList<>();

        ArrayList<ArrayList<PlaneSeat>> planeSeats = flight.getPlane().getSeats();

        for (ArrayList<PlaneSeat> planeSeat : planeSeats) {
            for (PlaneSeat seat : planeSeat) {

                seatCodes.add(seat.getSeatCode());

            }

        }

        for (int i = 0; i < plane.getPassengerCapacity(); i++)
        {

            Ticket ticket = new Ticket(this, flight);
            ticket.setSeatCode(seatCodes.get(i));
            flightTickets.add(i, ticket);

        }

        flight.setFlightTimes(generateFlightTimes(380, origin, destination));

        int flightStartTime, flightEndTime;

        if (!plane.getFlightQueue().isEmpty())
        {

            flightStartTime = plane.getLastGeneratedFlight().getEndTime();

        }
        else
        {
            flightStartTime = 240;
        }

        flight.setStartTime(flightStartTime);
        flightEndTime = flightStartTime + flight.getTotalDuration();
        flight.setEndTime(flightEndTime);
        flight.setDepartureTime(flightStartTime + flight.getBoardingDuration());
        flight.setArrivalTime(flight.getEndTime() - flight.getDeboardingDuration());

        return flight;

    }

    public Flight generateFlight(Plane plane, Airport origin)
    {

        Airport destination;

        int destIndex = random.nextInt(destinations.get(origin).size());
        destination = destinations.get(origin).get(destIndex);

        if (destination == origin)
        {

            while (destination == origin)
            {

                destIndex = random.nextInt(destinations.get(origin).size()-1);
                destination = destinations.get(origin).get(destIndex);

            }

        }

        Flight flight = new Flight(plane, origin, destination, generateFlightNumber());
        flight.setAirline(this);

        ArrayList<Ticket> flightTickets = flight.getTickets();

        ArrayList<String> seatCodes = new ArrayList<>();

        ArrayList<ArrayList<PlaneSeat>> planeSeats = flight.getPlane().getSeats();

        for (ArrayList<PlaneSeat> planeSeat : planeSeats) {
            for (PlaneSeat seat : planeSeat) {

                seatCodes.add(seat.getSeatCode());

            }

        }

        for (int i = 0; i < plane.getPassengerCapacity(); i++)
        {

            Ticket ticket = new Ticket(this, flight);
            ticket.setSeatCode(seatCodes.get(i));
            flightTickets.add(i, ticket);

        }

        flight.setFlightTimes(generateFlightTimes(380, origin, destination));

        int flightStartTime, flightEndTime;

        if (!plane.getFlightQueue().isEmpty())
        {

            flightStartTime = plane.getLastGeneratedFlight().getEndTime();

        }
        else
        {
            flightStartTime = 240;
        }

        flight.setStartTime(flightStartTime);
        flightEndTime = flightStartTime + flight.getTotalDuration();
        flight.setEndTime(flightEndTime);
        flight.setDepartureTime(flightStartTime + flight.getBoardingDuration());
        flight.setArrivalTime(flight.getEndTime() - flight.getDeboardingDuration());

        return flight;

    }

    public void addFlightToPlane(Flight flight, Plane plane)
    {

        int flightArrivalTime = flight.getArrivalTime();
        int flightStartTime = flight.getStartTime();
        Airport origin = flight.getOriginAirport();
        Airport destination = flight.getDestination();

        reserveGatesForFlight(origin, destination, flight, plane, flightStartTime, flightArrivalTime);

        flights.add(flight);
        plane.addFlightToQueue(flight);
        origin.addToDepartures(flight);
        origin.setTotalPaxCapacity(origin.getTotalPaxCapacity() + flight.getTickets().size());
        destination.addToArrivals(flight);

    }

    public void generateFlights(int day) {

        for (Plane model : fleet.keySet())
        {

            for (Plane plane : fleet.get(model)) {

                Flight flight = null;

                do {

                    if (flight != null) {
                        addFlightToPlane(flight, plane);
                    }
                    flight = generateFlight(plane);

                } while (flight.getEndTime() < (((day + 1) * 1440)));

                plane.loadNextFlight();

            }
        }
    }

    public int generateFlightNumber()
    {

        int flightNumber;

        do
        {//begin do-while loop

            flightNumber = random.nextInt(9999)+1;

        }while(flightNumbers.contains(flightNumber)); //end do-while loop

        flightNumbers.add(flightNumber);

        return flightNumber;

    }

    public int[] generateFlightTimes(int filedSpeed, Airport origin, Airport destination)
    {

        double earthRadiusMiles;
        double earthRadiusNM;
        double earthArcAngle;
        double earthGreatCircleDistance;
        int cruiseAltitude;
        int ascentVerticalSpeed;
        int descentVerticalSpeed;
        double ascentAcceleration;
        double descentAcceleration;
        double ascentDistance;
        double cruiseDistance;
        double descentDistance;
        double ascentTime;
        int cruiseTime;
        double descentTime;
        double takeoffSpeed = 150;

        earthRadiusMiles = 3959;
        earthRadiusNM = milesToNauticalMiles(earthRadiusMiles);

        earthArcAngle = Math.acos((Math.sin(origin.getLatitude() * Math.PI / 180)
                                  * Math.sin(destination.getLatitude() * Math.PI / 180))
                                  + (Math.cos(origin.getLatitude() * Math.PI / 180)
                                  * Math.cos(destination.getLatitude() * Math.PI / 180)
                                  * Math.cos((origin.getLongitude() - destination.getLongitude()) * Math.PI / 180)));

        earthGreatCircleDistance = earthArcAngle * earthRadiusNM;

        if (earthGreatCircleDistance < 150)
        {
            cruiseAltitude = 10000;
        }
        else if (earthGreatCircleDistance >= 150 && earthGreatCircleDistance < 250)
        {
            cruiseAltitude = 16000;
        }
        else
            cruiseAltitude = 32000;

        if (cruiseAltitude == 10000)
        {
            ascentVerticalSpeed = cruiseAltitude / 3;
            descentVerticalSpeed = cruiseAltitude / -10;
        }
        else
        {

            ascentVerticalSpeed = cruiseAltitude / 10;
            descentVerticalSpeed = cruiseAltitude / -20;

        }

        ascentTime =  (double) cruiseAltitude / ascentVerticalSpeed / 60;
        descentTime =  (double) -cruiseAltitude / descentVerticalSpeed / 60;

        double takeoffSpeedY = ascentVerticalSpeed * 0.00987473;
        double takeoffSpeedX = takeoffSpeed - takeoffSpeedY;

        ascentAcceleration = (filedSpeed - takeoffSpeed) / ascentTime / 60;
        descentAcceleration = (filedSpeed - takeoffSpeed) / descentTime / 60;

        ascentDistance = (takeoffSpeedX * ascentTime) +
                         (((double) 1 / 2) * ascentAcceleration * Math.pow(ascentTime, 2));

        descentDistance = (filedSpeed * descentTime) +
                          (((double) 1 / 2) * descentAcceleration * Math.pow(descentTime, 2));

        cruiseDistance = earthGreatCircleDistance - (ascentDistance + descentDistance);

        cruiseTime = (int) ((cruiseDistance / filedSpeed) * 60);

        int ascentTimeMin = (int) (ascentTime*60);
        int descentTimeMin = (int) (descentTime*60);

        int taxiingToRunwayDuration = random.nextInt(15)+1;
        int taxiingToGateDuration = random.nextInt(15)+1;
        int boardingDuration = random.nextInt(30)+45;
        int deboardingDuration = random.nextInt(45)+30;

        return new int[]{taxiingToRunwayDuration, ascentTimeMin, cruiseTime, descentTimeMin, taxiingToGateDuration,
                        boardingDuration, deboardingDuration};

    }

    private double milesToNauticalMiles(double miles){ return miles * 0.86897624; }

    public void assignPaxToSeat(Passenger passenger, Flight flight, String seatCode)
    {

        int[] seatCoords = fromSeatCodeToCoords(seatCode);

        int rowNum = seatCoords[0];
        int colIndex = seatCoords[1];

        Passenger[][] flightSeatingChart = flight.getSeatingChart();

        flightSeatingChart[rowNum][colIndex] = passenger;  // setting passenger to seat on seating chart
        flight.getUnreservedSeats().remove(seatCode);  // remove seat code so no other passengers can reserve
        passenger.getTicket().setSeatCode(seatCode);  // add seat code to ticket


    }

    public Ticket generateRandomTicket(Flight flight)
    {

        ArrayList<Ticket> tickets = flight.getTickets();
        Ticket ranTicket = tickets.get(random.nextInt(tickets.size()));
        tickets.remove(ranTicket);
        return ranTicket;

    }

    public int[] fromSeatCodeToCoords(String seatCode)
    {

        // 4A

        StringBuilder rowNumStr = new StringBuilder();  // 4
        StringBuilder colNumStr = new StringBuilder();  // A

        char[] seatNumAsArray = seatCode.toCharArray();

        for (char seatNumChar : seatNumAsArray)
        {

            if (seatNumChar > 47 && seatNumChar < 58)
            {

                rowNumStr.append(seatNumChar);

            }
            else
            {

                colNumStr.append(seatNumChar);

            }

        }

        int rowNum = Integer.parseInt(String.valueOf(rowNumStr))-1;
        int colIndex = 0;
        int colNumStrLength = colNumStr.toString().length();
        for (int i = 0; i < colNumStrLength; i++)
        {

            colIndex += (colNumStr.charAt(i)-65) * (int) Math.pow(26, colNumStrLength-(i+1));

        }

        int[] seatCoords = new int[2];

        seatCoords[0] = rowNum;
        seatCoords[1] = colIndex;

        return seatCoords;

    }

    public Gate findOpenGate(Airport airport, int startTime, int endTime)
    {

        HashMap<Gate, ArrayList<GateReservation>> gateReservations = airport.getGateReservations();
        ArrayList<Gate> airportGates = airport.getGates();
        int minGateReservations = 1000;
        int maxGateReservations = 0;

        for (int i = 0; i < gateReservations.size(); i++)
        {

            Gate currentGate = airportGates.get(i);
            ArrayList<GateReservation> currentGateReservations = gateReservations.get(currentGate);
            int currentGateReservationsSize = currentGateReservations.size();

            minGateReservations = Math.min(currentGateReservationsSize, minGateReservations);
            maxGateReservations = Math.max(currentGateReservationsSize, maxGateReservations);

        }

        for (int i = 0; i < maxGateReservations+1; i++)
        {

            if (i < minGateReservations)
                continue;

            for (int j = 0; j < gateReservations.size(); j++)
            {

                Gate currentGate = airportGates.get(j);
                ArrayList<GateReservation> currentGateReservations = gateReservations.get(currentGate);
                int currentGateReservationsSize = currentGateReservations.size();

                if (currentGateReservationsSize > i)
                    continue;

                if (currentGateReservationsSize > 1)
                {

                    for (int k = 1; k < currentGateReservationsSize; k++)
                    {
                        
                        GateReservation currentReservation = currentGateReservations.get(k);
                        GateReservation previousReservation = currentGateReservations.get(k - 1);

                        if (previousReservation.getStartTime() > endTime)
                            return currentGate;
                        else if (previousReservation.getEndTime() < startTime)
                            if (currentReservation.getStartTime() > endTime)
                                return currentGate;
                    }

                }
                else
                {

                    if (currentGateReservations.isEmpty())
                        return currentGate;
                    else
                    {

                        GateReservation onlyReservation = currentGateReservations.getFirst();

                        if (onlyReservation.getStartTime() > endTime)
                            return currentGate;
                        else if (onlyReservation.getEndTime() < startTime)
                            return currentGate;

                    }

                }

            }

        }
        return null;

    }

    public Gate reserveGate(Airport airport, Plane plane, int startTime, int endTime)
    {

        Gate openGate = findOpenGate(airport, startTime, endTime);
        ArrayList<GateReservation> openGateReservations = airport.getGateReservations().get(openGate);

        if (openGate != null)
        {

            if (openGateReservations.isEmpty())
            {
                GateReservation newReservation = new GateReservation(plane, startTime, endTime);
                openGateReservations.add(newReservation);
                return openGate;
            }
            else
            {
                for (int i = 0; i < openGateReservations.size(); i++) {

                    GateReservation currentReservation = openGateReservations.get(i);

                    if (currentReservation.getStartTime() > startTime) {

                        GateReservation newReservation = new GateReservation(plane, startTime, endTime);
                        openGateReservations.add(i, newReservation);
                        return openGate;

                    }

                }
            }

        }

        return null;

    }

    public void reserveGatesForFlight(Airport origin, Airport destination, Flight flight, Plane plane, int startTime,
                                      int arrivalTime)
    {

        Gate departGate;
        Deque<Flight> planeFlightQueue = plane.getFlightQueue();
        boolean flightQueueIsEmpty = planeFlightQueue.isEmpty();
        int flightStartTime = flight.getStartTime();

        if (flightQueueIsEmpty)
            departGate = reserveGate(origin, plane, flightStartTime, flightStartTime+60);
        else
            departGate = plane.getLastGeneratedFlight().getArrivalGate(); // need times to be start time and end of boarding time
        //previous flight -> depart gate is already reserved. just need to reserve arrival gate for full turnaround time
        //no previous flight -> reserve depart gate for depart turnaround time, reserve arrival gate for full turnaround time

        Gate arrivalGate = reserveGate(destination, plane, arrivalTime, arrivalTime+120); // // need times to be start of deboarding time and end time

        while (departGate == null || arrivalGate == null)
        {

            startTime += 15;
            arrivalTime += 15;
            int flightEndTime = flight.getEndTime();
            flight.setEndTime(flightEndTime+15);

            if (flightQueueIsEmpty)
                departGate = reserveGate(origin, plane, startTime, startTime+60);
            arrivalGate = reserveGate(destination, plane, arrivalTime, startTime+120);

        }

        flight.setDepartureGate(departGate);
        flight.setArrivalGate(arrivalGate);
        flight.setStartTime(startTime);
        flight.setArrivalTime(arrivalTime);

    }

    public void createSeatClass(String className, String amenities, String classCode)
    {

        String[] amenitiesArr = amenities.split(",");
        PlaneSeatClass seatClass = new PlaneSeatClass(className,
                new ArrayList<>(Arrays.asList(amenitiesArr)), classCode);
        planeSeatClassMap.put(className, seatClass);
    }

    public void createPlaneConfiguration(String seatingLayoutString, Plane model)
    {

        String[] configuration = new String[2];

        configuration[0] = seatingLayoutString;
        configuration[1] = "";

        planeConfigurations.put(model, configuration);

    }

    private void decodeSeatClasses(String seatClassesString)
    {

        String[] seatClasses = seatClassesString.split(";");

        for (String seatClass : seatClasses)
        {

            int indexOfOpenParen = seatClass.indexOf('(');
            int indexOfCloseParen = seatClass.indexOf(')');
            String className = seatClass.substring(0, indexOfOpenParen-2);
            String amenities = seatClass.substring(indexOfCloseParen, seatClass.length()-1);
            String classCode = seatClass.substring(indexOfOpenParen+1, indexOfCloseParen);

            createSeatClass(className, amenities, classCode);

        }


        //"Economy (E): Seat
        // First (F): Lie-flat Seat,Pillows,Blanket"

        //public void createSeatClass(String className, String amenities, String classCode)

    }

    public String generatePlaneID(Plane model)
    {

        String planeID = "";

        fleet.computeIfAbsent(model, k -> new ArrayList<>());
        int currentCountOfModel = fleet.get(model).size();

        planeID = airlineAbbreviation + "_"
                + model.getModelName() + "_"
                + (currentCountOfModel+1);

        return planeID;

    }

    public void requestPlane(String modelName, PlaneManufacturer manufacturer)
    {

        String[] currentConfiguration = new String[2];

        Plane desiredModel = manufacturer.getPlaneCatalog().get(modelName);

        currentConfiguration[0] = planeConfigurations.get(desiredModel)[0];

        fleet.computeIfAbsent(desiredModel, k -> new ArrayList<>());
        currentConfiguration[1] = generatePlaneID(desiredModel);
        manufacturer.configurePlane(desiredModel, currentConfiguration, this);

        currentConfiguration[0] = "";
        currentConfiguration[1] = "";

    }

}//end Airline class
