package com.primesoft.cryptanil.base

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.databinding.LoadingLayoutBinding
import com.primesoft.cryptanil.enums.OrderStatus
import com.primesoft.cryptanil.models.Error
import com.primesoft.cryptanil.models.Message
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.utils.DATA_KEY
import com.primesoft.cryptanil.utils.extensions.*
import com.primesoft.cryptanil.views.AppView

abstract class AppActivity<V : AppView, P : AppPresenter<V>> : AppCompatActivity(),
    AppView {

    var presenter: P? = null
    private var loadingLayout: View? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        onCreatePresenter()
    }

    private fun onCreatePresenter() {
        presenter = createPresenter()
        presenter?.onCreate()
    }

    override fun setContentView(view: View?) {
        super.setContentView(view)
        view?.let {
            loadingLayout = LoadingLayoutBinding.bind(view.findViewById(R.id.loading)).loadingView
        }
    }

    override fun onAllRequestsFinished() {
        hideLoading()
    }

    override fun showError(error: Error, requestType: RequestType) {
        showErrorDialog(error.userMessage)
    }

    override fun showReject(error: Error) {
        showErrorDialog(error.userMessage) {
            finishApplication()
        }
    }

    override fun showMessage(message: Message, requestType: RequestType) {
        showSuccessDialog(message.userMessage)
    }

    override fun showOfflineDialog() {
        showErrorDialog(R.string.no_internet_connection)
    }

    override fun showTimeOutDialog() {
        showErrorDialog(R.string.timeout_error)
    }

    override fun showUnknownError() {
        showErrorDialog(R.string.something_went_wrong)
    }

    override fun showLoading() {
        loadingLayout?.visible()
    }

    override fun closeApplication(orderInformation: OrderInformation?) {
        val returnIntent = Intent()
        returnIntent.putExtra(DATA_KEY, orderInformation)

        if ((orderInformation?.status ?: UNDEFINED_INT) > OrderStatus.CREATED.getId()) {
            setResult(Activity.RESULT_OK, returnIntent)
        } else {
            setResult(Activity.RESULT_CANCELED, returnIntent)
        }

        finishApplication()
    }

    override fun hideLoading() {
        loadingLayout?.gone()
    }

    override fun attachBaseContext(newBase: Context?) {
        super.attachBaseContext(updateResources(newBase))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter?.onDestroy()
    }

    abstract fun createPresenter(): P

}