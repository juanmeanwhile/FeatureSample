package com.meanwhile.featuresample.data.network

import com.adidas.sample_feature.model.gw.ActionResponse
import com.meanwhile.featuresample.data.network.model.SampleData
import kotlinx.coroutines.delay
import java.io.IOException

// This would be a Retrofit Api in the real world
class BackendApi {

    // To simulate errors
    private var mainDataError = false
    private var doneActions = 1

    // Get initial data for the screen
    suspend fun getData(): SampleData {
        delay(1500) // simulate call

        if (mainDataError) {
            return com.meanwhile.featuresample.data.network.model.SampleData("Data from the initial request")
        } else {
            mainDataError = true
            throw IOException("It's servers's fault")
        }
    }

    /**
     * Call the server and return updated data
     */

    suspend fun performAction(): ActionResponse {
        doneActions++
        delay(1000) // simulate call
        if (doneActions % 4 == 0) { //Return error every 4 actions
            throw IOException("It's gateway's fault")
        } else {
            return ActionResponse(doneActions)
        }
    }
}