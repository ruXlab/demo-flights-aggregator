package com.example.demosb.suppliers.crazyair

import assertk.assertThat
import assertk.assertions.*
import com.example.demosb.errors.ApiException
import com.example.demosb.suppliers.Flight
import com.example.demosb.suppliers.GetFlightsParameters
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.assertj.core.groups.Tuple
import org.junit.Before
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import kotlin.streams.toList


internal class CrazyAirApiTest {

    private val webServer: MockWebServer = MockWebServer()


    @Before
    fun init(): Unit {
        webServer.start()
    }

    @Test
    fun `test bad response`() {
        // given
        val mockResponse = MockResponse().apply {
            setBody("nonsense")
        }
        webServer.enqueue(mockResponse)

        val api = CrazyAirApi(
            jacksonObjectMapper().findAndRegisterModules(),
            url = "http://localhost:${webServer.port}"
        )

        // when
        val errorDescripion = assertThrows<ApiException>() {
            api.getFlights(
                GetFlightsParameters(
                    origin = "LHR",
                    destination = "ABC",
                    returnDate = LocalDate.now(),
                    departureDate = LocalDate.now(),
                    numberOfPassenger = 2
                )
            )
        }
        assertThat(errorDescripion::code).isEqualTo(500)
        assertThat(errorDescripion::error).contains("unexpected response")
        assertThat(errorDescripion::humanError).contains("One of the suppliers is not working")
    }

    @Test
    fun `happy path works`() {
        // given
        val mockResponse = MockResponse().apply {
            setBody(happyResponse)
        }
        webServer.enqueue(mockResponse)

        val api = CrazyAirApi(
            jacksonObjectMapper().findAndRegisterModules(),
            url = "http://localhost:${webServer.port}"
        )

        // when
        val flights = api.getFlights(
            GetFlightsParameters(
                origin = "LHR",
                destination = "ABC",
                returnDate = LocalDate.now(),
                departureDate = LocalDate.now(),
                numberOfPassenger = 2
            )
        ).toList()


        // then
        assertThat(flights).hasSize(2)
        assertThat(flights)
            .extracting(Flight::fare, Flight::destinationAirportCode)
            .containsExactlyInAnyOrder(
                "0.99".toBigDecimal() to "STN",
                "1.99".toBigDecimal() to "JFK"
            )
    }

    companion object {
        private val happyResponse =
            CrazyAirApi::class.java.classLoader.getResourceAsStream("good-crazyair-response.json")
                .bufferedReader()
                .readText()

    }
}