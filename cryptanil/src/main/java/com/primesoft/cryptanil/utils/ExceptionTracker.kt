package com.primesoft.cryptanil.utils

class ExceptionTracker {

    companion object {
        fun trackException(t: Throwable) = t.printStackTrace()
    }

}