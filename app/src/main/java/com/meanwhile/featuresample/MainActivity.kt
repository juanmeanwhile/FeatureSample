package com.meanwhile.featuresample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.meanwhile.featuresample.ui.featurescreen.SampleFeatureFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = SampleFeatureFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
}