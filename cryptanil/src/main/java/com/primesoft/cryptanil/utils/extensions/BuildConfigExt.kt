package com.primesoft.cryptanil.utils.extensions

import com.primesoft.cryptanil.BuildConfig


fun ifDebug(action: () -> Unit) {
    if (BuildConfig.DEBUG) {
        action()
    }
}

fun ifRelease(action: () -> Unit) {
    if (!BuildConfig.DEBUG) {
        action()
    }
}
