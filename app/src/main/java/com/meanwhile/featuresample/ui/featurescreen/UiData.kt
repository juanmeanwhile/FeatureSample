package com.adidas.sample_feature.ui.featurescreen

import com.meanwhile.common.Outcome

/**
 * Class used to render samples feature UI
 */
data class UiData(val message: String, val actionStatus: Outcome<Int>?)
