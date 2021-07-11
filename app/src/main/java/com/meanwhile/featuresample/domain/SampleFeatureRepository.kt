package com.meanwhile.featuresample.domain

import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.data.FeatureRepository
import com.meanwhile.featuresample.data.network.BackendApi
import com.meanwhile.featuresample.data.toActionResponse
import com.meanwhile.featuresample.data.toSampleData
import com.meanwhile.featuresample.domain.model.ActionResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class SampleFeatureRepository(private val api: BackendApi) : FeatureRepository {

    // Get initial data for the screen
    override fun getData() = flow {
        // wd could get data from persistence layer here
        emit(Outcome.loading())
        val response = api.getData()
        emit(Outcome.success(response.toSampleData()))
    }.catch { emit(Outcome.failure(it)) }
            .flowOn(Dispatchers.IO)

    // perform action at backend and return response
    override fun sendUserAction() = flow {
        emit(Outcome.loading())
        val response = api.performAction().toActionResponse()
        emit(Outcome.success(response))
    }.catch { emit(Outcome.failure<ActionResponse>(it)) }
            .flowOn(Dispatchers.IO)
}