package com.example.demosb.config

import com.example.demosb.aggregator.FlightsAggregator
import com.example.demosb.suppliers.crazyair.CrazyAirApi
import com.example.demosb.suppliers.toughjet.ToughJetApi
import org.springframework.context.annotation.Bean

class ApplicationConfig {
    @Bean
    fun provideFlightsAggregator(
        crazyAirApi: CrazyAirApi, toughJetApi: ToughJetApi
    ) = FlightsAggregator(
        listOf(crazyAirApi, toughJetApi)
    )
}