package com.example.demosb.suppliers.toughjet

import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode

private val manyDecimalsMc = MathContext(40, RoundingMode.DOWN)
private val HUNDRED = 100.toBigDecimal(manyDecimalsMc)

/**
 * Compute ToughJet fare
 */
fun toughJetApiComputeFare(basePrice: BigDecimal, discountPct: BigDecimal, taxPct: BigDecimal): BigDecimal {
    val discountedPrice = basePrice - basePrice * (
        discountPct.divide(HUNDRED, manyDecimalsMc)
    )
    val priceAfterTax = discountedPrice + discountedPrice * (
        taxPct.divide(HUNDRED, manyDecimalsMc)
    )
    return priceAfterTax.setScale(2, RoundingMode.DOWN)
}