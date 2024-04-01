package com.example.fitplan.UI.run

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.ContentValues.TAG
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationManager
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale
import androidx.core.content.ContextCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.gms.tasks.Task
import kotlinx.coroutines.delay

class RunViewModel(application: Application) : AndroidViewModel(application) {

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var startLocation: Location? = null
    private var totalDistance: Float = 0f
    private var locationCallback: LocationCallback? = null
    var currentLocation = MutableLiveData<Location?>()
    private var kmPerMinute = MutableLiveData<String>()
    private var pathPositions = mutableListOf<LatLng>()


    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var handler: android.os.Handler = android.os.Handler()
    private var locationHandler: android.os.Handler = android.os.Handler()
    private var runnable: Runnable = Runnable {}
    private var timeFormat = MutableLiveData<String>()

    private var isRunning: Boolean = false

    var isPermissionDeniedBefore = false

    interface ICheckLocationPermissionListener {
        fun onPermissionGranted()
        fun onPermissionDenied()

        fun onLocationOrNetworkDisable()
    }

    fun checkLocationPermissions(
        activity: Activity,
        checkLocationPermission: ICheckLocationPermissionListener
    ) {
        // Use ContextCompat.checkSelfPermission instead of ActivityCompat.checkSelfPermission
        val userChoosePermission = ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity,
            android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED

        if (!checkLocationServiceEnabled(activity.baseContext)) {
            checkLocationPermission.onLocationOrNetworkDisable()
        } else {
            if (userChoosePermission) {
                checkLocationPermission.onPermissionGranted()
            } else {
                isPermissionDeniedBefore = true
                checkLocationPermission.onPermissionDenied()
            }
        }
    }


    fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101
        )
    }

    private fun startLocationUpdates(activity: Activity) {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 5000 // Update interval in milliseconds
            fastestInterval = 2000 // Fastest update interval in milliseconds
            priority = com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult.lastLocation?.let { location ->
                    currentLocation.postValue(location)
                    pathPositions.add(LatLng(location.latitude, location.longitude))
                }
            }
        }

        if (ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity, Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request location permissions if not granted
            return

        }
        task.addOnSuccessListener {
            if (startLocation != null) {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback as LocationCallback, null
                )
            } else {
                startRun(it)
                startLocation?.let {
                    fusedLocationProviderClient.requestLocationUpdates(
                        locationRequest, locationCallback as LocationCallback, null
                    )
                }
            }
        }.addOnFailureListener { exception ->
            // Handle failure to retrieve last known location
            Log.e("eror", "Failed to retrieve last known location: $exception")
            Toast.makeText(activity, "Failed to retrieve last known location", Toast.LENGTH_SHORT)
                .show()
        }
    }


    fun getPointPath(): List<LatLng> {
        return pathPositions
    }

    private fun startRun(startLocation: Location) {
        this.startLocation = startLocation
        totalDistance = 0f
        isRunning = true
    } //define the start location of the device , and the total distance to 0

    fun updateDistance(currentLocation: Location) {
        startLocation?.let { start ->
            // Calculate distance from start location to current location
            val distance = start.distanceTo(currentLocation) / 1000f
            if (totalDistance - distance != totalDistance) {
                totalDistance += distance
                startLocation = currentLocation
            }
        }
    }

    fun getTotalDistance(): Float {
        return String.format("%.2f", totalDistance).toFloat()
    }

    fun getTime(): MutableLiveData<String> {
        return timeFormat
    }

    fun getKmForMinute(): MutableLiveData<String> {
        return kmPerMinute
    }

    private fun startTime() {
        startTime = System.currentTimeMillis() - elapsedTime
        runnable = object : Runnable {
            override fun run() {
                elapsedTime = System.currentTimeMillis() - startTime
                val seconds = (elapsedTime / 1000).toInt()
                val minutes = seconds / 60
                val hours = minutes / 60

                timeFormat.postValue(
                    String.format(
                        "%02d:%02d:%02d", hours, minutes % 60, seconds % 60
                    )
                )
                handler.postDelayed(this, 1000)
            }
        }
        handler.post(runnable)
    }

    private fun pauseTime() {
        handler.removeCallbacks(runnable)
    }

    private fun pauseRun() {
        startLocation = null
        locationCallback?.let { fusedLocationProviderClient.removeLocationUpdates(it) }
        isRunning = false
    }

    private fun resumeRun(activity: Activity) {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity.baseContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        task.addOnSuccessListener {
            startLocation = it
        }

    }

    private fun resetTime() {
        startTime = 0
        elapsedTime = 0
        handler.removeCallbacks(runnable)
        runnable = Runnable { }
        timeFormat.postValue("00:00:00")
    }

    private fun resetRun() {
        startLocation = null
        totalDistance = 0f
        kmPerMinute.postValue("0.00")
        pathPositions.removeAll(pathPositions)
        locationCallback?.let { fusedLocationProviderClient.removeLocationUpdates(it) }
        currentLocation.value = null
    }

    fun onStartRunning(activity: Activity) {
        isRunning = true
        startTime()

        startLocationUpdates(activity)

    }

    fun onResumeRunning(activity: Activity) {
        resumeRun(activity)
        startLocationUpdates(activity)
        startTime()
        isRunning = true
    }

    fun onPauseRunning() {
        pauseTime()
        pauseRun()
        isRunning = false
    }

    fun onStopRunning() {
        resetRun()
        resetTime()
        isRunning = false
    }

    fun kmForMinute() {
        val timeString = timeFormat.value
        if (timeString != null) {
            val timeParts = timeString.split(":")
            if (timeParts.size == 3) {
                val hour = timeParts[0].toFloatOrNull() ?: 0f
                val minutes = timeParts[1].toFloatOrNull() ?: 0f
                val seconds = timeParts[2].toFloatOrNull() ?: 0f
                val totalTimeInMinutes = hour * 60 + minutes + seconds / 60
                if (totalTimeInMinutes != 0f) {
                    val kmForMinutesFloat = totalDistance / totalTimeInMinutes
                    kmPerMinute.postValue(String.format("%.2f", kmForMinutesFloat))
                }
            }
        }
    }

    fun checkLocationServiceEnabled(context: Context): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return isGPSEnabled || isNetworkEnabled
    }

    fun isRunning(): Boolean {
        return isRunning
    }


    fun drawPath(): PolylineOptions {
        val polylineOptions = PolylineOptions()
        polylineOptions.addAll(getPointPath())
        polylineOptions.width(10f)
        polylineOptions.color(Color.RED)
        polylineOptions.geodesic(true)
        for (point in pathPositions) {
            Log.d("PathPoint", "Latitude: ${point.latitude}, Longitude: ${point.longitude}")
        }
        return polylineOptions
    }

}