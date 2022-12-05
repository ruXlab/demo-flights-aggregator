package com.example.demosb.suppliers.crazyair

import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.Supplier.CrazyAir
import com.example.demosb.suppliers.crazyair.CrazyAirApi.CrazyAirApiRequest

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
        departureDate = this.departureDate,
        airline = this.airline,
        arrivalDate = this.arrivalDate,
        fare = this.price,
        supplier = CrazyAir,
        destinationAirportCode = this.destinationAirportCode
    )
}