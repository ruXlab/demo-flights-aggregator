package com.example.demosb.suppliers.toughjet

import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.Supplier.ToughJet
import com.example.demosb.suppliers.toughjet.ToughJetApi.ToughJetRequest
import com.example.demosb.suppliers.toughjet.ToughJetApi.ToughJetResponse
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter.ISO_INSTANT
import java.time.format.DateTimeFormatter.ISO_LOCAL_DATE


fun GetFlightsParameters.toToughJetRequest() = ToughJetRequest(
    from = this.origin,
    to = this.origin,
    outboundDate = ISO_LOCAL_DATE.format(this.departureDate),
    inboundDate = ISO_LOCAL_DATE.format(this.returnDate),
    numberOfAdults = this.numberOfPassenger
)

private val ZONED_ISO_INSTANT = ISO_INSTANT.withZone(ZoneId.systemDefault())
fun ToughJetResponse.toFlight(): Flight = Flight(
    airline = this.carrier,
    arrivalDate = LocalDate.parse(this.inboundDateTime, ZONED_ISO_INSTANT),
    departureDate = LocalDate.parse(this.outboundDateTime, ZONED_ISO_INSTANT),
    departureAirportCode = this.departureAirportName,
    destinationAirportCode = this.arrivalAirportName,
    fare = toughJetApiComputeFare(this.basePrice, this.discount, this.tax),
    supplier = ToughJet
)