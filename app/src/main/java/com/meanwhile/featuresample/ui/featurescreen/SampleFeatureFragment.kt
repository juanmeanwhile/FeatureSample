package com.meanwhile.featuresample.ui.featurescreen

import android.animation.LayoutTransition.CHANGING
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.ViewModelProvider
import com.adidas.sample_feature.ui.base.LoadingAndRetryFragment
import com.adidas.sample_feature.ui.featurescreen.UiData
import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.databinding.FragmentLoadingAndRetryBinding

/**
 * This fragments serves as a base for most of the screens that needs tob be implemented in the app.
 * The whole architecture covers:
 *  - Requiring a first request to populate the screen
 *  - Handles whole screen loading/success/Error&Retry states
 *  - More request being trigger ed as a result of a user action which will modify the UI state
 *  - Using db as a local source of data for Offline mode.
 *  - Unidirectional flow of information: User action goes to viewModel which ends up generating a new UI state.
 */
class SampleFeatureFragment : LoadingAndRetryFragment<UiData>() {

    private var _binding: FragmentLoadingAndRetryBinding? = null
    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override val contentView: View
        get() = binding.contentGroup

    override val errorGroup: View
        get() = binding.errorGroup

    override val loadingView: View
        get() = binding.selectorLoading

    override val errorButton: Button
        get() = binding.retryButton

    override val errorText: TextView
        get() = binding.errorText

    private lateinit var viewModel: SampleFeatureViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        _binding = FragmentLoadingAndRetryBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(this, ViewModelFactory(this)).get(SampleFeatureViewModel::class.java)
        return binding.root

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupObservers()
        setupViews()
    }

    override fun onStart() {
        super.onStart()

        viewModel.requestData()
    }

    private fun setupObservers() {
        viewModel.eventLiveData.observe(viewLifecycleOwner) {
            // React to events like: navigation to other places, show toast, require permissions...
        }

        viewModel.uiLiveData.observe(viewLifecycleOwner) { outcome ->
            // Update UI
            submit(outcome)
        }
    }

    private fun setupViews() {
        binding.root.layoutTransition.enableTransitionType(CHANGING)

        binding.actionButton.setOnClickListener {
            viewModel.performActionAtGw()
        }
    }

    override fun displayContent(content: UiData) {
        with (binding) {
            data.text = content.message

            val text = when (content.actionStatus) {
                is Outcome.Failure -> content.actionStatus.e.toString()
                is Outcome.Progress -> "loading"
                is Outcome.Success -> "Actions count: ${content.actionStatus.data}"
                null -> "Launch action"
            }
            actionButton.text = text
        }
    }

    override fun onRetryFetchData() {
        viewModel.requestData()
    }

    companion object {
        fun newInstance() = SampleFeatureFragment()
    }
}
