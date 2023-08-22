package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.app

class CreateOrder(
    var auth: String? = null,
    val orderId: String = app.generateOrderID(),
    val clientId: String = app.getCustomerID(),
)