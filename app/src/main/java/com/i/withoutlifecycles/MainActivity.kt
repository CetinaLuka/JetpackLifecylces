package com.i.withoutlifecycles

import LocationRepository
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    lateinit var locationRepository: LocationRepository
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationRepository = LocationRepository(this)
        locationRepository.observe(this, Observer { location ->
            latitude.text = location.latitude.toString()
            longitude.text = location.longitude.toString()
        })
    }

    override fun onStart() {
        super.onStart()
        Toast.makeText(this, "Started location updates", Toast.LENGTH_SHORT).show()
        locationRepository.startUpdates()
    }
    override fun onPause() {
        super.onPause()
        Toast.makeText(this, "Stopped location updates", Toast.LENGTH_SHORT).show()
        locationRepository.stopUpdates()
    }
}