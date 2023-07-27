package com.primesoft.cryptanil.utils.extensions

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.github.alexzhirkevich.customqrgenerator.QrData
import com.github.alexzhirkevich.customqrgenerator.vector.QrCodeDrawable
import com.github.alexzhirkevich.customqrgenerator.vector.createQrVectorOptions
import com.github.alexzhirkevich.customqrgenerator.vector.style.*
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.app
import com.primesoft.cryptanil.utils.StringUtils
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.*

const val UNDEFINED = -1L
const val UNDEFINED_INT = -1
const val UNDEFINED_DOUBLE = -1.0
const val UNDEFINED_STRING = "UNDEFINED"
const val EMPTY_STRING = ""

fun <T, R> T.tryTo(transform: T.() -> R, failure: ((Throwable) -> Unit)?) = try {
    transform.invoke(this)
} catch (t: Throwable) {
    t.trackException()
    failure?.invoke(t)
    null
}

fun <T : Any> T.with(block: T.() -> Unit): Unit = block(this)

fun <T, R> T.tryTo(transform: T.() -> R) = tryTo(transform, {})

inline fun Boolean.ifTrue(block: () -> Unit): Boolean {
    if (this) {
        block()
    }
    return this
}

fun colors(resId: Int) = ContextCompat.getColor(app, resId)

fun drawables(resId: Int) = ContextCompat.getDrawable(app, resId)

fun Double.formatDecimal(): String {
    val df = DecimalFormat("0.###")
    return StringUtils.replaceCommas(df.format(this))
}

fun Double.formatCryptoDecimal(): String {
    val df = DecimalFormat("0.####")
    return StringUtils.replaceCommas(df.format(this))
}

fun Double.isDouble(): Boolean {
    return this - this.toInt() != 0.0
}

fun Int.addCommas(): String = try {
    var originalString: String = this.toString()
    if (originalString.contains(",")) {
        originalString = originalString.replace(",".toRegex(), "")
    }
    val longVal: Long = originalString.toLong()
    val formatter = NumberFormat.getInstance(Locale.US) as DecimalFormat
    formatter.applyPattern("#,###,###,###")
    formatter.format(longVal)
} catch (nfe: NumberFormatException) {
    nfe.printStackTrace()
    this.toString()
}

fun String.copy() {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
        val clipboard =
            app.getSystemService(Context.CLIPBOARD_SERVICE) as android.text.ClipboardManager
        clipboard.text = this
    } else {
        val clipboard = app.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
        val clip = ClipData.newPlainText("", this)
        clipboard.setPrimaryClip(clip)
    }

    toast(app.getString(R.string.copied))
}

fun createQrDrawable(qrData: String?, centerDrawable: Drawable): Drawable {
    val data = QrData.Url(qrData ?: EMPTY_STRING)

    val options = createQrVectorOptions {
        padding = 0f

        logo {
            drawable = centerDrawable
            size = .37f
            padding = QrVectorLogoPadding.Natural(0f)
            shape = QrVectorLogoShape
                .Circle
        }
        colors {
            dark = QrVectorColor
                .Solid(colors(R.color.white))

        }
        shapes {
            darkPixel = QrVectorPixelShape
                .Circle()
            ball = QrVectorBallShape
                .RoundCorners(.20f)
            frame = QrVectorFrameShape
                .RoundCorners(.20f)
        }
    }
    return QrCodeDrawable(data, options)
}

fun toast(message: String?, duration: Int = Toast.LENGTH_SHORT) {
    Toast.makeText(app, message, duration).show()
}





