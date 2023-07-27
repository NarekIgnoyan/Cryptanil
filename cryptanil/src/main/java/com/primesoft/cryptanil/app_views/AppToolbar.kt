package com.primesoft.cryptanil.app_views

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.databinding.AppToolbarLayoutBinding
import com.primesoft.cryptanil.utils.VoidCallback
import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING
import com.primesoft.cryptanil.utils.extensions.visible

class AppToolbar(context: Context, attributes: AttributeSet) :
    RelativeLayout(context, attributes) {

    private lateinit var binding: AppToolbarLayoutBinding

    init {
        init(attributes)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        binding = AppToolbarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        val ta = context.obtainStyledAttributes(
            attrs,
            R.styleable.AppToolbar
        )
        setIcon(ta.getDrawable(R.styleable.AppToolbar_icon))
        setTitle(ta.getString(R.styleable.AppToolbar_title))
        setActionText(ta.getString(R.styleable.AppToolbar_actionText))
        ta.recycle()
    }

    private fun setIcon(iconDrawable: Drawable?) {
        iconDrawable?.let {
            binding.toolbarIV.visible()
            binding.toolbarIV.setImageDrawable(iconDrawable)
        }
    }

    private fun setTitle(title: String?) {
        binding.titleTV.text = title ?: EMPTY_STRING
    }

    private fun setActionText(actionText: String?) {
        binding.actionTV.text = actionText ?: EMPTY_STRING
    }

    fun onActionClicked(clickCallback: VoidCallback) {
        binding.actionTV.setOnClickListener { clickCallback() }
    }

}