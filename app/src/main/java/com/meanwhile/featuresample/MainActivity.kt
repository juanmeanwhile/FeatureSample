package com.meanwhile.featuresample

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import com.meanwhile.featuresample.ui.screenb.FeatureBFragment
import com.meanwhile.featuresample.ui.featurescreen.SampleFeatureFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val fragment = SampleFeatureFragment.newInstance()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.screens_menu, menu)

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val fragment = FeatureBFragment()
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
        return true
    }
}