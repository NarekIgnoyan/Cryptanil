package com.primesoft.cryptanil.models

data class OrderRequiredAmount(
    val companyId: Int? = null,
    val requiredAmountCurrency: String? = null,
    val requiredCoinAmount: Double? = null,
    val coinRate: Double? = null,
    val remainingCoinAmount: Double? = null,
    val rateDate: String? = null,
    val id: Int? = null,
    val cryptanilOrderId: Int? = null,
    val currencyRate: Double? = null,
    val currencyCode: String? = null,
    val coinRateCurrency: Double? = null,
    val coin: String? = null
) : java.io.Serializable {
    fun getRemainingAmount() = remainingCoinAmount.toString() + " " + coin
    fun getCoinAmount() = remainingCoinAmount.toString()
}

