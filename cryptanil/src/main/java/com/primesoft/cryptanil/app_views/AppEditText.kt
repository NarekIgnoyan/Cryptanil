package com.primesoft.cryptanil.app_views

import android.annotation.SuppressLint
import android.content.Context
import android.net.Uri
import android.text.Editable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.inputmethod.EditorInfo
import android.widget.FrameLayout
import androidx.core.view.isVisible
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.databinding.AppEditTextLayoutBinding
import com.primesoft.cryptanil.utils.AppImeOption
import com.primesoft.cryptanil.utils.AppTextWatcher
import com.primesoft.cryptanil.utils.RegexUtils
import com.primesoft.cryptanil.utils.extensions.*


class AppEditText(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private lateinit var binding: AppEditTextLayoutBinding

    private var errorEnabled: Boolean = false

    init {
        init(attributeSet)
        setTextChangeListener()
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        binding = AppEditTextLayoutBinding.inflate(LayoutInflater.from(context), this, true)

        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.AppEditText
        )

        setHint(ta.getString(R.styleable.AppEditText_hint) ?: EMPTY_STRING)
        setText(ta.getString(R.styleable.AppEditText_text) ?: EMPTY_STRING)
        setImeOption(ta.getInteger(R.styleable.AppEditText_imeOption, EditorInfo.IME_NULL))
        disable(ta.getBoolean(R.styleable.AppEditText_disable, false))
        showCopy(ta.getBoolean(R.styleable.AppEditText_showCopy, false))
        ta.recycle()
    }

    fun setText(text: String?) {
        binding.inputET.setText(text ?: EMPTY_STRING)
    }

    fun setHint(hint: String?) {
        binding.hintTV.text = hint ?: EMPTY_STRING
        binding.copyTV.text =
            String.format(
                resources.getString(R.string.copy_text),
                (hint ?: EMPTY_STRING).lowercase()
            )
    }

    fun clear() = setText(EMPTY_STRING)

    fun getText(): String = binding.inputET.text.toString()

    fun getEditText() = binding.inputET

    private fun setImeOption(index: Int?) {
        val imeOption = AppImeOption.getById(index)
        binding.inputET.imeOptions = imeOption.getId()
    }

    private fun showCopy(show: Boolean?) {
        show?.ifTrue {
            binding.copyTV.underline()
            binding.copyTV.visible()
            binding.copyTV.setOnClickListener {
                copyInput()
            }
        }
    }

    fun setClickListener(clickListener: OnClickListener?) {
        clickListener?.let {
            binding.inputET.isFocusable = false
            binding.inputET.setOnClickListener(it)
        }
    }

    fun disable(disable: Boolean) {
        binding.inputET.isFocusable = !disable
        binding.inputET.isEnabled = !disable
    }

    private fun copyInput() {
        binding.inputET.text.toString().copy()
        toast(resources.getString(R.string.copied))
    }

    fun enableInfoMode() {
        binding.infoIV.visible()
        binding.inputET.setPadding(38.toPx(), 0, 10.toPx(), 0)
    }

    fun setInfoIcon(iconURL: String) {
        GlideToVectorYou
            .init()
            .with(this.context)
            .load(Uri.parse(iconURL), binding.infoIV)
    }

    private fun setTextChangeListener(listener: ((Int) -> Unit)? = null) {
        binding.inputET.addTextChangedListener(object : AppTextWatcher() {
            override fun afterTextChanged(s: Editable) {
                listener?.invoke(s.length)
                disableError()
            }
        })
    }

    private fun enableError(message: String) {
        binding.root.animateChangeBounds()
        binding.errorTV.text = message
        binding.errorTV.visible()
        errorEnabled = true
    }

    private fun disableError() {
        binding.errorTV.isVisible.ifTrue {
            binding.root.animateChangeBounds()
            binding.errorTV.invisible()
            binding.errorTV.text = ""
            errorEnabled = false
        }
    }

    fun checkEmptyInput(): Boolean {
        return checkEmptyInput(context.getString(R.string.required_field))
    }

    fun checkEmptyInput(messageId: Int): Boolean {
        return checkEmptyInput(resources.getString(messageId))
    }

    fun checkEmptyInput(message: String): Boolean {
        if (getText().isEmpty()) {
            enableError(message)
            return false
        } else {
            disableError()
        }
        return true
    }

    fun checkRegex(regEx: String, messageId: Int): Boolean {
        val isValid = RegexUtils.validation(regEx, getEditText().text.toString())
        if (!isValid) {
            enableError(resources.getString(messageId))
        }
        return isValid
    }

}