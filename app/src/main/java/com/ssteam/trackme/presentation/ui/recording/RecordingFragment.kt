package com.ssteam.trackme.presentation.ui.recording

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.ssteam.trackme.R
import com.ssteam.trackme.databinding.FragmentRecordingBinding
import com.ssteam.trackme.di.Injectable
import com.ssteam.trackme.domain.Status
import com.ssteam.trackme.domain.models.RecordingItem
import com.ssteam.trackme.presentation.utils.Utils
import com.ssteam.trackme.presentation.utils.autoCleared
import kotlinx.android.synthetic.main.fragment_recording.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import javax.inject.Inject

@SuppressLint("MissingPermission")
class RecordingFragment : Fragment(), OnMapReadyCallback, Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: RecordingViewModel by viewModels {
        viewModelFactory
    }
    var binding by autoCleared<FragmentRecordingBinding>()

    private lateinit var mMap: GoogleMap
    private var drewLastIndex = 0

    @Subscribe(threadMode = ThreadMode.MAIN)
    fun onRecordingItems(items: MutableList<RecordingItem>) {
        viewModel.setRecordingItem(items)
        val validLocations = viewModel.getValidLocations()
        val size = validLocations.size
        if (size == 1){
            val startLocation = validLocations[0]
            val latLng = LatLng(startLocation.lat, startLocation.lng)
            Utils.drawStartLocation(mMap,startLocation)
            moveCamera(latLng)
            viewModel.setRunningState()
        }else{
            val needToDrawLocations = validLocations.subList(drewLastIndex, size)
            if (needToDrawLocations.size >= 2){
                Utils.drawRoute(mMap,needToDrawLocations)
                drewLastIndex = size - 1
            }
        }
        updateResultView(items.last())
    }

    private fun updateResultView(item: RecordingItem){
        workoutResultView.update(
            item.distance,
            item.speed,
            viewModel.getDurationText()
        )
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
        val dataBinding = DataBindingUtil.inflate<FragmentRecordingBinding>(
            inflater,
            R.layout.fragment_recording,
            container,
            false
        )
        binding = dataBinding
        //sharedElementReturnTransition = TransitionInflater.from(context).inflateTransition(R.transition.move)
        return dataBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.lifecycleOwner = viewLifecycleOwner
        binding.waitingState = viewModel.waitingState
        binding.runningState = viewModel.runningState
        binding.pauseState =  viewModel.pauseState
        binding.pauseCallback = View.OnClickListener {
            viewModel.pause()
        }
        binding.resumeCallback = View.OnClickListener {
            viewModel.resume()
        }
        binding.stopCallback = View.OnClickListener {
            viewModel.stop()
        }
        binding.saveResultState = viewModel.saveResultState
        //binding.isLoading = viewModel.isLoading
        viewModel.saveResultState.observe(viewLifecycleOwner, Observer {
            when(it.status){
                Status.SUCCESS -> {
                    findNavController().popBackStack()
                }
                Status.ERROR -> {
                    //TODO
                    //should be move to BaseActivity, BaseFragment
                    //(requireActivity() as BaseActivity).showToast()
                    Toast.makeText(requireContext(),"save result error: ${it.message}", Toast.LENGTH_LONG).show()
                }
                Status.LOADING -> {
                    //already mapped
                }
            }
        })

        val mapFragment = childFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
    }
    private fun checkGPSEnableAndStart(){
        if (!Utils.isGpsEnable(requireContext())){
            showTipsEnableGPS()
        }else{
            viewModel.start()
        }
    }
    private fun showTipsEnableGPS(){
        AlertDialog.Builder(context)
            .setTitle(R.string.title_dialog_turn_on_gps)
            .setMessage(R.string.message_dialog_turn_on_gps)
            .setPositiveButton(R.string.action_turn_on_gps)
            { _, _ -> startActivityForResult(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS), RC_OPEN_LOCATION_SETTINGS) }
            .show()

    }
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        mMap.isMyLocationEnabled = true
        checkGPSEnableAndStart()
    }
    private fun moveCamera(latLng: LatLng) {
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when(requestCode){
            RC_OPEN_LOCATION_SETTINGS -> {
                checkGPSEnableAndStart()
            }
        }
    }
    companion object {
        const val RC_OPEN_LOCATION_SETTINGS = 1
        @JvmStatic
        fun newInstance() = RecordingFragment()
    }

}