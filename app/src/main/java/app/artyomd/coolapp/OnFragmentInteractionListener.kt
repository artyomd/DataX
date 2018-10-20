package app.artyomd.coolapp

import android.location.Location

interface OnFragmentInteractionListener {
    fun openCamera()
    fun openShare(path: String)
    fun getCurrentLocation(): Location
}