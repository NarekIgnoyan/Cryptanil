package com.primesoft.cryptanil.adapter.view_holders

import android.net.Uri
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.databinding.TypeRowLayoutBinding
import com.primesoft.cryptanil.models.TypeItem
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.extensions.drawables
import com.primesoft.cryptanil.utils.extensions.toPx
import com.primesoft.cryptanil.utils.extensions.visible

class TypeVH(itemView: View, var itemClickListener: ActionOne<TypeItem>?) : ViewHolder(itemView) {

    private val binding = TypeRowLayoutBinding.bind(itemView)

    private var params: ViewGroup.MarginLayoutParams = ViewGroup.MarginLayoutParams(
        ViewGroup.LayoutParams.MATCH_PARENT,
        ViewGroup.LayoutParams.WRAP_CONTENT
    )

    fun setUp(item: TypeItem, first: Boolean, last: Boolean) {
        binding.typeTV.text = item.getType()
        GlideToVectorYou
            .init()
            .with(binding.root.context)
            .load(Uri.parse(item.getIconURL()), binding.typeIV)
        setUpItemShape(first, last)
        itemView.setOnClickListener {
            itemClickListener?.invoke(item)
        }
    }

    private fun setUpItemShape(first: Boolean, last: Boolean) {
        val itemShape =
            if (first && last) {
                params.setMargins(20.toPx(), 35.toPx(), 20.toPx(), 35.toPx())
                drawables(R.drawable.search_item_round_shape)
            } else if (first) {
                params.setMargins(20.toPx(), 35.toPx(), 20.toPx(), 0)
                drawables(R.drawable.search_item_top_shape)
            } else if (last) {
                params.setMargins(20.toPx(), 0, 20.toPx(), 35.toPx())
                drawables(R.drawable.search_item_bottom_shape)
            } else {
                params.setMargins(20.toPx(), 0, 20.toPx(), 0)
                drawables(R.drawable.search_item_middle_shape)
            }

        binding.divider.visible(!last)
        itemView.background = itemShape
        itemView.layoutParams = params
    }

}