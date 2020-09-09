package com.example.gnsspositionapp.adapter

import android.view.View
import androidx.databinding.BindingAdapter

object BindingAdapter {

    @BindingAdapter("visibleGone")
    @JvmStatic
    fun showHide(v : View, visible : Boolean) {
        v.visibility = if(visible) View.VISIBLE else View.GONE
    }
}