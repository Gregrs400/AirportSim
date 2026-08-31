This document will go through the fields and functionality present in the Airport.java file. This is to assist with further development of the application as well as aiding in explaining to users how the software functions. 

# Fields

| name | type | purpose |
| :----: | :----: | :-------: |
| random | Random (java.util) | Generating random values |
| gates | ArrayList\<Gate\> | Storing gate objects |
| parkingReservations | HashMap\<ParkingLocation, ArrayList\<ParkingReservation\>\> | Storing reservations for plane parking |
| apronParkingSpots | ArrayList\<ApronParking\> | Storing parking spots on the apron | 
| paxInAirport | ArrayList\<Passenger\> | Storing the passengers currently in the airport | 
| airportLocation | String | Storing city name of airport's location |
| airportCode | String | Storing shorter name of airport for referencing |
| latitude | double | Storing latitude location of Airport |
| longitude | double | Storing longitude location of Airport |
| paxCounter | int | Storing passengers generated counter for use during passenger generation |
| paxGenerated | int | Storing total number of passengers generated | 
| paxBeingGenerated | int | Storing number of passengers being generated during 1 generation session |
| totalPaxCapacity | int | Storing total number of passengers that can be generated |
| departures | ArrayList\<Flight\> | Storing flights scheduled to leave airport |

# Functions
