package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.app

class RefundRequest(
    var fullName: String? = null,
    var mail: String? = null,
    var reason: String? = null,
    var description: String? = null,
    var auth: String = app.token
)