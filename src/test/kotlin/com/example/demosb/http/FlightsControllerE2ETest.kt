package com.example.demosb.http

import assertk.assertThat
import assertk.assertions.*
import com.example.demosb.http.dto.FlightResponse
import com.example.demosb.testutils.readResource
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.github.tomakehurst.wiremock.WireMockServer
import com.github.tomakehurst.wiremock.client.WireMock.*
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request
import org.junit.jupiter.api.AfterAll
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.web.server.LocalServerPort
import org.springframework.test.context.DynamicPropertyRegistry
import org.springframework.test.context.DynamicPropertySource


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class FlightsControllerE2ETest(
    @LocalServerPort
    private val port: Int
) {

    @Test
    fun `test query providers and aggregate results`() {
        // given we have mocked suppliers responses

        // given we have okhttp configured. It could be a MockMvc test as well.
        val okHttp = OkHttpClient.Builder().build()

        val url = HttpUrl.Builder()
            .scheme("http")
            .host("localhost")
            .port(port)
            .addPathSegment("search")
            .addQueryParameter("origin", "ONE")
            .addQueryParameter("destination", "TWO")
            .addQueryParameter("departureDate", "2000-10-10")
            .addQueryParameter("returnDate", "2024-10-20")
            .addQueryParameter("numberOfPassenger", "3")
            .build()

        val req = Request.Builder().get().url(url).build()

        // when
        val response = okHttp.newCall(req).execute()

        // then
        assertThat(response.isSuccessful).isTrue()

        val json = jacksonObjectMapper().readValue(response.body().byteStream(), Array<FlightResponse>::class.java)

        assertThat(json).hasSize(4)
        assertThat(json)
            .extracting { it.fare }
            .containsExactly(
                "0.99".toBigDecimal(), "1.99".toBigDecimal(),
                "11.23".toBigDecimal(), "14.43".toBigDecimal()
            )
    }

    companion object {
        private val mockServer = WireMockServer()

        @BeforeAll
        @JvmStatic
        fun beforeAll() {
            mockServer.start()
        }

        @AfterAll
        @JvmStatic
        fun afterAll() {
            mockServer.stop()
        }

        @JvmStatic
        @DynamicPropertySource
        fun propertyConfiguration(registry: DynamicPropertyRegistry) {
            registry.add("flightsproviders.crazyair.url") { mockServer.url("/crazyair") }
            mockServer.stubFor(
                post("/crazyair").willReturn(aResponse().withBody(readResource("good-crazyair-response.json")))
            )

            registry.add("flightsproviders.toughjet.url") { mockServer.url("/toughjet") }
            mockServer.stubFor(
                post("/toughjet").willReturn(aResponse().withBody(readResource("good-toughair-response.json")))
            )

            println("${mockServer.url("/crazyair")}")
        }
    }
}