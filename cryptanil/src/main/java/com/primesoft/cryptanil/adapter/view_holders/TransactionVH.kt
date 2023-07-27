package com.primesoft.cryptanil.adapter.view_holders

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import com.github.twocoffeesoneteam.glidetovectoryou.GlideToVectorYou
import com.primesoft.cryptanil.databinding.TransactionRowLayoutBinding
import com.primesoft.cryptanil.models.OrderTransaction
import com.primesoft.cryptanil.utils.ActionOne
import com.primesoft.cryptanil.utils.extensions.with

class TransactionVH(inflater: LayoutInflater?, var transaction: OrderTransaction) {

    val binding = inflater?.let { TransactionRowLayoutBinding.inflate(it) }

    val view: View? = binding?.root

    init {
        setUpViewHolder()
    }

    private fun setUpViewHolder() {
        binding?.with {
            coinTV.text = transaction.valueDecimal.toString() + " " + transaction.coinType
            networkTV.text = transaction.networkName
            hashTV.text = transaction.txId

            GlideToVectorYou
                .init()
                .with(view?.context)
                .load(Uri.parse(transaction.coinIconUrl), coinIV)

            GlideToVectorYou
                .init()
                .with(view?.context)
                .load(Uri.parse(transaction.networkIconUrl), networkIV)
        }
    }

    fun setTransactionClickListener(clickCallback: ActionOne<String?>) {
        view?.setOnClickListener {
            clickCallback(transaction.getNetworkDetailsURL())
        }
    }

}