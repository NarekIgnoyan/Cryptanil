package com.primesoft.cryptanil.utils.spinner_adapter

import android.net.Uri
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.models.TypeItem

class SpinnerVH(
    var rootView: View? = null,
    var parent: View? = null
) {

    fun setUp(option: TypeItem) {
        rootView?.findViewById<TextView>(R.id.spinnerTV)?.text = option.getType()
        val infoIV = rootView?.findViewById<ImageView>(R.id.spinnerIV)

        GlideToVectorYou
            .init()
            .with(rootView?.context)
            .load(Uri.parse(option.getIconURL()), infoIV)
    }
}
