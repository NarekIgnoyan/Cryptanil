package com.primesoft.cryptanil.views

import com.primesoft.cryptanil.models.Coin
import com.primesoft.cryptanil.models.CompanyInformation
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.utils.ActionOne

interface NavigationView {
    fun openTransactionFragment(testing: Boolean, companyInformation: CompanyInformation)
    fun openSearchFragment(coins: ArrayList<Coin>?, selectListener: ActionOne<Coin>)
    fun openStatusFragment(orderInformation: OrderInformation)
    fun closeApplication(orderInformation: OrderInformation?)
}