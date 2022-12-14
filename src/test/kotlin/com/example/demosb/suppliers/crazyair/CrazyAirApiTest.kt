package com.example.demosb.suppliers.crazyair

import assertk.assertThat
import assertk.assertions.contains
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.example.demosb.errors.ApiException
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.testutils.div
import com.example.demosb.testutils.readResource
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month.DECEMBER
import java.time.Month.NOVEMBER
import kotlin.streams.toList
import java.time.LocalTime.parse as parseTime


internal class CrazyAirApiTest {
    private val webServer: MockWebServer = MockWebServer()

    @Before
    fun init() {
        webServer.start()
    }

    @Test
    fun `test bad response`() {
        // given
        webServer.enqueue(MockResponse().apply {
            setBody("nonsense")
        })

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
    fun `CrazyAirApi can process valid response`() {
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

        val flight1 = flights.single { it.airline == "AirlineName1" }
        assertThat(flight1::departureDate).isEqualTo(LocalDateTime.of(3 / DECEMBER / 2011, parseTime("20:20:00")))
        assertThat(flight1::arrivalDate).isEqualTo(LocalDateTime.of(4 / DECEMBER / 2011, parseTime("04:20:00")))
        assertThat(flight1::fare).isEqualTo("0.99".toBigDecimal())
        assertThat(flight1::departureAirportCode).isEqualTo("LHR")
        assertThat(flight1::destinationAirportCode).isEqualTo("STN")

        val flight2 = flights.single { it.airline == "AirlineName2" }
        assertThat(flight2::departureDate).isEqualTo(LocalDateTime.of(3 / NOVEMBER / 2011, parseTime("10:15:30")))
        assertThat(flight2::arrivalDate).isEqualTo(LocalDateTime.of(3 / NOVEMBER / 2011, parseTime("15:10:00")))
        assertThat(flight2::fare).isEqualTo("1.99".toBigDecimal())
        assertThat(flight2::departureAirportCode).isEqualTo("LHR")
        assertThat(flight2::destinationAirportCode).isEqualTo("JFK")
    }

    companion object {
        private val happyResponse = readResource("good-crazyair-response.json")
    }
}