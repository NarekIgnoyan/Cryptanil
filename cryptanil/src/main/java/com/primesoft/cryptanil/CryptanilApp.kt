package com.primesoft.cryptanil

import android.app.Activity
import android.content.Intent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import com.primesoft.cryptanil.base.CryptanilApiStatus
import com.primesoft.cryptanil.base.CryptanilOrderAPI
import com.primesoft.cryptanil.enums.Language
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.CRYPTANIL_REQUEST_CODE
import com.primesoft.cryptanil.utils.DATA_KEY
import com.primesoft.cryptanil.utils.extensions.getIntent
import com.primesoft.cryptanil.utils.extensions.openActivity
import com.primesoft.cryptanil.utils.extensions.serializable

object CryptanilApp : java.io.Serializable {

    fun start(
        activity: Activity,
        language: Language,
        token: String,
        requestCode: Int = CRYPTANIL_REQUEST_CODE
    ) {
        app.language = language.getId()
        app.token = token
        activity.openActivity<CryptanilActivity>(requestCode)
    }

    fun start(
        fragment: Fragment,
        language: Language,
        token: String,
        requestCode: Int = CRYPTANIL_REQUEST_CODE
    ) {
        app.language = language.getId()
        app.token = token
        fragment.openActivity<CryptanilActivity>(requestCode)
    }

    fun createOrder(
        appKey: String?,
        onOrderCreated: ActionOne<String>,
        statusListener: ActionOne<CryptanilApiStatus>
    ) {
        CryptanilOrderAPI.instance.createOrder(onOrderCreated, statusListener)
    }

    fun createIntent(
        context: FragmentActivity,
        language: Language,
        token: String
    ): Intent {
        val cryptanilIntent = context.getIntent<CryptanilActivity>()
        app.language = language.getId()
        app.token = token
        return cryptanilIntent
    }

    fun getOrderInformation(data: Intent?): OrderInformation? {
        if (data?.extras != null) {
            return data.extras?.serializable(DATA_KEY)
        }

        return null
    }

}