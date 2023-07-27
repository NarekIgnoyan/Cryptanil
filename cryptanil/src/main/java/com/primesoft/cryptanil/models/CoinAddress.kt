package com.primesoft.cryptanil.models

class CoinAddress(
    var coin: String? = null,
    var address: String? = null,
    var tag: String? = null,
    var url: String? = null,
    var isBinance: Boolean? = null,
    var coinIconUrl: String? = null,
    var networkIconUrl: String? = null,
    var qrIconUrl: String? = null,
    var orderRequiredAmount: OrderRequiredAmount? = null
)