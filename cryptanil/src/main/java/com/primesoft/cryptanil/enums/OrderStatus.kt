package com.primesoft.cryptanil.enums

enum class OrderStatus(private var id: Int) {

    CREATED(1),
    SUBMITTED(2),
    EXPIRED(3),
    PARTLY_COMPLETED(4),
    COMPLETED(5),
    PARTLY_EXPIRED(6),
    REFUND_REQUESTED(7),
    REFUNDED(8),
    REFUNDED_REJECTED(9);

    companion object {
        fun getById(id: Int?): OrderStatus {
            for (status in values()) {
                if (status.id == id) return status
            }
            return CREATED
        }
    }

    open fun getId(): Int {
        return this.id
    }

}