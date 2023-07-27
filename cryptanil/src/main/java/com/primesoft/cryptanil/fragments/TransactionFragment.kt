package com.primesoft.cryptanil.fragments

import android.graphics.Color
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.adapter.view_holders.TransactionVH
import com.primesoft.cryptanil.base.AppFragment
import com.primesoft.cryptanil.databinding.TransactionFragmentLayoutBinding
import com.primesoft.cryptanil.models.*
import com.primesoft.cryptanil.presenters.TransactionPresenter
import com.primesoft.cryptanil.utils.AppGlideToVectorYou
import com.primesoft.cryptanil.utils.DATA_KEY
import com.primesoft.cryptanil.utils.DATA_KEY_2
import com.primesoft.cryptanil.utils.TEST_TX_ID
import com.primesoft.cryptanil.utils.extensions.*
import com.primesoft.cryptanil.views.TransactionView


class TransactionFragment :
    AppFragment<TransactionView, TransactionPresenter>(R.layout.transaction_fragment_layout),
    TransactionView {

    private val binding by viewBinding(TransactionFragmentLayoutBinding::bind)

    private var testing = false
    private var companyInformation: CompanyInformation? = null

    companion object {
        fun getInstance(testing: Boolean, companyInformation: CompanyInformation) =
            TransactionFragment().apply {
                arguments = Bundle().apply {
                    putBoolean(DATA_KEY, testing)
                    putSerializable(DATA_KEY_2, companyInformation)
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testing = arguments?.getBoolean(DATA_KEY) ?: false
        companyInformation = arguments?.serializable(DATA_KEY_2)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpCompanyInformation()
        setClickListeners()
        setTxIdListener()
        setUpTesting()
    }

    private fun setUpCompanyInformation() {
        companyInformation?.let {
            presenter.selectedCoin = it.coinType
            presenter.selectedNetwork = it.networkName
            Glide.with(this).load(it.companyIconUrl).into(binding.companyIV)
            binding.companyNameTV.text = it.companyName
            binding.amountTV.text = "${it.requiredAmount} ${it.currencyCode}"
            binding.companyLayout.setBackgroundColor(Color.parseColor(it.invoiceColor))
        }
    }

    private fun setUpTesting() {
        testing.ifTrue {
            binding.txIdEt.setText(TEST_TX_ID)
            binding.submitBT.gone()
            binding.txIdEt.gone()
            binding.simulateBT.visible()
        }
    }

    private fun setClickListeners() {
        binding.with {
            submitBT.setOnClickListener {
                presenter.submitOrder(txIdEt.getText())
            }
            simulateBT.setOnClickListener {
                submitBT.performClick()
            }
            toolbar.onActionClicked {
                presenter.closeCryptanil()
            }
        }
    }

    override fun processWalletInformation(
        coins: ArrayList<Coin>?,
        defaultCoin: Coin?,
        networks: ArrayList<Network>?,
        defaultNetwork: Network?
    ) {
        setUpCoinSpinner(coins, defaultCoin)
        setUpNetworkSpinner(networks, defaultNetwork)
    }

    override fun processCoinAddress(coinAddress: CoinAddress) {
        binding.addressHolder.visible()
        coinAddress.with {
            binding.addressET.setText(address)
            generateQRCode(address)
            binding.memoET.visible(tag?.isNotEmpty())
            binding.memoET.setText(tag)
        }

        binding.root.animateChangeBounds()
    }

    override fun setUpRemainingAmount(orderRequiredAmount: OrderRequiredAmount) {
        binding.requiredTitleTV.visible()
        binding.requiredAmountTV.visible()
        binding.requiredAmountTV.text = orderRequiredAmount.getRemainingAmount()
        binding.requiredAmountTV.setOnClickListener {
            orderRequiredAmount.getCoinAmount().copy()
        }
    }

    override fun setUpCoinUI(isBinance: Boolean) {
        binding.binanceHolder.visible(isBinance)
        binding.binanceWarningTV.visible(isBinance)
        binding.tronProgressBar.visible(!isBinance)
        binding.root.animateChangeBounds()
    }

    override fun showTransactions(transactions: ArrayList<OrderTransaction>?) {
        disableSpinners()
        binding.transactionsTV.visible()

        transactions?.let {
            if (binding.transactionsHolder.childCount != it.count()) {
                binding.transactionsHolder.removeAllViews()
                it.forEach { transaction ->
                    addTransaction(transaction)
                }
                getCoinAddress()
            }
        }
    }

    private fun addTransaction(transaction: OrderTransaction) {
        val holder = TransactionVH(activity?.layoutInflater, transaction)
        holder.setTransactionClickListener { activity?.openChromeTab(it) }
        binding.transactionsHolder.addView(holder.view)
    }

    override fun onOrderSubmitted(orderInformation: OrderInformation) {
        getNavigation()?.openStatusFragment(orderInformation)
    }

    override fun onProgressChanged(progressStatus: Int) {
        binding.tronProgressBar.setProgress(progressStatus)
    }

    override fun disableSpinners() {
        binding.cryptoTypeSpinner.disable()
        binding.networkSpinner.disable()
    }

    private fun setUpCoinSpinner(coins: ArrayList<Coin>?, defaultCoin: Coin?) {
        binding.cryptoTypeSpinner.initSpinner(coins as ArrayList<TypeItem>)
        binding.cryptoTypeSpinner.select(defaultCoin)
        binding.cryptoTypeSpinner.setClickListener {
            getNavigation()?.openSearchFragment(coins) {
                binding.cryptoTypeSpinner.select(it)
                setUpNetworkSpinner(it.networkList, it.getDefaultNetwork())
            }
        }
    }

    private fun setUpNetworkSpinner(networks: ArrayList<Network>?, defaultNetwork: Network?) {
        binding.networkSpinner.initSpinner(networks as ArrayList<TypeItem>) { item, position ->
            getCoinAddress()
        }
        binding.networkSpinner.select(defaultNetwork)
    }

    private fun getCoinAddress() {
        presenter.getCoinAddress(
            (binding.cryptoTypeSpinner.getSelected() as Coin).coin,
            (binding.networkSpinner.getSelected() as Network).network
        )
    }

    private fun generateQRCode(text: String?) {
        val png =
            "https://smalltotall.info/wp-content/uploads/2017/04/google-favicon-vector-400x400.png"
        val svg = "https://api.cryptanil.com/qr-icon/3aa3a22df3d50ab92e.svg"
        AppGlideToVectorYou
            .init()
            .with(requireActivity())
            .justLoadAsDrawable(requireActivity(), Uri.parse(svg))
            {

                val qrDrawable: Drawable = createQrDrawable(text, it)
                binding.qrIV.setImageDrawable(qrDrawable)
            }
    }

    private fun setTxIdListener() {
        binding.txIdEt.getEditText().setDebounce({
            binding.submitBT.isEnabled = it.length > 4
        })
    }

    override fun createPresenter() = TransactionPresenter()

}