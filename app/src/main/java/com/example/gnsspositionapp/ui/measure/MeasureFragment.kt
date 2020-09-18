package com.example.gnsspositionapp.ui.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.gnsspositionapp.ServiceEventViewModel
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.databinding.MeasureMeasuringLayoutBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeasureFragment : Fragment(),OnBackPressHandler {

    private lateinit var binding : MeasureMeasuringLayoutBinding

    private val measureViewModel : MeasureViewModel by viewModels()

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
                navigateToLocationListFragment()
            }
        }
    }

    //必ずServiceとFlowを閉じる
    private fun navigateToLocationListFragment() {
        measureViewModel.finishMeasuringLocation()
        serviceEventViewModel.measureEnd()
        findNavController().navigate(R.id.measure_to_list)
    }
    //必ずServiceとFlowを閉じる
    private fun navigateToStartFragment() {
        measureViewModel.forceToFinishMeasuringLocation()
        serviceEventViewModel.measureEnd()
        findNavController().popBackStack()
    }

    override fun onBackPressed() : Boolean{
        AlertDialog.Builder(requireContext())
            .setMessage(resources.getString(R.string.dialog_desc))
            .setPositiveButton(resources.getString(R.string.dialog_positive)) { _, _ ->
                navigateToStartFragment()
            }
            .setNegativeButton(resources.getString(R.string.dialog_negative)) { _, _ -> }
            .show()

        return true
    }
}