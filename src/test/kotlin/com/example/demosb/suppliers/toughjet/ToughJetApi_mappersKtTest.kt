package com.example.demosb.suppliers.toughjet

import assertk.assertThat
import assertk.assertions.isEqualTo
import com.example.demosb.suppliers.toughjet.ToughJetApi.ToughJetResponse
import org.junit.jupiter.api.Test

class ToughJetApi_mappersKtTest {
    @Test
    fun `test tax and discount are correctly computed`() {
        // given
        val resp = ToughJetResponse(
            carrier = "ABC", departureAirportName = "ONE", arrivalAirportName = "TWO",
            outboundDateTime = "2014-09-01T19:37:48.549Z", inboundDateTime = "2014-09-02T08:05:23.653Z",
            basePrice = "200.20".toBigDecimal(), tax = "20".toBigDecimal(), discount = "10".toBigDecimal()
        )

        // when
        val flight = resp.toFlight()

        // then
        assertThat(flight::fare).isEqualTo("216.21".toBigDecimal()) // 200.20 * (1-0.10) * 1.20 = 216.216)
    }
}