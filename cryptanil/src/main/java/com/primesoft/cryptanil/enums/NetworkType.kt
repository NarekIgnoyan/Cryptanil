package com.primesoft.cryptanil.enums


enum class NetworkType(private var id: Int) {

    TON(7),
    SOL(6),
    BSC(4),
    ETH(2),
    BTC(3),
    TRX(1);

    companion object {
        fun getById(id: Int?): NetworkType? {
            for (network in values()) {
                if (network.id == id) return network
            }
            return null
        }
    }

    open fun getId(): Int {
        return this.id
    }

}