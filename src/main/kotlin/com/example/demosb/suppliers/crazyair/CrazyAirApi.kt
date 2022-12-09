package com.example.demosb.suppliers.crazyair

import com.example.demosb.errors.ApiException
import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.IFlightsSupplier
import com.fasterxml.jackson.core.JsonParseException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.math.BigDecimal
import java.time.LocalDate
import java.util.*
import java.util.stream.Stream

/**
 * This is a crazy simple flights provider
 * It was initially codded during the pairing session so I'm not touching it,
 * although it could be good candidate for the generalisation
 */
@Service
class CrazyAirApi(
    private val jackson: ObjectMapper,
    @Value("\${flightsproviders.crazyair.url}")
    private val url: String,
    private val okHttp: OkHttpClient = OkHttpClient.Builder().build()
) : IFlightsSupplier {

    override fun getFlights(parameters: GetFlightsParameters): Stream<Flight> {
        val req = Request.Builder().url(url).post(
            RequestBody.create(MediaType.parse("application/json"), jackson.writeValueAsString(parameters.toCrazyAirRequest()))
        ) .build()
        val resp = okHttp.newCall(req).execute()

        if (!resp.isSuccessful) {
            throw ApiException(500, "Response is not ok: ${resp.code()}", "One of the suppliers failed to answer, please try again")
        }

        try {
            val jsonResponse = resp.body().byteStream()

            return jackson.readValue<CrazyAirRootResponse>(jsonResponse)
                .stream()
                .map(CrazyAirApiResponse::toData)
        } catch (e: JsonParseException) {
            throw ApiException(500,
                error = "Upstream returned unexpected response: ${e.toString()}",
                humanError = "One of the suppliers is not working, try later"
            )
        }
    }



    data class CrazyAirApiRequest(
        val origin: String,
        val destination: String,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val departureDate: LocalDate,
        @JsonDeserialize(using = LocalDateDeserializer::class)
        val returnDate: LocalDate,
        val passengerCount: Int
    )


    class CrazyAirRootResponse: ArrayList<CrazyAirApiResponse>()

    data class CrazyAirApiResponse(
        val airline: String,
        val price: BigDecimal,
        val cabinclass:	String,
        val departureAirportCode: String,
        val destinationAirportCode:	String,
        val departureDate: String,
        val arrivalDate: String,
    )

}