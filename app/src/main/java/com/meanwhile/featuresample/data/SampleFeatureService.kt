package com.adidas.sample_feature.data

import com.meanwhile.featuresample.model.gw.SampleData

/**
* Endpoints used for SampleFeature
*/
interface SampleFeatureService {

    suspend fun getSampleData(): SampleData
}
