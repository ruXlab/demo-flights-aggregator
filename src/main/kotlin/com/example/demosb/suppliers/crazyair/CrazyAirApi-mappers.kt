package com.example.demosb.suppliers.crazyair

import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.Supplier.CrazyAir
import com.example.demosb.suppliers.crazyair.CrazyAirApi.CrazyAirApiRequest
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE_TIME

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
        departureDate = LocalDateTime.parse(this.departureDate, ISO_LOCAL_DATE_TIME),
        airline = this.airline,
        arrivalDate = LocalDateTime.parse(this.arrivalDate, ISO_LOCAL_DATE_TIME),
        fare = this.price,
        supplier = CrazyAir,
        destinationAirportCode = this.destinationAirportCode
    )
}