package com.meanwhile.featuresample.ui.model

/**
 * Single use events happening in sample feature
 */
sealed class SampleFeatureEvent {
    class OneEvent : SampleFeatureEvent()
    class OtherEvent : SampleFeatureEvent()
    class ActionError(ex: Throwable) : SampleFeatureEvent()
}
