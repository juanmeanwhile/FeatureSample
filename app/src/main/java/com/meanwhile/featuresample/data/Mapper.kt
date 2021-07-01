package com.meanwhile.featuresample.data

import com.adidas.sample_feature.model.SampleData
import com.meanwhile.featuresample.model.ActionResponse
import com.adidas.sample_feature.model.gw.ActionResponse as GwActionResponse
import com.meanwhile.featuresample.model.gw.SampleData as GwSampleData

fun GwSampleData.toSampleData() = SampleData(someData)

fun GwActionResponse.toActionResponse() = ActionResponse(actionNum)
