package com.primesoft.cryptanil.fragments

import android.os.Bundle
import android.view.View
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.adapter.view_holders.InfoVH
import com.primesoft.cryptanil.adapter.view_holders.TransactionVH
import com.primesoft.cryptanil.base.AppFragment
import com.primesoft.cryptanil.databinding.StatusFragmentLayoutBinding
import com.primesoft.cryptanil.models.InfoItem
import com.primesoft.cryptanil.models.OrderInformation
import com.primesoft.cryptanil.models.OrderTransaction
import com.primesoft.cryptanil.presenters.StatusPresenter
import com.primesoft.cryptanil.utils.DATA_KEY
import com.primesoft.cryptanil.utils.extensions.*
import com.primesoft.cryptanil.views.StatusView

class StatusFragment : AppFragment<StatusView, StatusPresenter>(R.layout.status_fragment_layout),
    StatusView {

    private val binding by viewBinding(StatusFragmentLayoutBinding::bind)

    private var orderInformation: OrderInformation? = null

    companion object {
        fun getInstance(
            orderInformation: OrderInformation
        ) = StatusFragment().apply {
            arguments = Bundle()
            arguments?.putSerializable(DATA_KEY, orderInformation)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        orderInformation = arguments?.serializable(DATA_KEY)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpUI()
        setUpTransactionsUI()
        setClickListeners()
    }

    private fun setUpUI() {
        clearInfoView()

        orderInformation?.with {

            if (isPartlyExpired())
                binding.actionBT.text = getString(R.string.request_refund)
            else
                binding.actionBT.text = getString(R.string.go_to_merchant)

            binding.statusIV.setImageResource(getStatusImageID())
            binding.statusTV.text = getString(getStatusTitleID())
            binding.statusMessageTV.text = getString(getStatusMessageID())
            haveRefundReason {
                binding.statusMessageTV.text = binding.statusMessageTV.text.toString() + ": " + it
            }
            binding.statusMessageTV.setTextColor(colors(getStatusMessageColor()))
            binding.progressIndicator.visible(getIndicatorVisibility())

            requiredCoinAmount?.let {
                addInfoView(R.drawable.ic_data, R.string.required_amount, getTotalRequiredAmount())
            }

            orderInformation?.totalPaid?.let {
                addInfoView(
                    R.drawable.ic_data,
                    R.string.total_paid,
                    orderInformation?.getTotalPaidAmount() ?: EMPTY_STRING
                )
            }

            orderInformation?.refundTransactionFee?.let {
                addInfoView(
                    R.drawable.ic_data,
                    R.string.fee,
                    orderInformation?.getRefundFeeAmount() ?: EMPTY_STRING
                )
            }

            cryptoAmount?.let { addInfoView(R.drawable.ic_data, R.string.coin_amount, it) }

            clientWillReceive?.let {
                addInfoView(
                    R.drawable.ic_refresh,
                    R.string.refunded,
                    orderInformation?.getRefundAmount() ?: EMPTY_STRING
                )
            }

            convertedAmount?.let {
                addInfoView(
                    R.drawable.ic_refresh,
                    R.string.converted_amount,
                    getConvertedAmount()
                )
            }
            convertedAmountCurrency?.let {
                addInfoView(
                    R.drawable.ic_repeate,
                    R.string.converted_amount, getConvertedAmountCurrency()
                )
            }
            merchantCommission?.let {
                addInfoView(
                    R.drawable.ic_discount,
                    R.string.merchant_commission, getMerchantCommission()
                )
            }
            merchantCommissionCurrency?.let {
                if (it > 0)
                    addInfoView(
                        R.drawable.ic_discount,
                        R.string.merchant_commission_currency, getMerchantCommissionCurrency()
                    )
            }
            amountToShow?.let { addInfoView(R.drawable.ic_data, R.string.amount, getAmount()) }
            amountToShowCurrency?.let {
                if (it > 0)
                    addInfoView(
                        R.drawable.ic_repeate,
                        R.string.amount_currency,
                        getAmountCurrency()
                    )
            }

            txId?.let {
                addInfoView(
                    R.drawable.ic_document,
                    R.string.tx_id,
                    refundTxId ?: txId ?: EMPTY_STRING
                )
            }
            companyName?.let {
                addInfoView(R.drawable.ic_home, R.string.company_name, companyName ?: EMPTY_STRING)
            }
        }
    }

    private fun setClickListeners() {
        binding.with {
            toolbar.onActionClicked { presenter.closeCryptanil() }
            actionBT.setOnClickListener {
                presenter.navigateFromAction(
                    orderInformation?.isPartlyExpired()
                )
            }
        }
    }

    private fun addInfoView(resID: Int, titleID: Int, text: String) {
        val holder = InfoVH(activity?.layoutInflater, InfoItem(resID, getString(titleID), text))
        binding.infoHolder.addView(holder.view)
    }

    private fun clearInfoView() {
        binding.infoHolder.with {
            if (childCount > 0) binding.infoHolder.removeAllViews()
        }
    }

    private fun setUpTransactionsUI() {
        orderInformation?.orderTransactions?.isNotEmpty()?.ifTrue {
            binding.transactionsTV.visible()
            orderInformation?.orderTransactions?.forEach { transaction ->
                addTransaction(transaction)
            }
        }
    }

    override fun createPresenter() = StatusPresenter()

    override fun onOrderInformationUpdated(orderInformation: OrderInformation) {
        this.orderInformation = orderInformation
        setUpUI()
    }

    override fun startRefund() {
        activity?.showRefundDialog {
            presenter.requestRefund(it)
        }
    }

    private fun addTransaction(transaction: OrderTransaction) {
        val holder = TransactionVH(activity?.layoutInflater, transaction)
        holder.setTransactionClickListener {
            requireActivity().openChromeTab(it)
        }
        binding.transactionsHolder.addView(holder.view)
    }

}