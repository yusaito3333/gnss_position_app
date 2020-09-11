package com.example.gnsspositionapp.ui.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gnsspositionapp.ServiceEventViewModel
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.databinding.MeasureMeasuringLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class MeasureFragment : Fragment(),OnBackPressHandler {

    private lateinit var binding : MeasureMeasuringLayoutBinding

    private val measureViewModel : MeasureViewModel by activityViewModels()

    private val serviceEventViewModel : ServiceEventViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.measure_measuring_layout,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            viewModel = measureViewModel

            btnEnd.setOnClickListener {
                measureViewModel.finishMeasuringLocation()
                navigateToStartFragment()
                Timber.d("${measureViewModel.locations.value}")
            }
        }
    }

    private fun navigateToStartFragment() {
        serviceEventViewModel.measureEnd()
        findNavController().popBackStack()
    }

    override fun onBackPressed() : Boolean{
        AlertDialog.Builder(requireContext())
            .setMessage(resources.getString(R.string.dialog_desc))
            .setPositiveButton(resources.getString(R.string.dialog_positive)) { _, _ ->
                measureViewModel.forceToFinishMeasuringLocation()
                navigateToStartFragment()
            }
            .setNegativeButton(resources.getString(R.string.dialog_negative)) { _, _ -> }
            .show()

        return true
    }
}