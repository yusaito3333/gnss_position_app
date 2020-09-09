package com.example.gnsspositionapp

import android.os.Bundle
import android.view.MenuItem
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.gnsspositionapp.ui.measure.MeasureViewModel
import com.example.gnsspositionapp.ui.measure.OnBackPressHandler
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import timber.log.Timber

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setUpToolbar()
    }

    private fun setUpToolbar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConf = AppBarConfiguration(setOf(R.id.measure_fragment,R.id.start_fragment))

        toolbar.setupWithNavController(navController,appBarConf)
    }

    override fun onBackPressed() {
        val navHost = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val target = navHost.childFragmentManager.findFragmentById(R.id.nav_host_fragment_container)

        if (target is OnBackPressHandler) {

            if (target.onBackPressed()) {
                return
            }
        }
        super.onBackPressed()
    }
}