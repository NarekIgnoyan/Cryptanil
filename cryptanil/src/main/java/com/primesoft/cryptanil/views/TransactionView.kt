package com.primesoft.cryptanil.views

import com.primesoft.cryptanil.models.*

interface TransactionView : AppView {
    fun processWalletInformation(
        coins: ArrayList<Coin>?,
        defaultCoin: Coin?,
        networks: ArrayList<Network>?,
        defaultNetwork: Network?
    )

    fun processCoinAddress(coinAddress: CoinAddress)

    fun setUpRemainingAmount(orderRequiredAmount: OrderRequiredAmount)

    fun setUpCoinUI(isBinance: Boolean)

    fun onOrderSubmitted(orderInformation: OrderInformation)

    fun onProgressChanged(progressStatus: Int)

    fun disableSpinners()

    fun showTransactions(transactions: ArrayList<OrderTransaction>?)
}