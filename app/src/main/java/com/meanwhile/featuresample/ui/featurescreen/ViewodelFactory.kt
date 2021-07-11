package com.meanwhile.featuresample.ui.featurescreen

import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.savedstate.SavedStateRegistryOwner
import com.meanwhile.featuresample.data.network.BackendApi
import com.meanwhile.featuresample.domain.ActionUseCase
import com.meanwhile.featuresample.domain.MainDataUseCase
import com.meanwhile.featuresample.domain.SampleFeatureRepository

class ViewModelFactory(saveStateOwner: SavedStateRegistryOwner) : AbstractSavedStateViewModelFactory(saveStateOwner, null) {
    override fun <T : ViewModel?> create(key: String, modelClass: Class<T>, handle: SavedStateHandle): T {

        return when {
            modelClass.isAssignableFrom(SampleFeatureViewModel::class.java) -> {
                SampleFeatureViewModel(SampleFeatureRepository(BackendApi()))
            }
            modelClass.isAssignableFrom(FeatureViewModelWithUseCases::class.java) -> {
                val repo = SampleFeatureRepository(BackendApi())
                val mainUseCase = MainDataUseCase(repo)
                val actionUseCase = ActionUseCase(repo)
                FeatureViewModelWithUseCases(mainUseCase, actionUseCase)
            }
            else -> {
                throw IllegalArgumentException("Unknown ViewModel class")
            }
        } as T
    }
}