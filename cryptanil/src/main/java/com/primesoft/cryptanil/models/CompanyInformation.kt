package com.primesoft.cryptanil.models

class CompanyInformation(
    var companyName: String? = null,
    var companyIconUrl: String? = null,
    var requiredAmount: String? = null,
    var currencyCode: String? = null,
    var invoiceColor: String? = null,
    var qrColor: String? = null,
    var coinType: String? = null,
    var networkName: String? = null
) : java.io.Serializable