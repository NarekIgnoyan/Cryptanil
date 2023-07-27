package com.primesoft.cryptanil.base

import com.primesoft.cryptanil.api.ApiImplementation
import com.primesoft.cryptanil.api.ErrorKey
import com.primesoft.cryptanil.app
import com.primesoft.cryptanil.models.CreateOrder
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.models.Response
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.ExceptionTracker
import com.primesoft.cryptanil.utils.extensions.EMPTY_STRING
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.net.SocketTimeoutException

class CryptanilOrderAPI {

    private object Holder {
        val INSTANCE = CryptanilOrderAPI()
    }

    companion object {
        val instance: CryptanilOrderAPI by lazy { Holder.INSTANCE }
    }

    fun createOrder(
        onOrderCreated: ActionOne<String>,
        statusListener: ActionOne<CryptanilApiStatus>?
    ) {
        if (!checkAppIsOnline(statusListener))
            return

        ApiImplementation.instance.createOrder(CreateOrder())
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<Response<OrderInformation>?> {
                override fun onSubscribe(d: Disposable) {
                }

                override fun onNext(response: Response<OrderInformation>) {
                    if (checkResponseForError(response, statusListener))
                        return

                    try {
                        val orderInformation = response.result?.data as OrderInformation
                        onOrderCreated(orderInformation.orderKey ?: EMPTY_STRING)
                    } catch (e: Exception) {
                        ExceptionTracker.trackException(e)
                        showErrorInCaseOfException(e, statusListener)
                    }
                }

                override fun onError(t: Throwable) {
                    showErrorInCaseOfException(t, statusListener)
                }

                override fun onComplete() {
                }
            })
    }

    private fun checkAppIsOnline(statusListener: ActionOne<CryptanilApiStatus>?): Boolean {
        val isOnline = app.isOnline()

        if (!isOnline) {
            statusListener?.invoke(CryptanilApiStatus.NO_CONNECTION)
        }

        return isOnline
    }

    private fun showErrorInCaseOfException(
        t: Throwable,
        statusListener: ActionOne<CryptanilApiStatus>?
    ) {
        if (t is SocketTimeoutException || t is ConnectException) {
            statusListener?.invoke(CryptanilApiStatus.TIME_OUT)
        } else {
            statusListener?.invoke(CryptanilApiStatus.UNKNOWN)
        }
    }

    private fun checkResponseForError(
        response: Response<*>,
        statusListener: ActionOne<CryptanilApiStatus>?
    ): Boolean {
        val hasError = response.hasError()

        if (hasError) {
            when (response.error?.messageKey) {
                ErrorKey.REJECT.getId() -> {
                    statusListener?.invoke(CryptanilApiStatus.REJECT)
                }
                else -> {
                    statusListener?.invoke(CryptanilApiStatus.UNKNOWN)
                }
            }
        }
        return hasError
    }

}