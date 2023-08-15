package com.primesoft.cryptanil.views

import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.models.Error
import com.primesoft.cryptanil.models.Message
import com.primesoft.cryptanil.models.OrderInformation

interface AppView : LoadingView {
    fun onAllRequestsFinished()
    fun showError(error: Error, requestType: RequestType)
    fun showReject(error: Error)
    fun showMessage(message: Message, requestType: RequestType)
    fun showOfflineDialog()
    fun showTimeOutDialog()
    fun showUnknownError()
    fun closeApplication(orderInformation: OrderInformation?)
}