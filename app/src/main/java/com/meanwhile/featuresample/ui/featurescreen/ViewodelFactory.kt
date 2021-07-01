package com.meanwhile.featuresample.ui.featurescreen

import android.app.Application
import android.content.Context
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.savedstate.SavedStateRegistryOwner
import com.adidas.sample_feature.data.SampleFeatureRepository

class ViewModelFactory(saveStateOwner: SavedStateRegistryOwner) : AbstractSavedStateViewModelFactory(saveStateOwner, null) {
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

        return when {
            modelClass.isAssignableFrom(SampleFeatureViewModel::class.java) -> {
                SampleFeatureViewModel(SampleFeatureRepository())
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
    }
}