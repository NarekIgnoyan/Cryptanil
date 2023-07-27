package com.primesoft.cryptanil.models

class Response<R>(var result: Result<R>?, var error: Error?) {

    fun hasError(): Boolean {
        return result == null && error != null
    }

    fun hasMessage(): Boolean {
        return result != null && result?.message != null
    }

}