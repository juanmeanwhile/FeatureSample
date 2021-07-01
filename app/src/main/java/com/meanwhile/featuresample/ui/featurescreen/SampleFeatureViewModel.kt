package com.meanwhile.featuresample.ui.featurescreen

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.adidas.sample_feature.data.SampleFeatureRepository
import com.meanwhile.featuresample.model.ActionResponse
import com.adidas.sample_feature.model.SampleData
import com.adidas.sample_feature.ui.featurescreen.SampleFeatureEvent
import com.adidas.sample_feature.ui.featurescreen.UiData
import com.meanwhile.common.Outcome
import com.meanwhile.common.SingleLiveEvent
import com.meanwhile.common.mapData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch

/**
 * Holds business logic for Sample feature.
 * - Exposes only two liveData to UI, one exposing the UI state and one exposing single use events
 * - All the sources of variations for the UI are represented by flow which are all combined in a sinle method, which can be easily tested.
 *   - (This could be an external class injected and changed for china or hype)
 * - Exposes one method per gw request to trigger the request. This is specially interesting to allow the user retry failed request.
 * - Ui state is represented by a sealed class which containt all the required info to render the screen.
 * - Events are also a sealed class which
 */
class SampleFeatureViewModel(private val sampleFeatureRepo: SampleFeatureRepository) : ViewModel() {

    /**
     * Trigger for the main request which loads data for the screen
     */
    private val _requestTrigger = MutableStateFlow<Boolean>(true)
    private val requestFlow = _requestTrigger.flatMapLatest {
        Log.d("FLAR", "main request triggered" )
        sampleFeatureRepo.getData()
    }

    //TODO CON: actionFlow with the null check is a little boilerplated
    /**
     * Trigger for the action which can be done in this request
     */
    private val _actionTrigger = MutableStateFlow<Boolean?>(null)
    private val actionFlow : Flow<Outcome<ActionResponse>?> = _actionTrigger.flatMapLatest { trigger ->
        if (trigger != null) {
            Log.d("FLAR", "performActionAtGw triggered" )
            sampleFeatureRepo.performActionAtGw()
        } else {
            flowOf(null)
        }
    }

    /**
     * LiveData to screen data
     */
    val uiLiveData: LiveData<Outcome<UiData>> = combine(requestFlow, actionFlow) { outcomeA: Outcome<SampleData>, outcomeB: Outcome<ActionResponse>? ->
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

    //TODO CON: not loving the ! from the previous value
    /**
     * Trigger request to get data
     */
    fun requestData() {
        viewModelScope.launch {
            _requestTrigger.emit(!_requestTrigger.value)
        }
    }

    //TODO CON: Not loving the way to propagate trigger
    /**
     * Trigger request to perform action at GW
     */
    fun performActionAtGw() {
        viewModelScope.launch {
            _actionTrigger.emit(!(_actionTrigger.value?:true))
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
