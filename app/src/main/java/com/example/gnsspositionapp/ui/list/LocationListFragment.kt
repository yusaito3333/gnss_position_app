package com.example.gnsspositionapp.ui.list

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.DividerItemDecoration
import com.example.gnsspositionapp.R
import com.example.gnsspositionapp.ServiceEventViewModel
import com.example.gnsspositionapp.databinding.MeasureFinishedLayoutBinding
import com.example.gnsspositionapp.ui.measure.MeasureViewModel
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LocationListFragment : Fragment() {

    private lateinit var binding : MeasureFinishedLayoutBinding

    private lateinit var adapter : LocationListAdapter

    private val measureViewModel : MeasureViewModel by activityViewModels()

    private val serviceEventViewModel : ServiceEventViewModel by activityViewModels()

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

            locationList.apply {
                adapter = this@LocationListFragment.adapter
                addItemDecoration(DividerItemDecoration(this.context,DividerItemDecoration.VERTICAL))
            }

            btnSave.setOnClickListener {
                measureViewModel.saveLocations()
            }

            btnMeasureAgain.setOnClickListener {
                measureViewModel.startMeasuringLocation()
                serviceEventViewModel.measureStart()
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