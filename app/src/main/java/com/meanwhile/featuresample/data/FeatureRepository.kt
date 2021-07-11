package com.meanwhile.featuresample.data

import com.meanwhile.featuresample.domain.model.SampleData
import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.domain.model.ActionResponse
import kotlinx.coroutines.flow.Flow

interface FeatureRepository {

    fun getData(): Flow<Outcome<SampleData>>

    fun sendUserAction(): Flow<Outcome<ActionResponse>>
}