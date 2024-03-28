package com.example.fitplan.UI.run

import android.Manifest
import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationRequest
import androidx.core.app.ActivityCompat
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.Task

class RunViewModel(application: Application) : AndroidViewModel(application) {

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(application)
    private var startLocation: Location? = null
    private var totalDistance: Float = 0f
    private lateinit var locationCallback: LocationCallback
    var currentLocation = MutableLiveData<Location?>()
    private var kmPerMinute = MutableLiveData<String>()


    private var startTime: Long = 0
    private var elapsedTime: Long = 0
    private var handler: android.os.Handler = android.os.Handler()
    private var runnable: Runnable = Runnable {}
    private var timeFormat = MutableLiveData<String>()

    private var isRunning: Boolean = false
    private var pausedLocation: Location? = null

    fun checkLocationPermissions(activity: Activity): Boolean {
        return ActivityCompat.checkSelfPermission(
            activity.baseContext, android.Manifest.permission.ACCESS_FINE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
            activity.baseContext, android.Manifest.permission.ACCESS_COARSE_LOCATION
        ) != PackageManager.PERMISSION_GRANTED
    }

    private fun requestPermissions(activity: Activity) {
        ActivityCompat.requestPermissions(
            activity, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), 101
        )
    }

    private fun startLocationUpdates(activity: Activity) {
        val task: Task<Location> = fusedLocationProviderClient.lastLocation
        val locationRequest = com.google.android.gms.location.LocationRequest.create().apply {
            interval = 10000 // Update interval in milliseconds
            fastestInterval = 3000 // Fastest update interval in milliseconds
            priority = LocationRequest.QUALITY_HIGH_ACCURACY
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationResult?.lastLocation?.let { location ->
                    currentLocation.postValue(location)
                }
            }
        }
        if (ActivityCompat.checkSelfPermission(
                activity.baseContext, Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                activity.baseContext,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissions(activity = activity)
        }
        task.addOnSuccessListener {
            if (startLocation != null) {
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback, null
                )
            } else {
                startRun(it)
                fusedLocationProviderClient.requestLocationUpdates(
                    locationRequest, locationCallback, null
                )
            }
        }
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

    fun getKmForMinute():MutableLiveData<String>{
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
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
            pausedLocation = null
            isRunning = true
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
        fusedLocationProviderClient.removeLocationUpdates(locationCallback)
        currentLocation.value = null
    }

    fun onStartRunning(activity: Activity) {
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

     fun kmForMinute(){
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
                    kmPerMinute.postValue(String.format("%.2f",kmForMinutesFloat))
                }
            }
        }
    }

    fun isRunning(): Boolean {
        return isRunning
    }


}