package com.kuliah.medicalife.utils

import java.text.NumberFormat
import java.util.Locale

object PriceFormatter {
    fun formatPrice(price: Int): String {
        val localeId = Locale("id","ID")
        val numberFormat = NumberFormat.getCurrencyInstance(localeId)

        numberFormat.maximumFractionDigits = 0
        val formattedPrice = numberFormat.format(price)

        return formattedPrice.replace("Rp", "Rp ")
    }
}