package com.primesoft.cryptanil.utils.extensions

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.TextView

fun TextView.underline() {
    val content = SpannableString(this.text)
    content.setSpan(UnderlineSpan(), 0, this.text.toString().length, 0)
    this.text = content
}

