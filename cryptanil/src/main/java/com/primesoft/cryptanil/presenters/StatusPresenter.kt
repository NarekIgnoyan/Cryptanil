package com.primesoft.cryptanil.presenters

import com.primesoft.cryptanil.api.ApiImplementation
import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.base.AppPresenter
import com.primesoft.cryptanil.enums.OrderStatus
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.models.RefundRequest
import com.primesoft.cryptanil.models.Response
import com.primesoft.cryptanil.views.StatusView

class StatusPresenter : AppPresenter<StatusView>() {

    override fun onCreate() {
        super.onCreate()
        getOrderInformation()
        startOrderInformationUpdater()
    }

    override fun processResponse(response: Response<*>, requestType: RequestType) {
        when (requestType) {
            RequestType.ORDER_INFORMATION -> processOrderInformation(response.result?.data as OrderInformation)
            RequestType.REFUND_REQUEST -> getOrderInformation()
            else -> {}
        }
    }

    fun requestRefund(refundRequest: RefundRequest) {
        makeRequest(
            ApiImplementation.instance.requestRefund(refundRequest),
            RequestType.REFUND_REQUEST
        )
    }

    fun navigateFromAction(partlyExpired: Boolean?) {
        if (partlyExpired == true) {
            view?.startRefund()
        } else {
            closeCryptanil()
        }
    }

    private fun processOrderInformation(orderInformation: OrderInformation) {
        val orderStatus = OrderStatus.getById(orderInformation.status)

        if (orderStatus != OrderStatus.SUBMITTED && orderStatus != OrderStatus.REFUND_REQUESTED)
            stopOrderInformationUpdater()

        view?.onOrderInformationUpdated(orderInformation)
    }

}