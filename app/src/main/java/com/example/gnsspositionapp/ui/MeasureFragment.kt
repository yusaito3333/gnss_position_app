package com.example.gnsspositionapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.gnsspositionapp.R
import kotlinx.android.synthetic.main.measure_fragment.*

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

        btn_start.setOnClickListener{
            btn_start.visibility = View.GONE
            btn_end.visibility = View.VISIBLE
            textInputLayout.visibility = View.GONE
            loading_view.visibility = View.VISIBLE
        }
        btn_end.setOnClickListener {
            btn_start.visibility = View.VISIBLE
            btn_end.visibility = View.GONE
            textInputLayout.visibility = View.VISIBLE
            loading_view.visibility = View.GONE
        }
    }

}