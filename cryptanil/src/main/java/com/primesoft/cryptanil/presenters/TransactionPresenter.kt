package com.primesoft.cryptanil.presenters

import com.primesoft.cryptanil.api.ApiImplementation
import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.app
import com.primesoft.cryptanil.base.AppPresenter
import com.primesoft.cryptanil.enums.OrderStatus
import com.primesoft.cryptanil.enums.ProgressStatus
import com.primesoft.cryptanil.models.*
import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING
import com.primesoft.cryptanil.views.TransactionView

class TransactionPresenter : AppPresenter<TransactionView>() {

    var selectedCoin: String? = null
    var selectedNetwork: String? = null

    override fun onCreate() {
        super.onCreate()
        getWalletInformation()
    }

    override fun processResponse(response: Response<*>, requestType: RequestType) {
        when (requestType) {
            RequestType.WALLET_INFORMATION -> processWalletResponse(response.result?.data as WalletInformation)
            RequestType.GET_COIN_ADDRESS -> processCoinAddressResponse(response.result?.data as CoinAddress)
            RequestType.SUBMIT_ORDER -> openOrderStatus(response.result?.data as OrderInformation)
            RequestType.ORDER_INFORMATION -> processOrderInformation(response.result?.data as OrderInformation)
            else -> {}
        }
    }

    private fun openOrderStatus(orderInformation: OrderInformation) {
        view?.onOrderSubmitted(orderInformation)
    }

    private fun processOrderInformation(orderInformation: OrderInformation) {
        if (orderInformation.status == OrderStatus.COMPLETED.getId()) {
            openOrderStatus(orderInformation)
        } else {
            val progressStatus = orderInformation.progressStatus ?: 0

            when (ProgressStatus.getById(progressStatus)) {
                ProgressStatus.CONFIRMED -> openOrderStatus(orderInformation)
                else -> {
                    view?.onProgressChanged(progressStatus)

                    if (progressStatus > ProgressStatus.WAITING.getId())
                        view.disableSpinners()

                    if (orderInformation.orderTransactions?.isNotEmpty() == true)
                        view.showTransactions(orderInformation.orderTransactions)
                }
            }
        }
    }

    private fun processCoinAddressResponse(coinAddress: CoinAddress) {
        if (coinAddress.isBinance == false) {
            getOrderInformation()
            startOrderInformationUpdater()
        } else
            stopOrderInformationUpdater()

        view.setUpCoinUI(coinAddress.isBinance ?: true)
        view?.processCoinAddress(coinAddress)
        coinAddress.orderRequiredAmount?.let {
            view.setUpRemainingAmount(it)
        }
    }

    private fun processWalletResponse(response: WalletInformation) {
        val coins = response.coins

        val defaultCoin = response.getDefaultCoin(selectedCoin)
        val networks = defaultCoin?.networkList
        val defaultNetwork = defaultCoin?.getDefaultCoinNetwork(selectedNetwork)
        view?.processWalletInformation(coins, defaultCoin, networks, defaultNetwork)
    }

    private fun getWalletInformation() {
        makeRequest(
            ApiImplementation.instance.getWalletInformation(app.token),
            RequestType.WALLET_INFORMATION
        )
    }

    fun getCoinAddress(coinType: String?, networkType: String?) {
        makeRequest(
            ApiImplementation.instance.getCoinAddress(
                app.token,
                coinType ?: EMPTY_STRING,
                networkType ?: EMPTY_STRING
            ),
            RequestType.GET_COIN_ADDRESS
        )
    }

    fun submitOrder(txId: String) {
        makeRequest(
            ApiImplementation.instance.submitOrder(SubmitOrder(txId, app.token)),
            RequestType.SUBMIT_ORDER
        )
    }

    override fun checkResponseForMessage(response: Response<*>, requestType: RequestType) {
        when (requestType) {
            RequestType.SUBMIT_ORDER -> {}
            else -> super.checkResponseForMessage(response, requestType)
        }
    }

}