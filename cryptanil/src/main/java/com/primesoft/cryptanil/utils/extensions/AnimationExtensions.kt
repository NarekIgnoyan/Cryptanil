package com.primesoft.cryptanil.utils.extensions

import android.transition.ChangeBounds
import android.transition.Fade
import android.transition.TransitionManager
import android.transition.TransitionSet
import android.view.ViewGroup

fun ViewGroup.animateChangeBounds(duration: Long = 150, startDelay: Long = 0) {
    val transition = TransitionSet().addTransition(ChangeBounds()).addTransition(Fade())
    transition.excludeTarget(androidx.recyclerview.widget.RecyclerView::class.java, true)
    transition.duration = duration
    transition.startDelay = startDelay
    TransitionManager.beginDelayedTransition(this, transition)
}
