package com.primesoft.cryptanil.utils.extensions


fun Throwable.trackException() = ifDebug { printStackTrace() }