package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.app

class CreateOrder(
    val orderId: String = app.generateOrderID(),
    val clientId: String? = null,
    var auth: String? = null
)