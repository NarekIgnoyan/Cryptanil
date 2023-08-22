package com.primesoft.cryptanil.utils

import android.graphics.drawable.PictureDrawable
import android.widget.ImageView
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.ImageViewTarget
import com.bumptech.glide.request.target.Target
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener

class AppSvgSoftwareLayerSetter( var customListener: GlideToVectorYouListener?=null) : RequestListener<PictureDrawable> {

    override fun onLoadFailed(
        e: GlideException?,
        model: Any?,
        target: Target<PictureDrawable>?,
        isFirstResource: Boolean
    ): Boolean {
        val view = (target as ImageViewTarget<*>).view
        view.setLayerType(ImageView.LAYER_TYPE_NONE, null)

        if (customListener != null)
            customListener!!.onLoadFailed()

        return false
    }

    override fun onResourceReady(
        resource: PictureDrawable?,
        model: Any?,
        target: Target<PictureDrawable>?,
        dataSource: DataSource?,
        isFirstResource: Boolean
    ): Boolean {
        val view = (target as ImageViewTarget<*>).view
        view.setLayerType(ImageView.LAYER_TYPE_SOFTWARE, null)

        if (customListener != null) {
            customListener!!.onResourceReady()
        }

        return false
    }

}