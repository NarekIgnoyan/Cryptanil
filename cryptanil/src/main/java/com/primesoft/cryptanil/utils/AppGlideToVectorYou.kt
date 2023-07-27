package com.primesoft.cryptanil.utils

import android.app.Activity
import android.content.Context
import android.graphics.drawable.Drawable
import android.graphics.drawable.PictureDrawable
import android.net.Uri
import android.widget.ImageView
import com.bumptech.glide.RequestBuilder
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import com.github.twocoffeesoneteam.glidetovectoryou.GlideApp
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYouListener
import com.github.twocoffeesoneteam.glidetovectoryou.SvgDecoder

class AppGlideToVectorYou {

    private var requestBuilder: RequestBuilder<PictureDrawable>? = null
    private var placeHolderLoading = -1
    private var placeHolderError = -1

    companion object {
        private var instance: AppGlideToVectorYou? = null

        fun init(): AppGlideToVectorYou {
            if (instance == null)
                instance = AppGlideToVectorYou()

            return instance!!
        }
    }

    fun with(ctx: Context): AppGlideToVectorYou {
        createRequestBuilder(ctx)
        return instance!!
    }

    fun withListener(listener: GlideToVectorYouListener): AppGlideToVectorYou {
        requestBuilder?.listener(AppSvgSoftwareLayerSetter(listener))
        return instance!!
    }

    fun setPlaceHolder(placeHolderLoading: Int, placeHolderError: Int): AppGlideToVectorYou {
        this.placeHolderError = placeHolderError
        this.placeHolderLoading = placeHolderLoading
        return instance!!
    }

    fun load(uri: Uri, imageView: ImageView?) {
        if (placeHolderLoading != -1 && placeHolderError != -1) {
            requestBuilder?.apply(
                RequestOptions()
                    .placeholder(placeHolderLoading)
                    .error(placeHolderError)
            )
        }
        requestBuilder?.load(uri)?.into(imageView!!)
    }

    fun justLoadImage(activity: Activity, uri: Uri, imageView: ImageView?) {
        GlideApp.with(activity)
            .`as`(PictureDrawable::class.java)
            .listener(AppSvgSoftwareLayerSetter()).load(uri).into(imageView!!)
    }

    fun justLoadAsDrawable(activity: Activity, uri: Uri, drawableCallback: ActionOne<Drawable>) {
        GlideApp.with(activity).asDrawable().load(uri).into(object : SimpleTarget<Drawable>() {
            override fun onResourceReady(resource: Drawable, transition: Transition<in Drawable>?) {
                drawableCallback(resource)
            }
        })
    }

    fun getRequestBuilder(): RequestBuilder<PictureDrawable>? {
        return requestBuilder
    }

    private fun createRequestBuilder(ctx: Context) {
        requestBuilder = GlideApp.with(ctx)
            .`as`(PictureDrawable::class.java)
            .diskCacheStrategy(DiskCacheStrategy.DATA)
            .listener(AppSvgSoftwareLayerSetter())
            .decode(SvgDecoder::class.java)
    }

}