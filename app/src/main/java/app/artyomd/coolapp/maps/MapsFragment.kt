package app.artyomd.coolapp.maps

import android.content.Context
import android.graphics.*
import android.graphics.Paint.FILTER_BITMAP_FLAG
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import app.artyomd.coolapp.OnFragmentInteractionListener
import app.artyomd.coolapp.R
import app.artyomd.coolapp.api.ReliefService
import app.artyomd.coolapp.db.DB
import app.artyomd.coolapp.db.DisasterMetadata
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_maps.*


class MapsFragment : Fragment() {
    private var listener: OnFragmentInteractionListener? = null


    private lateinit var googleMap: GoogleMap
    private var db: DB? = null
    private lateinit var disasters: MutableList<DisasterMetadata>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_maps, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        disasters = mutableListOf()
        db = DB.instance
        db!!.addValueEventListener(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (data in dataSnapshot.children) {
                    val metadata = data.getValue(DisasterMetadata::class.java)
                    val marker = LatLng(metadata!!.latitude, metadata.longitude)
                    disasters.add(metadata)
                    var bitmap = getMarker(metadata.tag!!)

                    if (bitmap == null) {
                        googleMap.addMarker(MarkerOptions().position(marker)).tag = metadata.id
                    } else {
                        bitmap = scaleBitmap(bitmap, 100, 100)
                        googleMap.addMarker(MarkerOptions().position(marker).icon(BitmapDescriptorFactory.fromBitmap(bitmap))).tag = metadata.id
                    }
                }
            }
        })

        val mapFragment = (childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment)
        mapFragment.getMapAsync { it ->
            googleMap = it
            googleMap.isMyLocationEnabled = true

            ReliefService.getData { data ->
                data!!.forEach {
                    val marker = LatLng(it.latitude, it.longitude)
                    fab.post {
                        var bitmap = getMarker(it.tag!!)

                        if (bitmap == null) {
                            googleMap.addMarker(MarkerOptions().position(marker)).tag = it.id
                        } else {
                            bitmap = scaleBitmap(bitmap!!, 100, 100)
                            googleMap.addMarker(MarkerOptions().position(marker).icon(BitmapDescriptorFactory.fromBitmap(bitmap))).tag = it.id
                        }
                    }
                    disasters.add(it)

                }
            }

            googleMap.setOnMarkerClickListener(GoogleMap.OnMarkerClickListener { marker ->
                for (disaster in disasters) {
                    if (marker.tag!! == disaster.id) {
                        val dialog = MarkerDetailsDialog(context, disaster)
                        dialog.show()
                        return@OnMarkerClickListener true
                    }
                }
                return@OnMarkerClickListener true
            })
        }

        fab.setOnClickListener {
            listener?.openCamera()
        }
    }

    fun getMarker(tags: List<String>): Bitmap? {
        return when {
            tags.contains(DisasterMetadata.TAG_FIRE) -> BitmapFactory.decodeResource(resources, R.drawable.fire)
            tags.contains(DisasterMetadata.TAG_CAR) -> BitmapFactory.decodeResource(resources, R.drawable.car)
            tags.contains(DisasterMetadata.TAG_TRASH) -> BitmapFactory.decodeResource(resources, R.drawable.trash)
            tags.contains(DisasterMetadata.TAG_NATURAL) -> BitmapFactory.decodeResource(resources, R.drawable.natural)
            else -> null
        }
    }

    fun scaleBitmap(bitmap: Bitmap, newWidth: Int, newHeight: Int): Bitmap {
        val scaledBitmap = Bitmap.createBitmap(newWidth, newHeight, Bitmap.Config.ARGB_8888)

        val scaleX = newWidth / bitmap.width.toFloat()
        val scaleY = newHeight / bitmap.height.toFloat()
        val pivotX = 0f
        val pivotY = 0f

        val scaleMatrix = Matrix()
        scaleMatrix.setScale(scaleX, scaleY, pivotX, pivotY)

        val canvas = Canvas(scaledBitmap)
        canvas.matrix = scaleMatrix
        canvas.drawBitmap(bitmap, 0f, 0f, Paint(FILTER_BITMAP_FLAG))

        return scaledBitmap
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
