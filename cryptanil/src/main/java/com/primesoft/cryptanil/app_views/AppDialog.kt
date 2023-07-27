package com.primesoft.cryptanil.app_views

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
import com.primesoft.cryptanil.databinding.AppDialogLayoutBinding
import com.primesoft.cryptanil.utils.extensions.ifTrue
import com.primesoft.cryptanil.utils.extensions.visible

const val ICON_KEY = "_icon_key"
const val MESSAGE_KEY = "_message_key"

class AppDialog : DialogFragment() {

    private var binding: AppDialogLayoutBinding? = null

    private var iconRes: Int? = null
    private var message: String? = null
    private var onButtonClickListener: (() -> Unit)? = null

    companion object {
        const val TAG = "_apAlertDialog"

        fun getInstance(
            iconRes: Int?,
            message: String?
        ): AppDialog {
            val bundle = Bundle()
            bundle.putInt(ICON_KEY, iconRes ?: -1)
            bundle.putString(MESSAGE_KEY, message)
            val dialog = AppDialog()
            dialog.arguments = bundle
            return dialog
        }
    }

    override fun show(manager: FragmentManager, tag: String?) {
        try {
            val ft = manager.beginTransaction()
            ft.add(this, tag)
            ft.commitAllowingStateLoss()
        } catch (ignored: Exception) {
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        iconRes = arguments?.getInt(ICON_KEY)
        message = arguments?.getString(MESSAGE_KEY)
    }

    @SuppressLint("InflateParams")
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = activity?.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = AppDialogLayoutBinding.inflate(inflater)
        return setUp()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
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

        (iconRes != -1).ifTrue {
            binding?.dialogIV?.setImageResource(iconRes!!)
            binding?.dialogIV?.visible()
        }

        message?.let {
            binding?.dialogMessageTV?.text = message
            binding?.dialogMessageTV?.visible()
        }

        binding?.dialogBT?.setOnClickListener {
            onButtonClickListener?.invoke()
            dismiss()
        }

        return dialoog
    }

    fun setButtonClickListener(onButtonClickListener: (() -> Unit)?) {
        this.onButtonClickListener = onButtonClickListener
    }

}