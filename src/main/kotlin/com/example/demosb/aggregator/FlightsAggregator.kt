package com.example.demosb.aggregator

import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.IFlightsSupplier
import java.util.stream.Stream

class FlightsAggregator(
    private val suppliers: List<IFlightsSupplier>
): IFlightsSupplier {
    override fun getFlights(parameters: GetFlightsParameters): Stream<Flight> {
        return suppliers.parallelStream() // <- could use a dedicated threadpool for that, see README for details
            .flatMap { it.getFlights(parameters) }
            .sorted { f1, f2 -> f1.fare.compareTo(f2.fare) }
    }
}