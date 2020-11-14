
import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices

class LocationRepository(private val context: Context): LiveData<LocationModel>() {
    private var fusedLocationClient = LocationServices.getFusedLocationProviderClient(context)
    fun stopUpdates(){
        fusedLocationClient.removeLocationUpdates(locationCallback)
    }
    @SuppressLint("MissingPermission")
    fun startUpdates(){
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                location?.also {
                    Log.i("location", it.latitude.toString())
                    setLocationData(it)
                }
            }
        startLocationUpdates()
    }

    private fun setLocationData(location: Location) {
        value = LocationModel(
            longitude = location.longitude,
            latitude = location.latitude
        )
    }

    companion object {
        val locationRequest: LocationRequest = LocationRequest.create().apply {
            interval = 20000
            fastestInterval = 10000
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
        }
    }

    private val locationCallback = object : LocationCallback() {
        override fun onLocationResult(locationResult: LocationResult?) {
            locationResult ?: return
            for (location in locationResult.locations) {
                setLocationData(location)
            }
        }
    }
    @SuppressLint("MissingPermission")
    private fun startLocationUpdates() {
        fusedLocationClient.requestLocationUpdates(
            locationRequest,
            locationCallback,
            null
        )
    }
}