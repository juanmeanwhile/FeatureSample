package com.meanwhile.featuresample.data

import com.meanwhile.featuresample.domain.model.SampleData
import com.meanwhile.featuresample.domain.model.ActionResponse
import com.adidas.sample_feature.model.gw.ActionResponse as GwActionResponse
import com.meanwhile.featuresample.data.network.model.SampleData as GwSampleData

fun GwSampleData.toSampleData() = SampleData(someData)

fun GwActionResponse.toActionResponse() = ActionResponse(actionNum)
