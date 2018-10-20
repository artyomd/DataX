package app.artyomd.coolapp.maps

import android.content.Context
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.artyomd.coolapp.OnFragmentInteractionListener
import app.artyomd.coolapp.R
import app.artyomd.coolapp.db.DB
import app.artyomd.coolapp.db.DisasterMetadata
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.gson.reflect.TypeToken
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null


    private lateinit var googleMap: GoogleMap
    private var db: DB? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        db = DB.instance
        db!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val metadata = data.getValue(DisasterMetadata::class.java)
                    val marker = LatLng(metadata!!.latitude, metadata.longitude)
                    googleMap.addMarker(MarkerOptions().position(marker))
                }
            }
        })

        val mapFragment = (childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment)
        mapFragment.getMapAsync {
            googleMap = it

        }

        fab.setOnClickListener {
            listener?.openCamera()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    companion object {
        @JvmStatic
        fun newInstance() = MapsFragment()
    }
}
