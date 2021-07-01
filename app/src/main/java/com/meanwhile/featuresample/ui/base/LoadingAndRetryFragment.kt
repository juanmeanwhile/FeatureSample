package com.adidas.sample_feature.ui.base

import android.os.Bundle
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.meanwhile.common.Outcome

/**
 * Base fragment for those simple fragments which need to get some information from server and display it.
 * It offers:
 *  - Loading state
 *  - Error/Retry state parsing the error message from the gateway or using default one
 *  - Success state
 */
// TODO copied from checkout, move to common if seems interesting
abstract class LoadingAndRetryFragment<T> : Fragment() {

    // TODO CON: exposing this fixed views might not be too flexible
    abstract val contentView: View
    abstract val errorGroup: View
    abstract val errorText: TextView
    abstract val errorButton: Button
    abstract val loadingView: View

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        errorButton.setOnClickListener{
            onRetryFetchData()
        }
    }

    /**
     * Display given content in the UI
     */
    abstract fun displayContent(content: T)

    /**
     * Retry the fetch of new content
     */
    abstract fun onRetryFetchData()

    // Submit the data handling while screen loading/error/success state
    open fun submit(outcome: Outcome<T>) {
        when (outcome) {
            is Outcome.Progress -> {
                if (outcome.partialData == null) {
                    toLoadingState()
                } else {
                    toSuccessState()
                }
            }
            is Outcome.Failure -> {
                toErrorState(outcome.e)
            }
            is Outcome.Success -> {
                toSuccessState()
                displayContent(outcome.data)
            }
        }
    }

    private fun toErrorState(e: Throwable) {
        loadingView.visibility = GONE
        contentView.visibility = GONE
        errorGroup.visibility = VISIBLE
        errorText.text = e.message
    }

    private fun toLoadingState() {
        loadingView.visibility = VISIBLE
        contentView.visibility = GONE
        errorGroup.visibility = GONE
    }

    private fun toSuccessState() {
        loadingView.visibility = GONE
        contentView.visibility = VISIBLE
        errorGroup.visibility = GONE
    }
}
