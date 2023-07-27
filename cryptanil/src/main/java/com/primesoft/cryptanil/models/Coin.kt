package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING

class Coin(
    var coin: String? = null,
    var defaultNetwork: String? = null,
    var depositAllEnable: Boolean? = null,
    var locked: Int? = null,
    var freeze: Int? = null,
    var networkList: ArrayList<Network>? = null,
    var coinIcon: String? = null
) : TypeItem() {

    override fun getType(): String = coin ?: EMPTY_STRING
    override fun getIconURL(): String = coinIcon ?: EMPTY_STRING

    fun getDefaultNetwork(): Network? {
        networkList?.forEach {
            if (it.isDefault == true)
                return it
        }
        return networkList?.get(0)
    }

    fun getDefaultCoinNetwork(selectedNetwork: String? = null): Network? {
        networkList?.forEach {
            if (it.network == (selectedNetwork ?: defaultNetwork))
                return it
        }
        return networkList?.get(0)
    }

}