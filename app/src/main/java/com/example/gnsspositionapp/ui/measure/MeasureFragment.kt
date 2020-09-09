package com.example.gnsspositionapp.ui.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.Navigation
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.databinding.MeasureFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MeasureFragment : Fragment(),OnBackPressHandler {

    private lateinit var adapter: LocationListAdapter

    private lateinit var binding : MeasureFragmentBinding

    private val measureViewModel : MeasureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.measure_fragment,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LocationListAdapter(measureViewModel.unitIsYard.value ?: false)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            finishedView.locationList.adapter = adapter

            viewModel = measureViewModel

            loadingView.btnEnd.setOnClickListener {
                measureViewModel.finishMeasuringLocation()
            }

            finishedView.btnMeasureAgain.setOnClickListener {
                measureViewModel.startMeasuringLocation()
            }

            finishedView.btnSave.setOnClickListener {
                measureViewModel.saveLocations()
            }
        }

        measureViewModel.locations.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        measureViewModel.unitIsYard.observe(viewLifecycleOwner) {
            adapter.convertUnit(it)
        }

    }

    override fun onBackPressed() : Boolean{
        return if(measureViewModel.state.value == MeasureViewModel.MeasureState.MEASURING){
            AlertDialog.Builder(requireContext())
                .setMessage(resources.getString(R.string.dialog_desc))
                .setPositiveButton(resources.getString(R.string.dialog_positive)) { _, _ ->
                    measureViewModel.finishMeasuringLocation()
                    Navigation.findNavController(requireActivity(), R.id.nav_host_fragment_container).popBackStack()
                }
                .setNegativeButton(resources.getString(R.string.dialog_negative)) { _, _ -> }
                .show()
            true
        }else{
            false
        }
    }
}