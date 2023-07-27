package com.primesoft.cryptanil.models

import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.enums.OrderStatus
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING

class OrderInformation(
    var orderKey: String? = null,
    var status: Int? = null,
    var progressStatus: Int? = null,
    var orderId: String? = null,
    var redirectUrl: String? = null,
    var isTesting: Boolean? = null,
    var callBackFail: Boolean? = null,
    var triedSendCallBack: Int? = null,
    var note: String? = null,
    var txId: String? = null,
    var cryptoAmount: String? = null,
    var convertedAmount: Double? = null,
    var convertedCoinType: String? = null,
    var convertedAmountCurrency: Double? = null,
    var merchantCommission: Double? = null,
    var merchantCommissionCurrency: Double? = null,
    var cryptanilCommission: Double? = null,
    var amountToShow: Double? = null,
    var amountToShowCurrency: Double? = null,
    var depositCoinType: String? = null,
    var orderTransactions: ArrayList<OrderTransaction>? = null,
    var companyName: String? = null,
    var companyIconUrl: String? = null,
    var requiredAmount: String? = null,
    var currencyCode: String? = null,
    var invoiceColor: String? = null,
    var qrColor: String? = null,
    var totalPaid: Double? = null,
    var requiredCoinAmount: Double? = null,
    var refundDeclineReason: String? = null,
    var refundTransactionFee: Double? = null,
    var clientWillReceive: Double? = null,
    var refundTxId: String? = null,
    ) : java.io.Serializable {

    fun isPartlyExpired() = status == OrderStatus.PARTLY_EXPIRED.getId()

    fun haveRefundReason(reasonCallbacks: ActionOne<String>): Boolean {
        if (refundDeclineReason != null) {
            reasonCallbacks(refundDeclineReason ?: EMPTY_STRING)
            return true
        }
        return false
    }

    fun getTotalPaidAmount() = "$totalPaid $depositCoinType"

    fun getRefundFeeAmount() = "$refundTransactionFee $depositCoinType"

    fun getRefundAmount() = "$clientWillReceive $depositCoinType"

    fun getTotalRequiredAmount() =
        "$requiredCoinAmount $depositCoinType ( $requiredAmount $currencyCode )"

    fun getCompanyInformation() = CompanyInformation(
        companyName,
        companyIconUrl,
        requiredAmount,
        currencyCode,
        invoiceColor,
        qrColor,
        getTransaction()?.coinType,
        getTransaction()?.networkName
    )

    private fun getTransaction(): OrderTransaction? {
        if (orderTransactions?.isNotEmpty() == true)
            return orderTransactions?.get(0)

        return null
    }

    fun getStatusTitle() = OrderStatus.getById(status).toString()

    fun getOrderStatus() = OrderStatus.getById(status)

    fun getStatusMessageID(): Int =
        when (OrderStatus.getById(status ?: OrderStatus.EXPIRED.getId())) {
            OrderStatus.CREATED -> R.string.status_created_message
            OrderStatus.SUBMITTED -> R.string.status_submitted_message
            OrderStatus.EXPIRED -> R.string.status_expired_message
            OrderStatus.COMPLETED -> R.string.status_completed_message
            OrderStatus.REFUND_REQUESTED -> R.string.status_refund_requested_message
            OrderStatus.REFUNDED_REJECTED -> R.string.reason
            OrderStatus.REFUNDED -> R.string.status_refunded_message
            else -> R.string.status_empty_message
        }

    fun getIndicatorVisibility(): Boolean =
        when (OrderStatus.getById(status ?: OrderStatus.EXPIRED.getId())) {
            OrderStatus.CREATED -> true
            OrderStatus.SUBMITTED -> true
            OrderStatus.PARTLY_COMPLETED -> true
            else -> false
        }


    fun getStatusMessageColor(): Int =
        when (OrderStatus.getById(status ?: OrderStatus.EXPIRED.getId())) {
            OrderStatus.EXPIRED -> R.color.colorRed
            else -> R.color.textButtonColor
        }

    fun getStatusTitleID(): Int =
        when (OrderStatus.getById(status ?: OrderStatus.EXPIRED.getId())) {
            OrderStatus.CREATED -> R.string.status_created_title
            OrderStatus.SUBMITTED -> R.string.status_completed_title
            OrderStatus.EXPIRED -> R.string.status_expired_title
            OrderStatus.COMPLETED -> R.string.status_completed_title
            OrderStatus.PARTLY_EXPIRED, OrderStatus.REFUND_REQUESTED -> R.string.status_partly_expired
            OrderStatus.REFUNDED_REJECTED -> R.string.status_refund_rejected_title
            OrderStatus.REFUNDED -> R.string.status_refund_approved_title
            else -> R.string.status_empty_title
        }

    fun getStatusImageID(): Int =
        when (OrderStatus.getById(status ?: OrderStatus.EXPIRED.getId())) {
            OrderStatus.CREATED -> R.drawable.submitted
            OrderStatus.SUBMITTED -> R.drawable.submitted
            OrderStatus.EXPIRED, OrderStatus.REFUNDED_REJECTED -> R.drawable.expired
            OrderStatus.PARTLY_EXPIRED, OrderStatus.REFUND_REQUESTED -> R.drawable.partly_expired
            OrderStatus.REFUNDED -> R.drawable.refunded
            OrderStatus.COMPLETED -> R.drawable.completed
            else -> R.drawable.expired
        }

    fun getConvertedAmount(): String = convertedAmount.toString() + " " + convertedCoinType
    fun getConvertedAmountCurrency(): String =
        convertedAmountCurrency.toString() + " " + currencyCode

    fun getMerchantCommission(): String = merchantCommission.toString() + " " + depositCoinType
    fun getMerchantCommissionCurrency(): String =
        merchantCommissionCurrency.toString() + " " + currencyCode

    fun getAmount(): String = amountToShow.toString() + " " + convertedCoinType
    fun getAmountCurrency(): String = amountToShowCurrency.toString() + " " + currencyCode

}