package com.example.gnsspositionapp.ui.measure

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.lang.ref.SoftReference

class LocationCallbackWrapper(locationCallback: LocationCallback) : LocationCallback() {

    private val weakRef = SoftReference(locationCallback)

    override fun onLocationResult(p0: LocationResult?) {
        weakRef.get()?.onLocationResult(p0)
    }
}

