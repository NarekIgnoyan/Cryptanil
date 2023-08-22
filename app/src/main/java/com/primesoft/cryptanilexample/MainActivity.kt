package com.primesoft.cryptanilexample

import android.content.Intent
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.primesoft.cryptanil.CryptanilApp
import com.primesoft.cryptanil.utils.extensions.toast
import com.primesoft.cryptanil.utils.extensions.viewBinding
import com.primesoft.cryptanilexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private val cryptanilReceiver =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            onCryptanilResult(it?.data)
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        setClickListeners()
    }


    private fun setClickListeners() {
        binding.transactionButton.setOnClickListener {
//            launchCryptanil(binding.transactionInput.text.toString())
            createCryptanilOrder()
        }
    }

    private fun createCryptanilOrder() {
        //GET APP KEY IN https://dashboard.cryptanil.com/admin/settings
        CryptanilApp.createOrder("", { transactionID ->
            launchCryptanil(transactionID)
        }, { apiStatus->

        })
    }

    private fun launchCryptanil(transactionID: String) {
        cryptanilReceiver.launch(
            CryptanilApp.createIntent(
                this,
                transactionID
            )
        )
    }

    private fun onCryptanilResult(data: Intent?) {
        val orderInformation = CryptanilApp.getOrderInformation(data)

        //Example of handling status
        orderInformation?.let {
            when (it.getOrderStatus()) {
                else -> toast(it.getStatusTitle())
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cryptanilReceiver.unregister()
    }

}