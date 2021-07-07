package com.meanwhile.featuresample.domain

import com.adidas.sample_feature.model.SampleData
import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.domain.base.FlowUseCase
import kotlinx.coroutines.flow.Flow

/**
 * Retrieve data for main screen
 */
class MainDataUseCase(private val repository: SampleFeatureRepository) : FlowUseCase<Outcome<SampleData>>() {
    override fun performAction(): Flow<Outcome<SampleData>> {

        //Could have logic to get data from db

        return repository.getData()
    }
}