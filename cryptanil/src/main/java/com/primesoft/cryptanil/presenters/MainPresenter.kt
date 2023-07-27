package com.primesoft.cryptanil.presenters

import androidx.fragment.app.FragmentManager
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.api.RequestType
import com.primesoft.cryptanil.base.AppPresenter
import com.primesoft.cryptanil.enums.OrderStatus
import com.primesoft.cryptanil.fragments.SearchFragment
import com.primesoft.cryptanil.fragments.StatusFragment
import com.primesoft.cryptanil.fragments.TransactionFragment
import com.primesoft.cryptanil.models.*
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.extensions.getFragmentTag
import com.primesoft.cryptanil.utils.extensions.openFragment
import com.primesoft.cryptanil.utils.extensions.tryTo
import com.primesoft.cryptanil.utils.fragments.FragmentsController
import com.primesoft.cryptanil.views.MainView

class MainPresenter : AppPresenter<MainView>() {

    override fun onCreate() {
        super.onCreate()
        getOrderInformation()
    }

    override fun processResponse(response: Response<*>, requestType: RequestType) {
        when (requestType) {
            RequestType.ORDER_INFORMATION -> processOrderInformation(response.result?.data as OrderInformation)
            else -> {}
        }
    }

    private fun processOrderInformation(orderInformation: OrderInformation) {
        when (OrderStatus.getById(orderInformation.status)) {
            OrderStatus.CREATED, OrderStatus.PARTLY_COMPLETED -> view?.openTransactionFragment(
                orderInformation.isTesting ?: false,
                orderInformation.getCompanyInformation()
            )
            else -> view?.openStatusFragment(orderInformation)

        }
    }

    fun openTransactionFragment(
        manager: FragmentManager,
        testing: Boolean,
        companyInformation: CompanyInformation
    ) {
        manager.openFragment(
            TransactionFragment.getInstance(testing, companyInformation),
            R.id.fragmentContainer,
            false
        )
    }

    fun openSearchFragment(
        manager: FragmentManager,
        coins: ArrayList<Coin>?,
        selectListener: ActionOne<Coin>
    ) {
        val searchFragment = SearchFragment.getInstance(coins as ArrayList<TypeItem>)
        searchFragment.setTypeSelectListener {
            selectListener.invoke(it as Coin)
        }
        manager.openFragment(searchFragment, R.id.fragmentContainer)
    }

    fun openStatusFragment(manager: FragmentManager, orderInformation: OrderInformation) {
        tryTo {
            FragmentsController.removeFragment(
                manager, getFragmentTag<TransactionFragment>()
            )
        }
        manager.openFragment(
            StatusFragment.getInstance(orderInformation), R.id.fragmentContainer, false
        )
    }

}