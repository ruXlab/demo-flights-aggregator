package com.example.demosb.suppliers.crazyair

import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.Supplier.CrazyAir
import com.example.demosb.suppliers.crazyair.CrazyAirApi.CrazyAirApiRequest
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE

fun GetFlightsParameters.toCrazyAirRequest(): CrazyAirApiRequest {
    return CrazyAirApiRequest(
        departureDate = this.departureDate,
        destination = this.destination,
        origin = this.origin,
        passengerCount = this.numberOfPassenger,
        returnDate = this.returnDate
    )
}


fun CrazyAirApi.CrazyAirApiResponse.toData(): Flight {
    return Flight(
        departureAirportCode = this.departureAirportCode,
        departureDate = LocalDate.parse(this.departureDate, ISO_LOCAL_DATE),
        airline = this.airline,
        arrivalDate = LocalDate.parse(this.arrivalDate, ISO_LOCAL_DATE),
        fare = this.price,
        supplier = CrazyAir,
        destinationAirportCode = this.destinationAirportCode
    )
}