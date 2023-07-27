package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.app

class CreateOrder(
    val orderId: String = app.generateOrderID(),
    val clientId: String = "123123",
    //DEFAULT_IS_TESTING
    var auth: String = "test_9630bfcf-17ea-4bd0-b851-26cd8cc1c07d"
)