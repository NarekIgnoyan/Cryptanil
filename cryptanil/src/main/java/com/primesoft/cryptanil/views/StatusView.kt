package com.primesoft.cryptanil.views

import com.primesoft.cryptanil.models.OrderInformation

interface StatusView : AppView {
    fun onOrderInformationUpdated(orderInformation: OrderInformation)
    fun startRefund()
}