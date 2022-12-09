package com.example.demosb.http.dto

import java.math.BigDecimal
import javax.validation.constraints.Max
import javax.validation.constraints.Min
import javax.validation.constraints.Pattern
import javax.validation.constraints.Size

class FlightSearchParams {
    @Size(min = 3, max = 3, message = "Origin must be exactly 3 characters IATA code")
    lateinit var origin: String

    @Size(min = 3, max = 3, message = "Origin must be exactly 3 characters IATA code")
    lateinit var destination: String

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Departure date must be in ISO_LOCAL_DATE format, ie yyyy-MM-dd.")
    lateinit var departureDate: String

    @Pattern(regexp = "\\d{4}-\\d{2}-\\d{2}", message = "Departure date must be in ISO_LOCAL_DATE format, ie yyyy-MM-dd.")
    lateinit var returnDate: String

    @Min(1, message = "At least one passenger should be flying")
    @Max(4, message = "Our service only supports up to 4 passengers")
    lateinit var numberOfPassenger: Integer
}

data class FlightResponse(
    val airline: String,
    val supplier: String,
    val fare: BigDecimal,
    val departureAirportCode: String,
    val destinationAirportCode: String,
    val departureDate: String,
    val arrivalDate: String,
)