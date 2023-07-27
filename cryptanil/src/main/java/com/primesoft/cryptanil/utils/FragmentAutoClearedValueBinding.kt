package com.primesoft.cryptanil.utils

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentAutoClearedValueBinding<T : ViewBinding>(
    val binding: (View) -> T
) : ReadOnlyProperty<Fragment, T>,
    DefaultLifecycleObserver {

    private var value: T? = null

    override fun onDestroy(owner: LifecycleOwner) {
        value = null
    }

    override fun getValue(thisRef: Fragment, property: KProperty<*>): T {
        return value ?: binding(thisRef.requireView()).also {
            setValue(thisRef, it)
        }
    }

    private fun setValue(fragment: Fragment, value: T) {
        fragment.lifecycle.removeObserver(this)
        this.value = value
        fragment.lifecycle.addObserver(this)
    }

}