@file:Suppress("DEPRECATION")

package com.example.ajubarider

import android.Manifest
import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.os.IBinder
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import com.example.ajubarider.api.Network
import com.google.android.gms.location.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LocationService : Service(){
    val LOCATION_SERVIS_ID=175
    val ACTION_STOP_LOCATION_SERVICE="stopLocationService"
    @Inject
    lateinit var api:Network
    var phone= ""
    lateinit var sharedPreferences: SharedPreferences



    private var locationCallback:LocationCallback = object: LocationCallback() {
        override fun onLocationResult(p0: LocationResult?) {
            super.onLocationResult(p0)
            if(p0!=null && p0.lastLocation!=null){
                Log.d("LocationSrrrrrrr",p0.lastLocation.latitude.toString())

                try{
                    CoroutineScope(Main).launch { api.updateLocation(phone, Location(phone,p0.lastLocation.latitude,p0.lastLocation.longitude))
                    }
                }
                catch(err:Exception){
                    Log.d("ERROR",err.toString())

                }
            }
        }

    }



    fun startLocationService(){
        val CHANNEL_ID = BuildConfig.APPLICATION_ID + "_notification_id"
        val CHANNEL_NAME = BuildConfig.APPLICATION_ID + "_notification_name"
        val NOTIFICATION_ID = 100

        val builder: NotificationCompat.Builder
        sharedPreferences= this.getSharedPreferences(
                "appSharedPrefs",
                Context.MODE_PRIVATE
        )
        phone=sharedPreferences.getString("phone", "none").toString()
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val importance = NotificationManager.IMPORTANCE_NONE
            assert(notificationManager != null)
            var mChannel = notificationManager.getNotificationChannel(CHANNEL_ID)
            if (mChannel == null) {
                mChannel = NotificationChannel(CHANNEL_ID, CHANNEL_NAME, importance)
                notificationManager.createNotificationChannel(mChannel)
            }
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
            builder.setSmallIcon(R.mipmap.sym_def_app_icon)
                    .setContentTitle("Ajuba Rider")

        } else {
            builder = NotificationCompat.Builder(this, CHANNEL_ID)
            builder.setSmallIcon(R.mipmap.sym_def_app_icon)
                    .setContentTitle("Ajuba Rider")

        }
        var locationRequest = LocationRequest()
        locationRequest.setInterval(3000)
        locationRequest.setFastestInterval(1000)
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return
        }
        LocationServices.getFusedLocationProviderClient(this)
                .requestLocationUpdates(locationRequest,locationCallback, Looper.getMainLooper())
        //startForeground(NOTIFICATION_ID, builder.build())
        startForeground(NOTIFICATION_ID,builder.build())



    }

    fun stopLocationService(){
        LocationServices.getFusedLocationProviderClient(this)
                .removeLocationUpdates(locationCallback)
        stopForeground(true)
        stopSelf()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        if(intent!= null){
            var action=intent.action
            if(action!= null && action.equals("startLocationService")){
                startLocationService()


            }
            else if(action.equals(ACTION_STOP_LOCATION_SERVICE)){
                stopLocationService()
            }

        }
        return START_REDELIVER_INTENT
    }



    override fun onBind(intent: Intent?): IBinder? {
        throw UnsupportedOperationException("Not yet implemented")
    }

    override fun onDestroy() {
        super.onDestroy()

    }

}