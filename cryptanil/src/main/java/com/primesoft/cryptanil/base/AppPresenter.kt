package com.primesoft.cryptanil.base

import android.os.Handler
import androidx.annotation.CallSuper
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter
import com.primesoft.cryptanil.api.ApiImplementation
import com.primesoft.cryptanil.api.ErrorKey
import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.app
import com.primesoft.cryptanil.models.Error
import com.primesoft.cryptanil.models.Message
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.models.Response
import com.primesoft.cryptanil.utils.ExceptionTracker
import com.primesoft.cryptanil.utils.NO_ERROR_MESSAGE
import com.primesoft.cryptanil.utils.NO_SUCCESS_MESSAGE
import com.primesoft.cryptanil.views.AppView
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.util.concurrent.atomic.AtomicInteger

abstract class AppPresenter<V : AppView> : MvpBasePresenter<V>() {

    private var activeRequests: AtomicInteger? = null
    private var disposables: ArrayList<Disposable>? = null

    private var orderInformationRunnable: Runnable? = null
    private var handler: Handler? = null

    private val repeatInterval: Long = 10000

    private var orderInformation: OrderInformation? = null

    @CallSuper
    open fun onCreate() {
        initVariables()
    }

    fun initVariables() {
        if (disposables == null) disposables = ArrayList()

        if (activeRequests == null) activeRequests = AtomicInteger()
    }

    fun makeRequest(
        obs: Observable<*>, requestType: RequestType, silent: Boolean = false
    ) {

        if (!checkAppIsOnline()) return

        if (!silent) view?.showLoading()

        val observable: Observable<Response<*>> = initApiObservable(obs as Observable<Response<*>>)

        activeRequests?.incrementAndGet()


        val disposable = observable.doAfterTerminate {
            if (activeRequests?.decrementAndGet() == 0) {
                view?.onAllRequestsFinished()
            }

        }.subscribeWith<DisposableObserver<Response<*>>>(object :
            DisposableObserver<Response<*>>() {

            override fun onError(e: Throwable) {
                ExceptionTracker.trackException(e)
                showErrorInCaseOfException(e)
            }

            override fun onComplete() {

            }

            override fun onNext(response: Response<*>) {
                if (checkResponseForError(response, requestType)) {
                    return
                }

                try {
                    checkResponseForMessage(response, requestType)
                    saveOrderInformation(response, requestType)
                    processResponse(response, requestType)
                } catch (e: Exception) {
                    ExceptionTracker.trackException(e)
                    showErrorInCaseOfException(e)
                }
            }
        })

        disposables?.add(disposable)
    }

    private fun initApiObservable(observable: Observable<Response<*>>): Observable<Response<*>> {
        return observable.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
    }

    private fun checkAppIsOnline(): Boolean {
        val isOnline = app.isOnline()

        if (!isOnline) view?.showOfflineDialog()

        return isOnline
    }

    open fun showErrorInCaseOfException(t: Throwable) {
        if (t is SocketTimeoutException || t is ConnectException) {
            view?.showTimeOutDialog()
        } else {
            view?.showUnknownError()
        }
    }

    private fun checkResponseForError(response: Response<*>, requestType: RequestType): Boolean {
        val hasError = response.hasError()

        if (hasError) {
            when (response.error?.messageKey) {
                ErrorKey.REJECT.getId() -> view?.showReject(
                    response.error ?: Error(NO_ERROR_MESSAGE)
                )
                else -> view?.showError(response.error ?: Error(NO_ERROR_MESSAGE), requestType)
            }
        }
        return hasError
    }

    open fun checkResponseForMessage(response: Response<*>, requestType: RequestType) {
        if (response.hasMessage()) view?.showMessage(
            response.result?.message ?: Message(
                NO_SUCCESS_MESSAGE
            ), requestType
        )
    }

    fun saveOrderInformation(response: Response<*>, requestType: RequestType) {
        if (requestType == RequestType.ORDER_INFORMATION) {
            orderInformation = response.result?.data as OrderInformation
        }
    }

    abstract fun processResponse(response: Response<*>, requestType: RequestType)

    fun closeCryptanil() {
        view?.closeApplication(orderInformation)
    }

    fun startOrderInformationUpdater() {
        orderInformationRunnable = object : Runnable {
            override fun run() {
                getOrderInformation()
                handler?.postDelayed(this, repeatInterval)
            }
        }
        handler = Handler()
        handler?.postDelayed(orderInformationRunnable!!, repeatInterval)
    }

    fun stopOrderInformationUpdater() {
        orderInformationRunnable?.let {
            handler?.removeCallbacks(it)
        }
    }

    fun getOrderInformation() {
        makeRequest(
            ApiImplementation.instance.getOrderInformation(app.token),
            RequestType.ORDER_INFORMATION,
            true
        )
    }

    open fun onDestroy() {
        stopOrderInformationUpdater()

        if (disposables != null) {
            for (disposable in disposables!!) {
                disposable.dispose()
            }
        }
    }

}