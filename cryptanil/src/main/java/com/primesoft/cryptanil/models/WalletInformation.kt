package com.primesoft.cryptanil.models

class WalletInformation(
    var coins: ArrayList<Coin>? = null,
    var defaultCoin: String? = null,
    var defaultNetwork: String? = null,
) {

    fun getDefaultCoin(selectedCoinName: String? = null): Coin? {
        coins?.forEach {
            if (it.coin == (selectedCoinName ?: defaultCoin)) {
                it.defaultNetwork = defaultNetwork
                return it
            }
        }
        return coins?.get(0)
    }

}