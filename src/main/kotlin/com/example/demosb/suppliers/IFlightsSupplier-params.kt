package com.example.demosb.suppliers

import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime

data class GetFlightsParameters(
    val origin: String,
    val destination: String,
    val departureDate: LocalDate,
    val returnDate: LocalDate,
    val numberOfPassenger: Int
)

data class Flight(
    val airline: String,
    val supplier: Supplier,
    val fare: BigDecimal,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: LocalDateTime,
    val arrivalDate: LocalDateTime
)

enum class Supplier {
    CrazyAir, ToughJet
}