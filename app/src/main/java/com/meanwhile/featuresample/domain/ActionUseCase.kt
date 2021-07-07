package com.meanwhile.featuresample.domain

import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.domain.base.FlowUseCase
import com.meanwhile.featuresample.domain.base.NullableResultFlowUseCase
import com.meanwhile.featuresample.model.ActionResponse
import kotlinx.coroutines.flow.Flow

/**
 * Perform action and expose status of the action through flow
 */
class ActionUseCase(private val repository: SampleFeatureRepository) : NullableResultFlowUseCase<Outcome<ActionResponse>>() {

    override fun performAction() = repository.performActionAtGw()
}