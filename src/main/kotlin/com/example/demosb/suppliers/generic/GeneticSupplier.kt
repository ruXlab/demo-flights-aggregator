package com.example.demosb.suppliers.generic

import com.example.demosb.errors.ApiException
import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.suppliers.IFlightsSupplier
import com.fasterxml.jackson.core.JsonParseException
import okhttp3.OkHttpClient
import okhttp3.Request
import java.io.InputStream
import java.util.stream.Stream

abstract class GeneticSupplier(
    private val okHttp: OkHttpClient
): IFlightsSupplier {
    abstract fun buildRequest(parameters: GetFlightsParameters): Request

    abstract fun processRawResponse(inputStream: InputStream): Stream<Flight>
    override fun getFlights(parameters: GetFlightsParameters): Stream<Flight> {
        val resp = okHttp.newCall(buildRequest(parameters)).execute()

        if (!resp.isSuccessful)
            throw ApiException(500, "Response is not ok: ${resp.code()}", "One of the suppliers failed to answer, please try again")
        

        try {
            return processRawResponse(resp.body().byteStream())
        } catch (e: JsonParseException) {
            throw ApiException(500,
                error = "Upstream returned unexpected response: ${e.toString()}",
                humanError = "One of the suppliers is not working, try later"
            )
        }
    }
}