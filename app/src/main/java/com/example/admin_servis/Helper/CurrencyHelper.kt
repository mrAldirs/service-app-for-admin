package com.example.admin_servis.Helper

import java.text.NumberFormat
import java.util.*

class CurrencyHelper {

    companion object {
        fun formatCurrency(amount: Int): String {
            val localeID = Locale("id", "ID")
            val currencyFormat = NumberFormat.getCurrencyInstance(localeID)

            // Mengatur minimum fraction digits menjadi 0
            currencyFormat.minimumFractionDigits = 0

            val formattedAmount = currencyFormat.format(amount.toLong())

            // Menghilangkan desimal ,00 jika ada
            return if (formattedAmount.endsWith(",00")) {
                formattedAmount.substring(0, formattedAmount.length - 3)
            } else {
                formattedAmount
            }
        }
    }
}
