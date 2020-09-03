package com.example.gnsspositionapp.ui.start

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.gnsspositionapp.R
import kotlinx.android.synthetic.main.start_fragment.*

class StartFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return inflater.inflate(R.layout.start_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btn_to_measure.setOnClickListener{
            navigateToMeasureFragment()
        }
    }

    private fun navigateToMeasureFragment() {
        findNavController().navigate(R.id.start_to_measure)
    }
}