package com.primesoft.cryptanil.app_views

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.ProgressBar
import androidx.core.view.isGone
import androidx.core.view.isVisible
import com.primesoft.cryptanil.R
import com.primesoft.cryptanil.databinding.CryptanilProgressBarLayoutBinding
import com.primesoft.cryptanil.enums.ProgressStatus
import com.primesoft.cryptanil.utils.extensions.*


class CryptanilProgress(context: Context, attributeSet: AttributeSet) :
    FrameLayout(context, attributeSet) {

    private lateinit var binding: CryptanilProgressBarLayoutBinding

    init {
        init()
    }

    private fun init() {
        binding =
            CryptanilProgressBarLayoutBinding.inflate(LayoutInflater.from(context), this, true)
        binding.confirmingTV.isEnabled = false
    }

    fun setProgress(progressStatus: Int) {
        when (ProgressStatus.getById(progressStatus)) {
            ProgressStatus.WAITING -> enableWaitingProgress()
            ProgressStatus.CONFIRMING -> enableConfirmingProgress()
            ProgressStatus.CONFIRMED -> enableConfirmedProgress()
        }
    }

    private fun enableWaitingProgress() {
        binding.with {
            confirmingPB.isVisible.ifTrue {
                confirmingLineView.setBackgroundColor(colors(R.color.darkGray))
                confirmingTV.setTextColor(colors(R.color.darkGray))

                resetProgress(waitingPB)

                confirmingEmptyProgress.gone()
                confirmingPB.visible()
            }
        }
    }

    private fun enableConfirmingProgress() {
        binding.with {
            confirmingPB.isGone.ifTrue {
                confirmingLineView.setBackgroundColor(colors(R.color.buttonColor))
                confirmingTV.setTextColor(colors(R.color.buttonColor))

                onProgressDone(waitingPB)

                confirmingEmptyProgress.gone()
                confirmingPB.visible()
            }
        }
    }

    private fun enableConfirmedProgress() {
        binding.with {
            confirmedPB.isGone.ifTrue {
                confirmedLineView.setBackgroundColor(colors(R.color.buttonColor))
                confirmedTV.setTextColor(colors(R.color.buttonColor))

                onProgressDone(waitingPB)
                onProgressDone(confirmingPB)

                confirmedEmptyProgress.gone()
                confirmedPB.visible()
            }
        }
    }

    private fun onProgressDone(progressBar: ProgressBar) {
        progressBar.indeterminateDrawable = drawables(R.drawable.done_icon)
        progressBar.setBackgroundResource(R.drawable.done_icon)
    }

    private fun resetProgress(progressBar: ProgressBar) {
        progressBar.isIndeterminate = false
        progressBar.indeterminateDrawable = null
        progressBar.setBackgroundResource(-1)
    }

}