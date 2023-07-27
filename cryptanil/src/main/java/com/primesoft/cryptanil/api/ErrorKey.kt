package com.primesoft.cryptanil.api

enum class ErrorKey(private var key: String) {

    REJECT("reject");

    companion object {
        fun getById(id: String): ErrorKey {
            for (error in values()) {
                if (error.key == id) return error
            }
            return REJECT
        }
    }

    open fun getId(): String {
        return this.key
    }

}