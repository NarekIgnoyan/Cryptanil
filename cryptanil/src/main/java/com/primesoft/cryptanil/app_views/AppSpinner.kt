package com.primesoft.cryptanil.app_views

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.databinding.AppSpinnerLayoutBinding
import com.primesoft.cryptanil.models.TypeItem
import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING
import com.primesoft.cryptanil.utils.spinner_adapter.AppSpinnerAdapter

class AppSpinner(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private lateinit var binding: AppSpinnerLayoutBinding

    private var data: ArrayList<TypeItem> = ArrayList()
    private var adapter: AppSpinnerAdapter? = null
    private var changeListener: ((TypeItem, Int) -> Unit)? = null
    private var selectedPosition: Int? = null

    init {
        init(attributeSet)
    }

    @SuppressLint("CustomViewStyleable")
    private fun init(attrs: AttributeSet?) {
        binding = AppSpinnerLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        val ta = context.obtainStyledAttributes(attrs, R.styleable.AppSpinner)
        setText(ta.getString(R.styleable.AppSpinner_text))
        setHint(ta.getString(R.styleable.AppSpinner_hint))
        ta.recycle()

        setUpUI()
    }

    private fun setUpUI() {
        setDropdownArrow()
    }

    fun initSpinner(
        types: ArrayList<TypeItem>, changeListener: ((TypeItem, Int) -> Unit)? = null
    ) {
        this.changeListener = changeListener

        data.clear()
        data.addAll(types)

        adapter = AppSpinnerAdapter(context, data) { item, position ->
            selectedPosition = position
            binding.typeET.setText(item.getType())
            changeListener?.invoke(item, position)
            binding.spinner.onDetachedFromWindow()
        }
        binding.spinner.adapter = adapter
        changeListener?.let {
            binding.typeET.setClickListener {
                binding.spinner.performClick()
            }
        }
    }

    private fun select(position: Int) {
        selectedPosition = position
        binding.spinner.setSelection(position)
        val item = data[position]
        setText(item.getType())
        setInfoIcon(item.getIconURL())
        changeListener?.invoke(item, position)
    }

    fun select(item: TypeItem?) {
        data.forEachIndexed { index, spinnerItem ->
            if (item?.getType() == spinnerItem.getType()) {
                select(index)
            }
        }
    }

    private fun setText(text: String?) {
        binding.typeET.setText(text)
    }

    private fun setHint(hint: String?) {
        binding.typeET.setHint(hint)
    }

    fun clear() {
        binding.typeET.setText(EMPTY_STRING)
    }

    private fun setDropdownArrow() {
        binding.typeET.getEditText()
            .setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_arrow, 0)
    }

    fun setClickListener(clickListener: OnClickListener?) {
        clickListener?.let {
            binding.typeET.setClickListener(it)
        }
    }

    fun getText(): String = binding.typeET.getText()

    fun getSelected(): TypeItem = data[selectedPosition ?: 0]

    fun disable() {
        binding.typeET.disable(true)
    }

    private fun setInfoIcon(iconURL: String) {
        binding.typeET.enableInfoMode()
        binding.typeET.setInfoIcon(iconURL)
    }

}