package com.example.gnsspositionapp.ui.measure

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gnsspositionapp.R
import kotlinx.android.synthetic.main.loading_layout.view.*
import kotlinx.android.synthetic.main.measure_finished_layout.view.*
import kotlinx.android.synthetic.main.measure_fragment.*
import kotlinx.android.synthetic.main.measure_start_layout.view.*

class MeasureFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.measure_fragment,container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        start_view.btn_start.setOnClickListener{
            start_view.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
        }
        loading_view.btn_end.setOnClickListener {
            loading_view.visibility = View.GONE
            finished_view.visibility = View.VISIBLE
        }
        finished_view.btn_measure_again.setOnClickListener {
            finished_view.visibility = View.GONE
            start_view.visibility = View.VISIBLE
        }
    }

}