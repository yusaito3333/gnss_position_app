package com.example.gnsspositionapp.ui

import android.content.Context
import android.view.View
import com.google.android.material.snackbar.Snackbar

fun showIndefiniteSnackBar(view : View,context :Context ,resId : Int) {
    Snackbar.make(view,context.getString(resId), Snackbar.LENGTH_INDEFINITE)
        .show()
}

fun showShortSnackBar(view : View,context :Context ,resId : Int) {
    Snackbar.make(view,context.getString(resId), Snackbar.LENGTH_SHORT)
        .show()
}