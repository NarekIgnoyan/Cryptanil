package com.primesoft.cryptanil.utils.spinner_adapter

import android.content.Context
import android.view.View
import android.view.ViewGroup
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.models.TypeItem

class AppSpinnerAdapter(
    context: Context,
    data: ArrayList<TypeItem>,
    private val itemClickListener: (TypeItem, Int) -> Unit
) : BaseSpinnerAdapter<TypeItem, SpinnerVH>(context, data) {

    private var selectedPosition = 0


    override fun onCreateViewHolder(rowView: View?, parent: ViewGroup?, position: Int): SpinnerVH {
        return SpinnerVH(rowView, parent)

    }

    override fun onBindView(viewHolder: SpinnerVH, position: Int) {
        viewHolder.rootView?.setOnClickListener {
            selectedPosition = position
            itemClickListener.invoke(data[position], position)
        }
        viewHolder.setUp(data[position])
    }

    override fun getLayoutResId(): Int = R.layout.app_spinner_row_layout

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    fun setSelectedPosition(selectedPosition: Int) {
        this.selectedPosition = selectedPosition
    }

    fun getSelectedOption(): TypeItem = data[selectedPosition]
}