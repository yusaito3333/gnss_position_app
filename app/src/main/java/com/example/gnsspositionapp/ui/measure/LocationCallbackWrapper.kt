package com.example.gnsspositionapp.ui.measure

import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import java.lang.ref.SoftReference

//LocationCallbackをそのまま使用するとリークするため、弱参照を保持する
class LocationCallbackWrapper(locationCallback: LocationCallback) : LocationCallback() {

    private val weakRef = SoftReference(locationCallback)

    override fun onLocationResult(locationResult: LocationResult?) {
        weakRef.get()?.onLocationResult(locationResult)
    }
}

