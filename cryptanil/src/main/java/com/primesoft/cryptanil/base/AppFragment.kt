package com.primesoft.cryptanil.base

import android.content.Context
import android.os.Bundle
import android.view.View
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.models.Error
import com.primesoft.cryptanil.models.Message
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.utils.extensions.finishApplication
import com.primesoft.cryptanil.utils.extensions.hideSoftKeyboard
import com.primesoft.cryptanil.utils.extensions.showErrorDialog
import com.primesoft.cryptanil.utils.extensions.showSuccessDialog
import com.primesoft.cryptanil.views.AppView
import com.primesoft.cryptanil.views.MainView
import com.primesoft.cryptanil.views.NavigationView

abstract class AppFragment<V : AppView, P : AppPresenter<V>>(id: Int) : AppMvpFragment<V, P>(id),
    AppView {

    private var mainView: MainView? = null
    private var navigationView: NavigationView? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        mainView = context as MainView
        navigationView = context
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        presenter.onCreate()
    }

    abstract override fun createPresenter(): P

    override fun showLoading() {
        mainView?.showLoading()
    }

    override fun hideLoading() {
        mainView?.hideLoading()
    }

    override fun showError(error: Error, requestType: RequestType) {
        activity?.showErrorDialog(error.userMessage)
    }

    override fun showReject(error: Error) {
        activity?.showErrorDialog(error.userMessage) {
            activity?.finishApplication()
        }
    }

    override fun showMessage(message: Message, requestType: RequestType) {
        activity?.showSuccessDialog(message.userMessage)
    }

    override fun showOfflineDialog() {
        activity?.showErrorDialog(R.string.no_internet_connection)
    }

    override fun showTimeOutDialog() {
        activity?.showErrorDialog(R.string.timeout_error)
    }

    override fun showUnknownError() {
        activity?.showErrorDialog(R.string.something_went_wrong)
    }

    override fun onAllRequestsFinished() {
        hideLoading()
    }

    fun getNavigation() = navigationView

    override fun onResume() {
        super.onResume()
        view?.requestFocus()
        activity?.hideSoftKeyboard(view)
    }

    override fun closeApplication(orderInformation: OrderInformation?) {
        mainView?.closeApplication(orderInformation)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.onDestroy()
    }

}