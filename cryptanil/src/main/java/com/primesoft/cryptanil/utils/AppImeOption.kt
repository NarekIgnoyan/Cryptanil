package com.primesoft.cryptanil.utils

enum class AppImeOption(private var id: Int) {

    NEXT(5),
    DONE(6);

    companion object {
        fun getById(id: Int?): AppImeOption {
            for (e in values()) {
                if (e.id == id) return e
            }
            return NEXT
        }
    }

    open fun getId(): Int {
        return this.id
    }

}