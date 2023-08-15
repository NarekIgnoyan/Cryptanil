package com.primesoft.cryptanil

import android.os.Bundle
import android.window.OnBackInvokedDispatcher
import com.primesoft.cryptanil.base.AppActivity
import com.primesoft.cryptanil.databinding.ActivityCryptanilBinding
import com.primesoft.cryptanil.models.Coin
import com.primesoft.cryptanil.models.CompanyInformation
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.presenters.MainPresenter
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.extensions.hideSoftKeyboard
import com.primesoft.cryptanil.utils.extensions.viewBinding
import com.primesoft.cryptanil.utils.fragments.FragmentsController
import com.primesoft.cryptanil.views.MainView

class CryptanilActivity : AppActivity<MainView, MainPresenter>(), MainView {

    private val binding by viewBinding(ActivityCryptanilBinding::inflate)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

    override fun openTransactionFragment(testing: Boolean, companyInformation: CompanyInformation) {
        presenter?.openTransactionFragment(supportFragmentManager, testing, companyInformation)
    }

    override fun openSearchFragment(coins: ArrayList<Coin>?, selectListener: ActionOne<Coin>) {
        presenter?.openSearchFragment(supportFragmentManager, coins) {
            selectListener.invoke(it)
            binding.root.hideSoftKeyboard()
            onBackPressedDispatcher.onBackPressed()
        }
    }

    override fun openStatusFragment(orderInformation: OrderInformation) {
        presenter?.openStatusFragment(supportFragmentManager, orderInformation)
    }

    override fun createPresenter() = MainPresenter(this)

    override fun getOnBackInvokedDispatcher(): OnBackInvokedDispatcher {
        if (!FragmentsController.haveBackStack(supportFragmentManager)) {
            presenter?.closeCryptanil()
            return super.getOnBackInvokedDispatcher()
        } else {
            return super.getOnBackInvokedDispatcher()
        }
    }
}