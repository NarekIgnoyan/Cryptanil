package com.primesoft.cryptanil.utils.extensions

import android.app.Activity
import android.content.res.Resources
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.LayoutRes
import androidx.fragment.app.FragmentActivity
import com.jakewharton.rxbinding4.widget.textChanges
import com.primesoft.cryptanil.app
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.ObservableSource
import io.reactivex.rxjava3.functions.Consumer
import java.util.concurrent.TimeUnit

fun View.visible() {
    this.visibility = View.VISIBLE
}

fun View.visible(value: Boolean?) {
    if (value == true)
        this.visibility = View.VISIBLE
    else
        this.visibility = View.GONE
}

fun View.gone() {
    this.visibility = View.GONE
}

fun View.invisible() {
    this.visibility = View.INVISIBLE
}

fun Int.toPx(): Int {
    return (this * Resources.getSystem().displayMetrics.density).toInt()
}

fun FragmentActivity.hideSoftKeyboard(view: View?) {
    val imm = this.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    if (view != null) {
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }
}

fun ViewGroup.hideSoftKeyboard() {
    val imm = app.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager

    imm.hideSoftInputFromWindow(this.windowToken, 0)
}

fun EditText.setDebounce(onNext: Consumer<String>, DELAY: Int = 300) {
    this.textChanges()
        .debounce(DELAY.toLong(), TimeUnit.MILLISECONDS)
        .observeOn(AndroidSchedulers.mainThread())
        .doOnNext {
            onNext.accept(it.toString())
        }
        .subscribe()

}

fun ViewGroup.inflate(@LayoutRes layoutRes: Int, attachToRoot: Boolean = false): View {
    return LayoutInflater.from(this.context).inflate(layoutRes, this, attachToRoot)
}
