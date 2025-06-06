package AirportSim;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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


    public Flight generateFlight()
    {

        Airport origin, destination;

        int flightNumber;

        //do-while loop to ensure each flight number is unique

        do
        {//begin do-while loop

            flightNumber = random.nextInt(9999)+1;

        }while(flightNumbers.contains(flightNumber)); //end do-while loop

        flightNumbers.add(flightNumber);

        Plane plane = availablePlanes.getFirst();
        availablePlanes.removeFirst();

        int originIndex = random.nextInt(allDestinations.size());

        int destinationIndex = random.nextInt(allDestinations.size());

        origin = allDestinations.get(originIndex);

        if (destinationIndex == originIndex)
        {
            while (destinationIndex == originIndex) {
                destinationIndex = random.nextInt(allDestinations.size());
            }
        }

        destination = allDestinations.get(destinationIndex);

        Flight flight = new Flight(plane, destination, origin, flightNumber);

        flights.add(flight);

        addFlightToDepartures(origin, flight);
        addFlightToArrivals(destination, flight);

        return flight;

    }

    public Flight generateFlight(Plane plane, Airport origin, int departHour, int departMin, int departTime, Gate gate)
    {//begin generateFlight

        Airport destination;

        //do-while loop to ensure each flight number is unique

        destination = allDestinations.get(random.nextInt(allDestinations.size()));

        Flight flight = new Flight(plane, origin, destination, generateFlightNumber(),
                                   departTime, gate);

        flights.add(flight);

        return flight;

    }

    public Flight generateFlight(Plane plane, Airport origin, Airport destination)
    {//begin generateFlight

        Flight flight = new Flight(plane, origin, destination, generateFlightNumber());

        flight.setAirline(this);

        flights.add(flight);

        return flight;

    }

    public void generateFlight(Plane plane)
    {

        int originIndex = random.nextInt(allDestinations.size()-1);
        int destIndex = random.nextInt(allDestinations.size()-1);

        if (destIndex == originIndex)
        {

            while (destIndex == originIndex)
            {

                destIndex = random.nextInt(allDestinations.size()-1);

            }

        }

        Airport origin = allDestinations.get(originIndex);
        Airport destination = allDestinations.get(destIndex);

        Flight flight = new Flight(plane, origin, destination, generateFlightNumber());

        ArrayList<Ticket> flightTickets = flight.getTickets();

        ArrayList<String> seatCodes = new ArrayList<>();

        ArrayList<ArrayList<PlaneSeat>> planeSeats = flight.getPlane().getSeats();

        for (ArrayList<PlaneSeat> planeSeat : planeSeats) {
            for (PlaneSeat seat : planeSeat) {

                seatCodes.add(seat.getSeatCode());

            }

        }

        for (int i = 0; i < flightTickets.size(); i++)
        {

            Ticket ticket = new Ticket(this, flight);
            ticket.setSeatCode(seatCodes.get(i));
            flightTickets.set(i, ticket);

        }

        flight.setFlightTimes(generateFlightTimes(380, origin, destination));

        int boardingDuration = Math.ceilDiv(plane.getPassengerCapacity(), 3);
        flight.setBoardingDuration(boardingDuration);
        flight.setDeboardingDuration(boardingDuration);

        int flightStartTime;

        if (!plane.getFlightQueue().isEmpty())
        {

            flightStartTime = plane.getLastGeneratedFlight().getEndTime();

        }
        else
        {
            flightStartTime = 240;
        }

        flight.setStartTime(flightStartTime);
        flight.setEndTime(flightStartTime+flight.getTotalDuration());
        flight.setDepartureTime(flightStartTime + flight.getBoardingDuration());

        flights.add(flight);
        plane.addFlightToQueue(flight);

    }

    public void generateFlights(int day)
    {

        for (Plane plane : airlineFleet)
        {

            do
            {

                generateFlight(plane);

            }while(plane.getLastGeneratedFlight().getEndTime() < ((day+1) * 1440));
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
        int boardingDuration = random.nextInt(30)+15;
        int deboardingDuration = random.nextInt(45)+15;

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

    public void assignTicketToPassenger(Flight flight, Passenger passenger)
    {

        ArrayList<Ticket> tickets = flight.getTickets();
        Ticket ranTicket = tickets.get(random.nextInt(tickets.size()));
        passenger.setTicket(ranTicket);
        tickets.remove(ranTicket);

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

}//end Airline class
