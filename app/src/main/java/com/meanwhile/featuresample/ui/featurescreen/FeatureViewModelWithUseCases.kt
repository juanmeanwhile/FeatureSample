package com.meanwhile.featuresample.ui.featurescreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.meanwhile.featuresample.domain.model.ActionResponse
import com.meanwhile.featuresample.domain.model.SampleData
import com.meanwhile.featuresample.ui.model.SampleFeatureEvent
import com.meanwhile.featuresample.ui.model.UiData
import com.meanwhile.common.Outcome
import com.meanwhile.common.SingleLiveEvent
import com.meanwhile.common.mapData
import com.meanwhile.featuresample.domain.ActionUseCase
import com.meanwhile.featuresample.domain.MainDataUseCase
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch

/**
 * Holds business logic for Sample feature.
 * - Exposes only two liveData to UI, one exposing the UI state and one exposing single use events
 * - All the sources of variations for the UI are represented by flow which are all combined in a single method, which can be easily tested.
 *   - (This could be an external class injected and changed for china or hype)
 * - Exposes one method per gw request to trigger the request. This is specially interesting to allow the user retry failed request.
 * - Ui state is represented by a sealed class which contains all the required info to render the screen.
 * - Events are also a sealed class
 *
 * Next steps:
 *  - Send some kind of single use events to the UI, maybe with errors
 *  - Consider saving some state in the viewModel
 *  - Move UI generation to an strategy
 *
 */
class FeatureViewModelWithUseCases(
        private val mainDataUseCase: MainDataUseCase,
        private val actionUseCase: ActionUseCase
        ) : ViewModel() {

    /**
     * LiveData to screen data
     */
    val uiLiveData: LiveData<Outcome<UiData>> = combine(mainDataUseCase.resultFlow, actionUseCase.resultFlow) {
        outcomeA: Outcome<SampleData>, outcomeB: Outcome<ActionResponse>? ->
        Log.d("FLAR", "ui LiveData -> outcomeA: $outcomeA, action: $outcomeB" )
        generateUiData(outcomeA, outcomeB)
    }.asLiveData()

    private val _eventLiveData = SingleLiveEvent<SampleFeatureEvent>()

    /**
     * One time event .
     * Used to send consumable events from viewModel to UI (i.e: display toast, or open screen)
     * Exposed as non mutable
     *
     */
    val eventLiveData: LiveData<SampleFeatureEvent> = _eventLiveData

    /**
     * Trigger request to get data
     */
    fun requestData() {
        viewModelScope.launch {
            mainDataUseCase.launch()
        }
    }

    /**
     * Trigger request to perform action at GW
     */
    fun onUserActionDone() {
        viewModelScope.launch {
            actionUseCase.launch()
        }
    }

    //TODO PRO: all the ui data is generated here. This could be an strategy which might change by country
    /**
     * Generate UI state from the different sources
     */
    private fun generateUiData(outcomeA: Outcome<SampleData>, actionOutcome: Outcome<ActionResponse>?): Outcome<UiData> {
        val actionStatus = actionOutcome?.mapData {
            it.actionNum
        }

        return outcomeA.mapData { a ->
            UiData(a.someData, actionStatus)
        }
    }
}
