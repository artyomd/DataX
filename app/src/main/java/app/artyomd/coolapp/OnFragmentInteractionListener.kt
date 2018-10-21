package app.artyomd.coolapp

import android.location.Location

interface OnFragmentInteractionListener {
    fun openMaps()
    fun openCamera()
    fun openShare(path: String)
    fun getCurrentLocation(): Location
}