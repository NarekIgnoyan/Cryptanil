package com.primesoft.cryptanil.fragments

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.databinding.RefundRequestDalogFragmentLayoutBinding
import com.primesoft.cryptanil.models.RefundRequest
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.RegexUtils
import com.primesoft.cryptanil.utils.extensions.ifTrue

class RefundRequestDialogFragment : DialogFragment() {

    private var binding: RefundRequestDalogFragmentLayoutBinding? = null

    private var callback: ActionOne<RefundRequest>? = null

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: Exception) {
        }
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = RefundRequestDalogFragmentLayoutBinding.inflate(inflater)
        return setUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        dialog?.window!!.requestFeature(Window.FEATURE_NO_TITLE)
        dialog?.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    private fun setUp(): Dialog {
        val alertDialog = AlertDialog.Builder(requireActivity())
        alertDialog.setView(binding?.root)
        val dialoog = alertDialog.create()
        dialoog.window!!.attributes.windowAnimations = R.style.scaleDialogAnimation

        binding?.closeBT?.setOnClickListener {
            dialoog.dismiss()
        }
        binding?.submitBT?.setOnClickListener {
            submitRefundRequest()
            dialoog.dismiss()
        }

        return dialoog
    }

    private fun submitRefundRequest() {
        canContinue().ifTrue {
            callback?.invoke((getData()))
        }
    }

    private fun canContinue(): Boolean {
        binding?.let {
            return it.fullNameET.checkEmptyInput() and (it.mailET.checkEmptyInput() && it.mailET.checkRegex(
                RegexUtils.EMAIL_REGEX, R.string.mail_error
            )) and it.reasonET.checkEmptyInput() and it.descriptionET.checkEmptyInput()
        }

        return false
    }

    private fun getData() = RefundRequest(
        binding?.fullNameET?.getText(),
        binding?.mailET?.getText(),
        binding?.reasonET?.getText(),
        binding?.descriptionET?.getText()
    )

    fun setRefundCallback(callbacks: ActionOne<RefundRequest>) {
        this.callback = callbacks
    }

}