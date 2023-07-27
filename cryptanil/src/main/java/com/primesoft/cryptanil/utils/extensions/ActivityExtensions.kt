package com.primesoft.cryptanil.utils.extensions

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.activity.ComponentActivity
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.viewbinding.ViewBinding
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.utils.FragmentAutoClearedValueBinding
import com.primesoft.cryptanil.utils.fragments.FragmentsController
import java.io.Serializable

inline fun <T : ViewBinding> ComponentActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun FragmentActivity.finishApplication() {
    this.finish()
}

fun <T : ViewBinding> Fragment.viewBinding(
    binding: (View) -> T
) = FragmentAutoClearedValueBinding(binding)

inline fun <reified T : Fragment> getFragmentTag(): String = T::class.java.name

fun FragmentManager.openFragment(
    fragment: Fragment,
    layoutId: Int,
    addToBackStack: Boolean = true
) {
    FragmentsController.addFragment(
        this,
        layoutId,
        fragment,
        fragment::class.java.name,
        withAnimation = true, forceAdd = false, addToBackStack = addToBackStack
    )
}

inline fun <reified T : AppCompatActivity> Activity.openActivity() {
    val intent = newIntent<T>(this)
    startActivity(intent)
}

inline fun <reified T : AppCompatActivity> Activity.getIntent(): Intent =
    newIntent<T>(this)

inline fun <reified T : AppCompatActivity> newIntent(context: Context): Intent =
    Intent(context, T::class.java)

inline fun <reified T : AppCompatActivity> newIntent(context: FragmentActivity?): Intent =
    Intent(context, T::class.java)


inline fun <reified T : AppCompatActivity> Activity.openActivity(
    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    startActivityForResult(intent, requestCode, options)
}

inline fun <reified T : AppCompatActivity> Fragment.openActivity(

    requestCode: Int = -1,
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this.activity)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivityForResult(intent, requestCode, options)
    } else {
        startActivityForResult(intent, requestCode)
    }
    activity?.overridePendingTransition(R.anim.activity_anim_open, R.anim.activity_anim_close)
}

inline fun <reified T : AppCompatActivity> Context.openActivity(
    options: Bundle? = null,
    noinline init: Intent.() -> Unit = {}
) {
    val intent = newIntent<T>(this)
    intent.init()
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
        startActivity(intent, options)
    } else {
        startActivity(intent)
    }
}

inline fun <reified T : Serializable> Bundle.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializable(key, T::class.java)
    else -> @Suppress("DEPRECATION") getSerializable(key) as? T
}

inline fun <reified T : Serializable> Intent.serializable(key: String): T? = when {
    Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU -> getSerializableExtra(
        key,
        T::class.java
    )
    else -> @Suppress("DEPRECATION") getSerializableExtra(key) as? T
}

fun FragmentActivity.openChromeTab(url: String?) {
    if (url == null || url.isEmpty())
        return

    val builder = CustomTabsIntent.Builder()
    builder.setToolbarColor(ContextCompat.getColor(this, R.color.colorPrimary))
    builder.setStartAnimations(this, R.anim.activity_anim_open, R.anim.activity_anim_close)
    builder.setExitAnimations(this, R.anim.activity_anim_open, R.anim.activity_anim_close)
    val customTabsIntent = builder.build()
    customTabsIntent.launchUrl(this, Uri.parse(url))
}

