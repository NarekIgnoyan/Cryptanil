package com.primesoft.cryptanil.adapter.view_holders

import android.view.LayoutInflater
import android.view.View
import com.primesoft.cryptanil.databinding.InfoLayoutBinding
import com.primesoft.cryptanil.models.InfoItem
import com.primesoft.cryptanil.utils.extensions.drawables
import com.primesoft.cryptanil.utils.extensions.with

class InfoVH(inflater: LayoutInflater?, var infoItem: InfoItem) {

    val binding = inflater?.let { InfoLayoutBinding.inflate(it) }

    val view: View? = binding?.root

    init {
        setUpViewHolder()
    }

    private fun setUpViewHolder() {
        binding?.with {
            infoIV.setImageDrawable(drawables(infoItem.resID))
            textTV.text = infoItem.text
            titleTV.text = infoItem.title
        }
    }
}