package com.meanwhile.featuresample.domain

import com.meanwhile.featuresample.domain.model.SampleData
import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.domain.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Retrieve data for main screen
 */
class MainDataUseCase(private val repository: SampleFeatureRepository) : FlowUseCase<Outcome<SampleData>>() {
    override fun performAction(): Flow<Outcome<SampleData>> {
        return repository.getData()
    }
}