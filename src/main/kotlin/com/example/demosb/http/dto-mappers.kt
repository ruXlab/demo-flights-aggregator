package com.example.demosb.http

import com.example.demosb.http.dto.FlightResponse
import com.example.demosb.http.dto.FlightSearchParams
import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import java.time.LocalDate
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE
import java.util.stream.Stream
import kotlin.streams.toList

fun FlightSearchParams.toData(): GetFlightsParameters {
    return GetFlightsParameters(
        origin = this.origin,
        destination = this.destination,
        departureDate = LocalDate.parse(this.departureDate, ISO_LOCAL_DATE),
        returnDate = LocalDate.parse(this.departureDate, ISO_LOCAL_DATE),
        numberOfPassenger = this.numberOfPassenger.toInt(),
    )
}

fun Flight.toDto(): FlightResponse {
    return FlightResponse(
        airline = this.airline,
        supplier = this.supplier.name,
        fare = this.fare,
        departureAirportCode = this.departureAirportCode,
        destinationAirportCode = this.destinationAirportCode,
        departureDate = ISO_DATE_TIME.format(this.departureDate),
        arrivalDate = ISO_DATE_TIME.format(this.departureDate),
    )
}

fun Stream<Flight>.toDto(): List<FlightResponse> =
    this.map(Flight::toDto).toList()

