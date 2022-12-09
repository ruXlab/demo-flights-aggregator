package com.example.demosb.suppliers.toughjet

import assertk.assertThat
import assertk.assertions.hasSize
import assertk.assertions.isEqualTo
import com.example.demosb.suppliers.GetFlightsParameters
import com.example.demosb.testutils.div
import com.example.demosb.testutils.readResource
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import org.junit.Before
import org.junit.jupiter.api.Test
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.Month.SEPTEMBER
import kotlin.streams.toList
import java.time.LocalTime.parse as parseTime

class ToughJetApiTest {
    private val webServer: MockWebServer = MockWebServer()

    @Before
    fun init() {
        webServer.start()
    }

    @Test
    fun `ToughJetApi can process valid response`() {
        // given
        webServer.enqueue(MockResponse().apply {
            setBody(happyResponse)
        })

        val api = ToughJetApi(
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

        val beAir = flights.single { it.airline == "BeAir" }
        assertThat(beAir::departureDate)
            .isEqualTo(LocalDateTime.of(2 / SEPTEMBER / 2014, parseTime("09:05:00")))
        assertThat(beAir::arrivalDate)
            .isEqualTo(LocalDateTime.of(2 / SEPTEMBER / 2014, parseTime("13:30:00")))
        assertThat(beAir::fare).isEqualTo("14.43".toBigDecimal())
        assertThat(beAir::departureAirportCode).isEqualTo("ONE")
        assertThat(beAir::destinationAirportCode).isEqualTo("TWO")

        val triAir = flights.single { it.airline == "TriAir" }
        assertThat(triAir::departureDate)
            .isEqualTo(LocalDateTime.of(21 / SEPTEMBER / 2014, parseTime("09:05:00")))
        assertThat(triAir::arrivalDate)
            .isEqualTo(LocalDateTime.of(22 / SEPTEMBER / 2014, parseTime("13:30:00")))
        assertThat(triAir::fare).isEqualTo("11.23".toBigDecimal())
        assertThat(triAir::departureAirportCode).isEqualTo("ABC")
        assertThat(triAir::destinationAirportCode).isEqualTo("DEF")
    }

    companion object {
        private val happyResponse = readResource("good-toughair-response.json")
    }

}