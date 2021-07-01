package com.adidas.sample_feature.data

import android.util.Log
import com.meanwhile.featuresample.model.ActionResponse
import com.adidas.sample_feature.model.SampleData
import com.meanwhile.common.Outcome
import com.meanwhile.featuresample.data.toActionResponse
import com.meanwhile.featuresample.data.toSampleData
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.flow
import java.io.IOException
import com.adidas.sample_feature.model.gw.ActionResponse as GwActionResponse
import com.meanwhile.featuresample.model.gw.SampleData as GwSampleData

class SampleFeatureRepository() {

    private var doneActions = 1

    private var mainDataError = false

    // TODO add db layer
    fun getData() = flow<Outcome<SampleData>> {
        emit(Outcome.loading())

        // If DB layer, the obtain local info
        // val cachedData = db.getLocalData()
        // emit(Outcome.loading(partialData = cachedData))

        kotlin.runCatching {
            // val sampleData = api.getSampleData()
            delay(1500) // simulate call

            if (mainDataError) {
                val gwData = GwSampleData("Data from the initial request")
                emit(Outcome.success(gwData.toSampleData()))
            } else {
                mainDataError = true
                throw IOException("It's gateway's fault")
            }

        }.onFailure {
            emit(Outcome.failure(it))
        }
    }

    fun performActionAtGw() = flow<Outcome<ActionResponse>> {
        doneActions++ // TODO for showcase purposes

        emit(Outcome.loading())
        runCatching {
            // val sampleData = api.performActionAtGw()
            delay(1000) // simulate call

            if (doneActions % 4 == 0){
                //Return error every 4 actions
                throw IOException("It's gateway's fault")
            }  else {
                val gwData = GwActionResponse(doneActions)
                emit(Outcome.success(gwData.toActionResponse()))
            }

        }.onFailure {
            emit(Outcome.failure(it))
        }
    }
}
