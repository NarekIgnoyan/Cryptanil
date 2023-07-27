package com.primesoft.cryptanil.utils.extensions

import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import com.primesoft.cryptanil.app
import com.primesoft.cryptanil.enums.Language
import java.util.*

fun updateResources(context: Context?): Context? {
    var mContext = context
//    val locale = Locale(app.resources.configuration.locale.language ?: (Language.EN.toString()))
    val locale = Locale(app.language ?: (Language.EN.getId()))
//    Locale.setDefault(locale)
    val res = context?.resources
    val config = Configuration(res?.configuration)
    config.setLocale(locale)
    mContext = context?.createConfigurationContext(config)
    return mContext
}

fun setLanguage(language: Language) {
    val locale = Locale(language.getId())
    app.language = language.getId()
//    Locale.setDefault(locale)
    val res: Resources = app.resources
    val config = Configuration(res.configuration)
    config.locale = locale
    res.updateConfiguration(config, res.displayMetrics)
}
