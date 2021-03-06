@file:Suppress("DEPRECATION")

package com.example.ajubarider

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.*
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.example.ajubarider.api.Network
import com.google.android.gms.location.*
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.*
import javax.inject.Inject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var api:Network


    lateinit var sharedPreferences: SharedPreferences
    var p="7009516346"
    var id=0

    private val locationPermissionCode = 2
    var location:Boolean=true


    lateinit var fusedLocationClient: FusedLocationProviderClient
    private var locationRequest: LocationRequest? = null
    private var locationCallback: LocationCallback? = null
    var handler:Handler ?= null
    var runnable: Runnable ?= null
    var adapter:ViewPagerAdapter ?= null
    var toBeAccepted:ArrayList<Order> ?= ArrayList()
    var toBeDelivered:ArrayList<Order> ?= ArrayList()
    var accepted:ArrayList<Order> ?= ArrayList()



    override fun onResume() {
        super.onResume()
        reload()


    }





    override fun onCreate(savedInstanceState: Bundle?) {
        super<AppCompatActivity>.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences= this.getSharedPreferences(
                "appSharedPrefs",
                Context.MODE_PRIVATE
        )

        adapter = accepted?.let { toBeDelivered?.let { it1 -> toBeAccepted?.let {
            it2 -> ViewPagerAdapter(it1, it2, it,this) } } }
        vpHome.adapter=adapter
        TabLayoutMediator(tbHome, vpHome){ tab, position->
            if(position==0){
                tab.text="Delivery"
            }
            else if(position==1){
                tab.text="Processing"
            }
            else{
                tab.text="Accepted"
            }

        }.attach()











        fabPause.visibility=View.GONE
        fabPlay.visibility= View.VISIBLE
        fabPlay.setOnClickListener {
            if (ActivityCompat.checkSelfPermission(this,
                            android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this,
                        arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION), MainActivity2.LOCATION_PERMISSION_REQUEST_CODE)
                recreate()
            }
            location=true
            fabPause.visibility=View.VISIBLE
            fabPlay.visibility= View.GONE
            updateLocation()

        }
        fabPause.setOnClickListener {
            location=false
            fabPause.visibility=View.GONE
            fabPlay.visibility= View.VISIBLE




        }

        strReload.setOnRefreshListener {
            reload()
            strReload.isRefreshing=false
        }



        p= sharedPreferences.getString("phone", "none").toString()
        id=sharedPreferences.getInt("DbID",0)



    }

    private fun updateLocation() {
        if(location){
            var intent: Intent = Intent(applicationContext, LocationService::class.java)
            intent.action="startLocationService"
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
                applicationContext.startForegroundService(intent)
            }
            else{

                applicationContext.startService(intent)
            }
        }
        else {

                var intent: Intent = Intent(applicationContext, LocationService::class.java)
                intent.action="stopLocationService"
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O ){
                applicationContext.startForegroundService(intent)
            }
            else{
                applicationContext.startService(intent)
            }



        }
    }


    fun reload(){
        CoroutineScope(Dispatchers.IO).launch {
            try{
                toBeDelivered?.clear()
                toBeAccepted?.clear()
                accepted?.clear()
                val list = api.getOrders(id).body()

                api.getProcessingOrders().body()?.let { toBeAccepted?.addAll(it) }
                toBeAccepted?.forEach {
                    if(it.deliveryBoyPhone==p)
                        accepted?.add(it)
                }

                list?.forEach {
                    if(it.status=="B"){
                        toBeDelivered?.add(it)
                    }

                }


                Log.d("list", list.toString())
                withContext(Dispatchers.Main) {



                        adapter?.notifyDataSetChanged()


                }


            }
            catch (err: Exception){
                Log.d("connect MainActivity", err.toString())

            }
        }
    }




}





