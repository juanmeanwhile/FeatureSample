package com.meanwhile.featuresample.ui.featurescreen

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.meanwhile.featuresample.domain.ActionUseCase
import com.meanwhile.featuresample.domain.MainDataUseCase
import com.meanwhile.featuresample.domain.SampleFeatureRepository
import com.meanwhile.featuresample.ui.screenb.FeatureBViewModel

class ViewModelFactory(saveStateOwner: SavedStateRegistryOwner) : AbstractSavedStateViewModelFactory(saveStateOwner, null) {
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

        return when {
            modelClass.isAssignableFrom(SampleFeatureViewModel::class.java) -> {
                SampleFeatureViewModel(SampleFeatureRepository())
            }
            modelClass.isAssignableFrom(FeatureBViewModel::class.java) -> {
                val repo = SampleFeatureRepository()
                val mainUseCase = MainDataUseCase(repo)
                val actionUseCase = ActionUseCase(repo)
                FeatureBViewModel(mainUseCase, actionUseCase)
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
    }
}