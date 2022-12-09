package com.example.demosb.http

import com.example.demosb.aggregator.FlightsAggregator
import com.example.demosb.http.dto.FlightResponse
import com.example.demosb.http.dto.FlightSearchParams
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController
import javax.validation.Valid


@RestController
class FlightsController(
    private val aggregator: FlightsAggregator
) {
    @GetMapping("/search")
    fun search(@Valid params: FlightSearchParams): List<FlightResponse> {
        return aggregator.getFlights(params.toData()).toDto()
    }

}