package com.example.gnsspositionapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.databinding.MeasureFinishedLayoutBinding
import com.example.gnsspositionapp.ui.measure.LocationListAdapter
import com.example.gnsspositionapp.ui.measure.MeasureViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationListFragment : Fragment() {

    private lateinit var binding : MeasureFinishedLayoutBinding

    private lateinit var adapter : LocationListAdapter

    private val measureViewModel : MeasureViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.measure_finished_layout,
            container,false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = LocationListAdapter(measureViewModel.unitIsYard.value!!)

        binding.apply {
            lifecycleOwner = viewLifecycleOwner

            viewModel = measureViewModel

            locationList.adapter = adapter

            btnSave.setOnClickListener {
                measureViewModel.saveLocations()
            }

            btnMeasureAgain.setOnClickListener {
                measureViewModel.startMeasuringLocation()
                navigateToMeasureFragment()
            }
        }

        measureViewModel.locations.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }

        measureViewModel.unitIsYard.observe(viewLifecycleOwner) {
            adapter.convertUnit(it)
        }
    }

    private fun navigateToMeasureFragment() {
        findNavController().navigate(R.id.location_to_measure)
    }
}