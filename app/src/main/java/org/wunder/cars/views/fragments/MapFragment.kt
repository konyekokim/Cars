package org.wunder.cars.views.fragments

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Build
import android.os.Bundle
import android.support.annotation.DrawableRes
import android.support.v4.app.Fragment
import android.support.v4.content.ContextCompat
import android.support.v4.graphics.drawable.DrawableCompat
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.android.synthetic.main.fragment_map.*

import org.wunder.cars.R
import org.wunder.cars.views.Placement


class MapFragment : Fragment(), OnMapReadyCallback {
    private var listener: OnFragmentInteractionListener? = null
    private lateinit var googleMap: GoogleMap
    private lateinit var placements: List<Placement>
    private var index: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            placements = it.getParcelableArrayList("placements")!!
            index = it.getInt("current_index")
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val map = childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        map.getMapAsync(this)
        back.setOnClickListener {
            listener!!.onBackPressed()
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentInteractionListener) {
            listener = context
        } else {
            throw RuntimeException(context.toString() + " must implement " +
                    "OnFragmentInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listener = null
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map
        googleMap.isTrafficEnabled = true
        googleMap.isBuildingsEnabled = true
        //googleMap.setOnMarkerClickListener(this)
        placements.forEach {
            val geoPoint = LatLng(it.coordinates!![1].toDouble(), it.coordinates!![0].toDouble())
            val marker: Marker = createMarker(geoPoint, it.name!!,
                    "VIN: ${it.vin!!}")
            marker.setIconFromResource(R.drawable.ic_car)

            if (placements[index] == it) {
                marker.showInfoWindow()
                val newCameraPosition = CameraPosition.Builder().zoom(15.0f).bearing(180f)
                        .target(geoPoint)
                googleMap.animateCamera(CameraUpdateFactory
                        .newCameraPosition(newCameraPosition.build()))
            }
        }

    }

    private fun createMarker(latLng: LatLng, title: String, snippet: String) : Marker{
        return googleMap.addMarker(MarkerOptions()
                .position(latLng)
                .anchor(0.5f, 0.5f)
                .title(title)
                .snippet(snippet))

    }

    private fun Marker.setIconFromResource(@DrawableRes resId: Int) {
        var drawable = ContextCompat.getDrawable(context!!, resId)
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            drawable = DrawableCompat.wrap(drawable!!).mutate()
        }

        val bitmap = Bitmap.createBitmap(drawable!!.intrinsicWidth,
                drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)

        val canvas = Canvas(bitmap)
        drawable.setBounds(0, 0, canvas.width, canvas.height)
        drawable.draw(canvas)
        setIcon(BitmapDescriptorFactory.fromBitmap(bitmap))
    }


    interface OnFragmentInteractionListener {
        fun onBackPressed()
    }
}
