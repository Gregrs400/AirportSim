This document will go through the fields and functionality present in the Airport.java file. This is to assist with further development of the application as well as aiding in explaining to users how the software functions. 

# Fields

| name | type | purpose |
| :----: | :----: | :-------: |
| random | Random (java.util) | Generating random values |
| gates | ArrayList\<Gate\> | Storing gate objects |
| parkingReservations | HashMap\<ParkingLocation, ArrayList\<ParkingReservation\>\> | Storing reservations for plane parking |
| apronParkingSpots | ArrayList\<ApronParking\> | Storing parking spots on the apron | 
| paxInAirport | ArrayList\<Passenger\> | Storing the passengers currently in the airport | 

# Functions
