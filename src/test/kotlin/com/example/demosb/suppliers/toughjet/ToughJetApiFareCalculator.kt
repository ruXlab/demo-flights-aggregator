package com.example.demosb.suppliers.toughjet

import assertk.assertThat
import assertk.assertions.isEqualTo
import org.junit.jupiter.api.Test

class ToughJetApiFareCalculator {
    @Test
    fun `test tax and discount are correctly computed`() {
        // given
        val expectedFare = "216.21".toBigDecimal() // 200.20 * (1-0.10) * 1.20 = 216.216

        // when
        val fare = toughJetApiComputeFare(
            basePrice = "200.20".toBigDecimal(),
            taxPct = "20".toBigDecimal(),
            discountPct = "10".toBigDecimal()
        )

        // then
        assertThat(fare).isEqualTo(expectedFare)
    }
}