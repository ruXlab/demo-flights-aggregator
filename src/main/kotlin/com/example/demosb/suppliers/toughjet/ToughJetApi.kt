package com.example.demosb.suppliers.toughjet

import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.toughjet.ToughJetApi.ToughJetRequest
import com.example.demosb.suppliers.toughjet.ToughJetApi.ToughJetResponse
import com.example.demosb.suppliers.generic.GenericHttpPostSupplier
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.OkHttpClient
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal

@Service
class ToughJetApi(
    jackson: ObjectMapper,
    @Value("\$flightsproviders.toughjet.url")
    url: String,
    okHttp: OkHttpClient = OkHttpClient.Builder().build()
): GenericHttpPostSupplier<ToughJetRequest, ToughJetResponse>(
    url, ToughJetResponse::class, jackson, okHttp
) {
    override fun makeRequestBody(parameters: GetFlightsParameters): ToughJetRequest = parameters.toToughJetRequest()
    override fun mapToFlight(rawEntity: ToughJetResponse): Flight = rawEntity.toFlight()
    data class ToughJetRequest(
        val from: String,
        val to: String,
        val outboundDate: String,
        val inboundDate: String,
        val numberOfAdults: Int,
    )

    data class ToughJetResponse(
        val carrier: String,
        val basePrice: BigDecimal,
        val tax: BigDecimal,
        val discount: BigDecimal,
        val departureAirportName: String,
        val arrivalAirportName: String,
        val outboundDateTime: String,
        val inboundDateTime: String,
    )
}