package AirportSim;

import java.lang.reflect.Array;
import java.util.*;

public class Airline
{//begin Airline class

    private final Random random = new Random();

    private HashMap<Airport, ArrayList<Airport>> destinations = new HashMap<>();

    public void setDestinations(HashMap<Airport, ArrayList<Airport>> destinations) { this.destinations = destinations; }

    //Airport ArrayList of all destinations of an airline

    private final ArrayList<Airport> allDestinations = new ArrayList<>();

    //Integer ArrayList of Flight Numbers of flights created by the airline

    private final ArrayList<Integer> flightNumbers = new ArrayList<>();

    //Flight object ArrayList storing the flights created by the airline

    private final ArrayList<Flight> flights = new ArrayList<>();

    public ArrayList<Flight> getFlights() {return flights;}

    //Plane object ArrayList storing the different plane models in an airline's fleet

    private final ArrayList<Plane> airlineFleet = new ArrayList<>();

    public ArrayList<Plane> getAirlineFleet() {return airlineFleet;}

    //Hashmap allowing retrieval of Flight objects when given flight numbers

    private final ArrayList<Plane> availablePlanes = new ArrayList<>();

    private final HashMap<String, PlaneSeat> planeSeats = new HashMap<>();

    //Airline parameterized constructor

    public Airline(ArrayList<Airport> destinationList) // all destinations available from all airports
    {//begin Airline parameterized constructor

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

    }//end Airline parameterized constructor

    public Airline(HashMap<Airport, ArrayList<Airport>> destinationMap)
    {

        setDestinations(destinationMap);

    }

    //addPlane to add a plane to an airline's fleet

    public void addPlane(Plane plane)
    {//begin addPlane

        airlineFleet.add(plane);
        availablePlanes.add(plane);

    }//end addPlane

    //generateFlight to create a flight for an airline, assigning each flight a plane, a destination, a flight number, and a departure time

    public void generateFlight(Plane plane)
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
        int flightArrivalTime = flight.getArrivalTime();

        reserveGatesForFlight(origin, destination, flight, plane, flightStartTime, flightArrivalTime);

        flights.add(flight);
        plane.addFlightToQueue(flight);
        origin.addToDepartures(flight);
        origin.setTotalPaxCapacity(origin.getTotalPaxCapacity() + flight.getTickets().size());
        destination.addToArrivals(flight);

    }

    public void generateFlights(int day)
    {

        for (Plane plane : airlineFleet)
        {

            do
            {

                generateFlight(plane);

            }while(plane.getLastGeneratedFlight().getEndTime() < ((day+1) * 1440));

            plane.loadNextFlight();

        }

    }

    public Airport getRandomDestination(Airport origin)
    {

        int randomIndex = random.nextInt(destinations.get(origin).size());

        return destinations.get(origin).get(randomIndex);

    }

    public int getPassengerTotal()
    {

        int passengerTotal = 0;

        for(Flight flight : flights)
        {

            Plane flightPlane = flight.getPlane();

            passengerTotal += flightPlane.getPassengers().size();


        }

        return passengerTotal;

    }

    public void removeFromDestinations(Airport destination)
    {//begin removeFromDestinations

        allDestinations.remove(destination);

    }//end removeFromDestinations

    public void addPlaneSeat(String name, PlaneSeat planeSeat)
    {

        planeSeats.put(name, planeSeat);

    }

    public void addFlightToDepartures(Airport origin, Flight flight)
    {

        origin.getDepartures().add(flight);

    }

    public void addFlightToArrivals(Airport destination, Flight flight)
    {

        destination.getArrivals().add(flight);

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

    public void startNextFlight(Plane plane)
    {

        if (!plane.getFlightQueue().isEmpty())
        {

            plane.setCurrentFlight(plane.getFlightQueue().poll());

        }

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

}//end Airline class
