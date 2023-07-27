package com.primesoft.cryptanil.app_views

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.widget.SpinnerAdapter
import androidx.appcompat.widget.AppCompatSpinner

class SelectionSpinner(context: Context, attrs: AttributeSet?) : AppCompatSpinner(context, attrs) {

    private var listener: OnItemSelectedListener? = null
    private var mAdapter: SpinnerAdapter? = null
    private var mValue = false

    fun setValueChecker(mValue: Boolean) {
        this.mValue = mValue
    }

    override fun setSelection(position: Int) {
        super.setSelection(position)
        if (listener != null) listener!!.onItemSelected(null, null, position, 0)
    }

    fun setOnItemSelectedEvenIfUnchangedListener(
        listener: OnItemSelectedListener?
    ) {
        this.listener = listener
    }

    override fun setAdapter(adapter: SpinnerAdapter) {
        super.setAdapter(adapter)
        mAdapter = adapter
    }

    override fun onViewAdded(child: View?) {
        super.onViewAdded(child)
        if (mValue) {
            if (mAdapter!!.count == 1 && listener != null) {
                setSelection(0)
                listener!!.onItemSelected(null, null, 0, 0)
            }
        }
    }

    public override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
    }

}