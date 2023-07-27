package com.primesoft.cryptanil.enums

enum class ProgressStatus(private var id: Int) {

    WAITING(1),
    CONFIRMING(2),
    CONFIRMED(3);

    companion object {
        fun getById(id: Int?): ProgressStatus {
            for (status in values()) {
                if (status.id == id) return status
            }
            return WAITING
        }
    }

    open fun getId(): Int {
        return this.id
    }

}