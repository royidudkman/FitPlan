package com.example.fitplan.UI.run

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.fitplan.R
import com.example.fitplan.databinding.FragmentRunBinding
import com.example.fitplan.databinding.FragmentRunSaveDetailsBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.MapView
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions

class RunSaveDetails : Fragment(), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var googleMap: GoogleMap
    private val viewModel: RunViewModel by activityViewModels()
    private var _binding: FragmentRunSaveDetailsBinding? = null
    private val binding get() = _binding!!


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRunSaveDetailsBinding.inflate(inflater,container,false)
        binding.distanceTv.text = "You Run : ${viewModel.getTotalDistance()} KM"
        binding.kmMTv.text = "Your Km For Minute : ${viewModel.getKmForMinute().value} km/m "
        binding.timeTv.text = "Total Time : ${viewModel.getTime().value}"

        binding.saveRunBtn.setOnClickListener {
            //TODO SAVE THE DETAILS AND RESET DETAILS
            // save distance
            // save km for minute
            // save time

            viewModel.onStopRunning()
            findNavController().navigate(R.id.runFragment)
        }

        binding.cancelRunBtn.setOnClickListener {
            viewModel.onStopRunning()
            findNavController().navigate(R.id.runFragment)
        }

        // Request location permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            // Permissions already granted, proceed with map initialization
            initializeMap(binding.root, savedInstanceState)
        }

        return binding.root
    }

    private fun initializeMap(rootView: View, savedInstanceState: Bundle?) {
        mapView = rootView.findViewById(R.id.mapView)
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync { googleMap ->
            googleMap.addPolyline(viewModel.drawPath())
            val camera = CameraUpdateFactory.newLatLngZoom(viewModel.getPointPath().last(),15f)
            googleMap.moveCamera(camera)
        }
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map


        // Customize the map as needed
    }

    // Handle permission request result
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    // Permission granted, initialize the map
                    initializeMap(requireView(), null)
                } else {
                    // Permission denied, handle accordingly (e.g., show a message)
                }
                return
            }
            else -> {
                // Handle other permission requests if any
            }
        }
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        super.onPause()
        mapView.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView.onDestroy()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    companion object {
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 123
    }
}

