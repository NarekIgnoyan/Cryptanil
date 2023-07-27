package com.primesoft.cryptanil.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.adapter.view_holders.TypeVH
import com.primesoft.cryptanil.models.TypeItem
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.extensions.inflate

class TypeAdapter(private val itemClickListener: ActionOne<TypeItem>? = null) :
    RecyclerView.Adapter<TypeVH>() {

    private var data: ArrayList<TypeItem> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TypeVH {
        return TypeVH(parent.inflate(R.layout.type_row_layout), itemClickListener)
    }

    override fun onBindViewHolder(holder: TypeVH, position: Int) {
        holder.setUp(data[position], position == 0, position == data.size - 1)
    }

    fun setData(data: ArrayList<TypeItem>?) {
        this.data.clear()
        this.data.addAll(data ?: ArrayList())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int = data.size

}