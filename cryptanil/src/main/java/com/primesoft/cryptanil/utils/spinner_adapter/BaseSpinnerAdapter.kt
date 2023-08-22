package com.primesoft.cryptanil.utils.spinner_adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.SpinnerAdapter
import androidx.annotation.LayoutRes

abstract class BaseSpinnerAdapter<D, H>(var context: Context, var data: List<D>) : BaseAdapter(),
    SpinnerAdapter {

    override fun getCount(): Int = data.size

    override fun getItem(position: Int): D = data[position]

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View =
        getDropDownView(position, convertView, parent)

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val row: View
        val viewHolder: H
        if (convertView == null) {
            val layoutInflater =
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
            row = layoutInflater.inflate(getLayoutResId(), null)
            viewHolder = onCreateViewHolder(row, parent, position)
            row.tag = viewHolder
        } else {
            row = convertView
            viewHolder = row.tag as H
        }
        onBindView(viewHolder, position)
        return row
    }


    abstract fun onCreateViewHolder(rowView: View?, parent: ViewGroup?, position: Int): H

    abstract fun onBindView(viewHolder: H, position: Int)

    @LayoutRes
    abstract fun getLayoutResId(): Int
}