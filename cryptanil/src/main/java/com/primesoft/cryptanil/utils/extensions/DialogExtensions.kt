package com.primesoft.cryptanil.utils.extensions


import androidx.fragment.app.FragmentActivity
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.app_views.AppDialog
import com.primesoft.cryptanil.fragments.RefundRequestDialogFragment
import com.primesoft.cryptanil.models.RefundRequest
import com.primesoft.cryptanil.utils.ActionOne


fun FragmentActivity.showInfoDialog(
    messageID: Int?,
    onButtonClickListener: (() -> Unit)? = null
) {
    showDialog(null, messageID?.let { getString(it) }, onButtonClickListener)
}

fun FragmentActivity.showInfoDialog(
    message: String,
    onButtonClickListener: (() -> Unit)? = null
) {
    showDialog(null, message, onButtonClickListener)
}


fun FragmentActivity.showErrorDialog(
    messageID: Int?,
    onButtonClickListener: (() -> Unit)? = null
) {
    showDialog(
        R.drawable.ic_warning,
        messageID?.let { getString(it) },
        onButtonClickListener
    )
}

fun FragmentActivity.showErrorDialog(
    message: String?,
    onButtonClickListener: (() -> Unit)? = null
) {
    showDialog(
        R.drawable.ic_warning,
        message,
        onButtonClickListener
    )
}

fun FragmentActivity.showSuccessDialog(
    messageID: Int?,
    onButtonClickListener: (() -> Unit)? = null
) {
    showDialog(
        null,
        messageID?.let { getString(it) },
        onButtonClickListener
    )
}

fun FragmentActivity.showSuccessDialog(
    message: String?,
    onButtonClickListener: (() -> Unit)? = null
) {
    showDialog(
        null,
        message,
        onButtonClickListener
    )
}

fun FragmentActivity.showDialog(
    iconResID: Int?,
    messageID: String?,
    onButtonClickListener: (() -> Unit)? = null
) {
    val alert =
        AppDialog.getInstance(
            iconResID,
            messageID,
        )
    alert.isCancelable = false
    alert.setButtonClickListener(onButtonClickListener)
    alert.show(this.supportFragmentManager, AppDialog.TAG)
}

fun FragmentActivity.showRefundDialog(
    callback: ActionOne<RefundRequest>
) {
    val alert = RefundRequestDialogFragment()

    alert.isCancelable = false
    alert.setRefundCallback(callback)
    alert.show(this.supportFragmentManager, getFragmentTag<RefundRequestDialogFragment>())
}