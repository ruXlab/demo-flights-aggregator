package com.example.demosb.suppliers

import com.example.demosb.suppliers.crazyair.CrazyAirApi
import java.util.stream.Stream

interface IFlightsSupplier {
    fun getFlights(parameters: GetFlightsParameters): Stream<Flight>
}