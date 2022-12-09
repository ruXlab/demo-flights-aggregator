package com.example.demosb.suppliers.crazyair

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.demosb.suppliers.GetFlightsParameters
import org.junit.jupiter.api.Test
import java.time.LocalDate

internal class CrazyAirApi_mappersKtTest {
    @Test
    fun `test get request transformed for the crazyApiProvider`() {
        // given
        val request = GetFlightsParameters(
            origin = "LHR",
            destination = "MAL",
            returnDate = LocalDate.now(),
            departureDate = LocalDate.now().minusDays(1),
            numberOfPassenger = 2
        )

        // when
        val crazyAirRequest = request.toCrazyAirRequest()

        // then
        assertThat(crazyAirRequest::origin).isEqualTo("LHR")
        assertThat(crazyAirRequest::destination).isEqualTo("MAL")
        assertThat(crazyAirRequest::returnDate).isEqualTo(LocalDate.now())
        assertThat(crazyAirRequest::departureDate).isEqualTo(request.departureDate)
        assertThat(crazyAirRequest::passengerCount).isEqualTo(request.numberOfPassenger)
    }
    
}