package com.example.ajubarider

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.example.ajubarider.api.Network
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_maps.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity2 : AppCompatActivity(), OnMapReadyCallback {

    @Inject
    lateinit var api: Network

    private lateinit var map: GoogleMap
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var lastLocation: Location

    lateinit var sharedPreferences: SharedPreferences

    var p:String ="7009516346"
    var id=0







    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)
        sharedPreferences= this.getSharedPreferences(
            "sharedPrefFile",
            Context.MODE_PRIVATE
        )
        if(intent.getStringExtra("intent")=="1"){
            button.text="Accept"
            button.setOnClickListener{
                CoroutineScope(Dispatchers.IO).launch {
                    val response = intent.getIntExtra("id",0)?.let { it1 -> api.acceptProcessingOrder(it1,id) }
                    if (response?.body()?.message == "SUCCESS") {

                        finish()
                    } else {

                        finish()

                    }
                }
            }
        }
        else{
            button.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = intent.getIntExtra("id", 0)?.let { it1 -> api.orderDelivered(it1,id) }
                    if (response?.body()?.message == "SUCCESS") {

                        finish()
                    } else {

                        finish()

                    }
                }
            }

            button2.setOnClickListener {
                CoroutineScope(Dispatchers.IO).launch {
                    val response = intent.getIntExtra("id", 0).let { it1 -> api.orderDeclined(it1,id) }
                    if (response?.body()?.message == "SUCCESS") {

                        finish()
                    } else {

                        finish()

                    }
                }

            }

        }
        fabCall.setOnClickListener {
            val dialIntent = Intent(Intent.ACTION_DIAL)
            dialIntent.data = Uri.parse("tel:" + intent.getStringExtra("phone"))
            startActivity(dialIntent)
        }

        p= sharedPreferences.getString("phone", "none").toString()
        id= sharedPreferences.getInt("DbID", 0)



        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


    }
    companion object {
        const val LOCATION_PERMISSION_REQUEST_CODE = 1
    }private fun setUpMap() {
        if (ActivityCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
            return
        }

        map.isMyLocationEnabled = true

        fusedLocationClient.lastLocation.addOnSuccessListener(this) { location ->
            // Got last known location. In some rare situations this can be null.
            if (location != null) {
                lastLocation = location
                val currentLatLng = LatLng(intent.getDoubleExtra("lat", 0.0),
                    intent.getDoubleExtra("lon",0.0 ) )
                placeMarkerOnMap(currentLatLng)
                map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 15f))
            }
        }
    }
    private fun placeMarkerOnMap(location: LatLng) {
        // 1
        var markerOptions = MarkerOptions().position(location)

        // 2
        markerOptions.draggable(false)
        map.addMarker(markerOptions)

    }override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap

        map.uiSettings.isZoomControlsEnabled = true


        setUpMap()



    }




}