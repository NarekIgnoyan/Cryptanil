package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.enums.NetworkType
import com.primesoft.cryptanil.utils.*


class OrderTransaction(
    var coinDecimal: Double? = null,
    var coinIconUrl: String? = null,
    var coinType: String? = null,
    var cryptanilOrderId: Int? = null,
    var currencyCode: String? = null,
    var currencyRate: Double? = null,
    var currencyValueDecimal: Double? = null,
    var depositDate: String? = null,
    var fromAddress: String? = null,
    var id: Int? = null,
    var network: Int? = null,
    var networkIconUrl: String? = null,
    var networkName: String? = null,
    var toAddress: String? = null,
    var txId: String? = null,
    var value: Double? = null,
    var valueDecimal: Double? = null,
) : java.io.Serializable {

    fun getNetworkDetailsURL() = when (NetworkType.getById(network)) {
        NetworkType.TON -> TON_NETWORK.replace("{txid}", txId ?: "")
        NetworkType.SOL -> SOL_NETWORK.replace("{txid}", txId ?: "")
        NetworkType.BSC -> BSC_NETWORK.replace("{txid}", txId ?: "")
        NetworkType.ETH -> ETH_NETWORK.replace("{txid}", txId ?: "")
        NetworkType.BTC -> BTC_NETWORK.replace("{txid}", txId ?: "")
        NetworkType.TRX -> TRX_NETWORK.replace("{txid}", txId ?: "")
        else -> null
    }
}
