package com.primesoft.cryptanil.models

import com.google.gson.annotations.SerializedName
import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING

class Message(
    @SerializedName("localizedMessage")
    var userMessage: String,
    var messageKey: String
) {
    constructor(userMessage: String) : this(userMessage = userMessage, messageKey = EMPTY_STRING)
}