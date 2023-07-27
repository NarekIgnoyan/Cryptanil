package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING

class Network(
    var network: String? = null,
    var name: String? = null,
    var coin: String? = null,
    var isDefault: Boolean? = null,
    var depositEnable: Boolean? = null,
    var networkIcon: String? = null,
    var qrIcon: String? = null
) : TypeItem() {
    override fun getType(): String = name ?: EMPTY_STRING
    override fun getIconURL(): String = networkIcon ?: EMPTY_STRING
}