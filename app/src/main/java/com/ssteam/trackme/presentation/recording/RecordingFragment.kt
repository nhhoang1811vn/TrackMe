package com.ssteam.trackme.presentation.recording

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.work.WorkRequest
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.maps.model.RoundCap
import com.ssteam.trackme.R
import com.ssteam.trackme.domain.RecordingService
import com.ssteam.trackme.domain.eventbusmodels.RecordingEvent
import com.ssteam.trackme.domain.eventbusmodels.RecordingStatusEvent
import com.ssteam.trackme.domain.models.Location
import kotlinx.android.synthetic.main.fragment_recording.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode

@SuppressLint("MissingPermission")
class RecordingFragment : Fragment(), OnMapReadyCallback {
    private lateinit var mMap: GoogleMap
    private var lastIndexDrawed = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecordingEvent(event: RecordingEvent) {
        when (event.status) {
            RecordingStatusEvent.READY -> {
                //Log.d("RecordingFragment", System.currentTimeMillis().toString())
                event.locations[0].let {
                    val latLng = LatLng(it.lat, it.lng)
                    drawStartLocation(latLng)
                    moveCamera(latLng)
                }
            }
            RecordingStatusEvent.RUNNING -> {
                val locations = event.locations
                val size = locations.size
                val needToDrawLocations = locations.subList(lastIndexDrawed, size)
                Log.d("RecordingFragment", needToDrawLocations.size.toString())
                if (needToDrawLocations.size >= 2){
                    drawRoute(needToDrawLocations)
                    lastIndexDrawed = size - 1
                }
                val lastLocation = locations[size - 1]
                moveCamera(LatLng(lastLocation.lat, lastLocation.lng))

                workoutResultView.update(
                    event.distanceInKiloMeter,
                    event.speedInKiloMeterPerHour,
                    event.duration
                )
            }
        }

    }

    override fun onStart() {
        super.onStart()
        EventBus.getDefault().register(this)
    }

    override fun onStop() {
        super.onStop()
        EventBus.getDefault().unregister(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_recording, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }

    companion object {
        @JvmStatic
        fun newInstance() = RecordingFragment()
    }


    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        startRecordingService()
    }

    private fun drawStartLocation(latLng: LatLng) {
        mMap.addMarker(
            MarkerOptions()
                .position(latLng)
                .title("Start location")
        )
    }

    private fun moveCamera(latLng: LatLng) {
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 14f))
    }
    private fun drawRoute(locations: List<Location>) {
        for (i in 0..locations.size-2) {
            val firstLocation = locations[i]
            val secondLocation = locations[i + 1]
            val polyLine = mMap.addPolyline(PolylineOptions().color(Color.RED)
                .add(
                    LatLng(firstLocation.lat, firstLocation.lng),
                    LatLng(secondLocation.lat, secondLocation.lng)
                )
            )
            polyLine.startCap = RoundCap()
            polyLine.endCap = RoundCap()
        }
    }

    private fun startRecordingService() {
        val intent = Intent(requireActivity(), RecordingService::class.java)
        requireContext().startService(intent)
    }
}