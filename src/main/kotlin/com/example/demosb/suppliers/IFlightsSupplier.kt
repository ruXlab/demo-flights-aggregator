package com.example.demosb.suppliers

import java.util.stream.Stream

interface IFlightsSupplier {
    fun getFlights(parameters: GetFlightsParameters): Stream<Flight>
}