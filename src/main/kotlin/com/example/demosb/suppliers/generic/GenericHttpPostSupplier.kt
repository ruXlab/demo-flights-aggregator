package com.example.demosb.suppliers.generic

import com.example.demosb.errors.ApiException
import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.crazyair.toCrazyAirRequest
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody
import java.io.InputStream
import java.util.stream.Stream
import java.util.stream.StreamSupport
import kotlin.reflect.KClass

/**
 * For the sake of demo in IRL some generic adapters often become slightly more specific and again
 * helping to reduce boilerplate code
 *
 * This adapter can handle providers exposing REST JSON via POST interface
 * and returning response shaped as [ flight1, flight2, flightN ]
 */
abstract class GenericHttpPostSupplier<REQUEST: Any, RESPONSE: Any>(
    private val url: String,
    private val responseClass: KClass<RESPONSE>,
    private val jackson: ObjectMapper,
    okHttpClient: OkHttpClient = OkHttpClient.Builder().build(),
): GeneticSupplier(okHttpClient) {

    abstract fun makeRequestBody(parameters: GetFlightsParameters): REQUEST
    abstract fun mapToFlight(rawEntity: RESPONSE): Flight
    override fun buildRequest(parameters: GetFlightsParameters): Request {
        val providerSpecificRequest = jackson.writeValueAsString(makeRequestBody(parameters))
        return Request.Builder()
            .url(url)
            .post(RequestBody.create(MediaType.parse("application/json"), jackson.writeValueAsString(parameters.toCrazyAirRequest()))
        ).build()
    }

    override fun processRawResponse(inputStream: InputStream): Stream<Flight> {
        val root = jackson.readTree(inputStream)
        if (!root.isArray)
            throw ApiException(500,
                error = "Provider at $url returned unexpected response. An array was expected",
                humanError = "One of the providers is down, try again in 5 minutes"
            )

        // memory footprint of this is M(1), comes for free thanks to jackson
        return StreamSupport.stream(root.spliterator(), false)
            .map { jackson.convertValue(it, responseClass.java) }
            .map(::mapToFlight)
    }
}