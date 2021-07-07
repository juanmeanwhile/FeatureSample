package com.meanwhile.featuresample.domain.base

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf

/**
 * Simple use case exposing result as a flow.
 * Result flow will emit null while the action has not been triggered
 */
@ExperimentalCoroutinesApi
abstract class NullableResultFlowUseCase<T> {

    /**
     * Trigger for the action which can be done in this request
     */
    private val _trigger = MutableStateFlow<Boolean?>(null)

    /**
     * Exposes result of this use case
     */
    val resultFlow: Flow<T?> = _trigger.flatMapLatest { trigger ->
        trigger?.let { performAction() } ?: flowOf(null)
    }

    protected abstract fun performAction() : Flow<T>

    /**
     * Triggers the execution of this use case
     */
    suspend fun launch() {
        _trigger.emit(!(_trigger.value?:true))
    }
}