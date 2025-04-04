package AirportSim;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

public class Sim   // upper level class to enclose all objects
{

    static ArrayList<ArrayList<Flight>> movingPlanes = new ArrayList<>();

    static ArrayList<Airline> airlines = new ArrayList<>();

    static File airportFile = new File("airportNamesAndCoords");

    static Scanner airportFileReader;

    static {
        try {
            airportFileReader = new Scanner(airportFile);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    static ArrayList<Airport> airports = new ArrayList<>();

    public static void main(String[] args)
    {

        for(int i = 0; i < 1440; i++)
        {

            movingPlanes.add(new ArrayList<>());

        }

        while (airportFileReader.hasNext())
        {

            String airportFileLine = airportFileReader.nextLine();
            String[] airportItems = airportFileLine.split(",");
            Airport airport = new Airport(airportItems[0], airportItems[1], Double.parseDouble(airportItems[2]),
                                  Double.parseDouble(airportItems[3]), 50);
            airports.add(airport);

        }

        Airline airlineOne = new Airline(airports);
        airlines.add(airlineOne);

        Plane plane1 = new Plane(50);

        String airlineOnePlane1SeatLayoutStr = "Row 1: FF,Row 2-13: EEEE";

        PlaneSeatClass airlineOneEconomyClass = new PlaneSeatClass("airlineOneEconomy",
                new ArrayList<>(List.of("Seat")), "E");
        PlaneSeatClass airlineOneFirstClass = new PlaneSeatClass("airlineOneFirst",
                new ArrayList<>(List.of("Lie-flat Seat", "Pillows", "Blanket")), "F");

        PlaneSeat airlineOneEconomySeat = new PlaneSeat("a1sampleEconomy", airlineOne, airlineOneEconomyClass);
        PlaneSeat airlineOneFirstSeat = new PlaneSeat("a1sampleFirst", airlineOne, airlineOneFirstClass);

        Map<String, PlaneSeat> airlineOneSeatTemplateMap = new HashMap<>();
        airlineOneSeatTemplateMap.put(airlineOneEconomySeat.getSeatClass().getClassCode(), airlineOneEconomySeat);
        airlineOneSeatTemplateMap.put(airlineOneFirstSeat.getSeatClass().getClassCode(), airlineOneFirstSeat);

        Plane airlineOnePlane1Template = new Plane(plane1, airlineOnePlane1SeatLayoutStr, airlineOneSeatTemplateMap);

        for (int i = 0; i < 50; i++)
        {

            airlineOne.addPlane(new Plane(airlineOnePlane1Template, "a1p1_"+i));

        }

        //simulation loop

        for (int day = 0; day < 5; day++)
        {

            //flight generation loop

            for(Airline airline : airlines)
            {

                airline.generateFlights(day);

            }

//            for (int min = 0; min < 1440; min++) {
//
//                if (!(movingPlanes.get(min).isEmpty())) {//begin if statement checking for moving planes
//
//                    for (int j = 0; j < movingPlanes.get(min).size(); j++) {//begin for loop that moves planes in movingPlanes
//
//                        Flight flight = movingPlanes.get(min).get(j);
//
//                        Plane flightPlane = flight.getPlane();
//
//                        flightPlane.move();
//
//                    }//end for loop that moves planes in movingPlanes
//
//                }//end if statement to move planes
//
//            }

        }

        Plane testPlane = airlineOne.getAirlineFleet().get(1);
        Flight flight1 = airlineOne.generateFlight(testPlane, airports.get(2), airports.get(5));

        Passenger passenger1 = new Passenger(flight1, "pax1");
        Passenger passenger2 = new Passenger(flight1, "pax2");

        passenger1.reserveRandomSeat();
        passenger2.reserveRandomSeat();
        System.out.println(testPlane.getSeats());
        System.out.println("passenger1 seat on ticket: " + passenger1.getTicket().getSeatCode());
        System.out.println("passenger2 seat on ticket: " + passenger2.getTicket().getSeatCode());

    }

}
