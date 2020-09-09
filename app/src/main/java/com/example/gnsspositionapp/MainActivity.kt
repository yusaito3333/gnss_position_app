package com.example.gnsspositionapp

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.example.gnsspositionapp.databinding.ActivityMainBinding
import com.example.gnsspositionapp.ui.measure.MeasureViewModel
import com.example.gnsspositionapp.ui.measure.OnBackPressHandler
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding

    private val measureViewModel : MeasureViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this,R.layout.activity_main)

        setUpToolbar()

        measureViewModel.savingEvent.observe(this){
            showSavingSnackBar()
        }

        measureViewModel.saveFinishedEvent.observe(this){
            showSaveFinishedSnackBar()
        }
    }

    private fun setUpToolbar() {
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container) as NavHostFragment

        val navController = navHostFragment.navController

        val appBarConf = AppBarConfiguration(setOf(R.id.measure_fragment,R.id.start_fragment))

        binding.toolbar.setupWithNavController(navController,appBarConf)
    }

    private fun showSavingSnackBar() {
        Snackbar.make(binding.root,getString(R.string.snack_bar_saving),Snackbar.LENGTH_INDEFINITE)
            .show()
    }

    private fun showSaveFinishedSnackBar() {
        Snackbar.make(binding.root,getString(R.string.snack_bar_save_finished),Snackbar.LENGTH_SHORT)
            .show()
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