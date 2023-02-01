package com.fanyfernaldi.pitjarustest.presentation.store

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Color
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.toBitmap
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databases.LocalDatabase
import com.fanyfernaldi.pitjarustest.databinding.ActivityStoreListBinding
import com.fanyfernaldi.pitjarustest.domain.Store
import com.fanyfernaldi.pitjarustest.misc.AppUtils
import com.fanyfernaldi.pitjarustest.misc.toFormattedDate
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class StoreListActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var mMap: GoogleMap
    private lateinit var binding: ActivityStoreListBinding
    private lateinit var localDb: LocalDatabase
    private var storeList = mutableListOf<Store>()
    private var isMapReady = false
    private var isFirstLoad = false
    private var mapCircle: Circle? = null
    private var myLatitude = 0.0
    private var myLongitude = 0.0
    private var storeListAdapter: StoreListAdapter = StoreListAdapter(storeList)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityStoreListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localDb = LocalDatabase.getDatabase(this)
        binding.tvListVisitDate.text =
            getString(R.string.label_list_visit_date_value, Date().toFormattedDate("dd-MM-yyyy"))

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment = supportFragmentManager
            .findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        binding.apply {
            setRecyclerViewData()
            initListeners()
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        isMapReady = true

        Log.w("LOGW_TEST", "i am called 1")
        binding.setMyLocation()
        enableUserLocation()
    }

    @SuppressLint("MissingPermission")
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // We have the permission
                binding.setMyLocation()
                mMap.isMyLocationEnabled = true
            } else {
                // We do not have the permission
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun ActivityStoreListBinding.setMyLocation() {

        Log.w("LOGW_TEST", "i am called 2")
        val locationManager = getSystemService(LOCATION_SERVICE) as LocationManager
        if (ActivityCompat.checkSelfPermission(
                this@StoreListActivity,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this@StoreListActivity,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_LOCATION_ACCESS_REQUEST_CODE
            )
            return
        }

        Log.w("LOGW_TEST", "i am called 3")
        locationManager.requestLocationUpdates(
            LocationManager.GPS_PROVIDER, 500, 0f
        ) { p0 ->
            Log.w("LOGW_TEST", "i am called 4")
            if (isMapReady) {
                myLatitude = p0.latitude
                myLongitude = p0.longitude
                var myLocation = LatLng(myLatitude, myLongitude)
                mapCircle?.remove()
                mapCircle = mMap.addCircle(
                    CircleOptions()
                        .center(myLocation)
                        .radius(100.0)
                        .strokeColor(Color.parseColor("#1A005EEF"))
                        .fillColor(Color.parseColor("#1A005EEF"))
                )
                setAdapter(storeList)
                if (!isFirstLoad) {
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, 17f))
                    isFirstLoad = true
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    private fun enableUserLocation() {
        // 1. Check if permissions are granted, if so, enable the my location layer
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            mMap.isMyLocationEnabled = true
            return
        }

        // 2. We need to show user a dialog for displaying the permission is needed and then ask for the permission
        if (ActivityCompat.shouldShowRequestPermissionRationale(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                FINE_LOCATION_ACCESS_REQUEST_CODE
            )
            return
        }

        // 3. Otherwise, request permission
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
            ),
            FINE_LOCATION_ACCESS_REQUEST_CODE
        )
    }

    private fun setStoreListMarker(data: List<Store>) {
        data.forEach { store ->
            if (!store.latitude.isNullOrEmpty() || !store.longitude.isNullOrEmpty()) {
                val latLng = LatLng(store.latitude.toDouble(), store.longitude.toDouble())
                val markerOptions = MarkerOptions().position(latLng).title(store.storeName)
                var bitmapIcon =
                    AppCompatResources.getDrawable(this, R.drawable.ic_location_on)!!.toBitmap()
                markerOptions.icon(BitmapDescriptorFactory.fromBitmap(bitmapIcon))
                mMap.addMarker(markerOptions)
            }
        }
    }

    private fun ActivityStoreListBinding.initListeners() {
        ivBack.setOnClickListener {
            this@StoreListActivity.onBackPressed()
        }
    }

    private fun ActivityStoreListBinding.setAdapter(data: List<Store>) {
        val storeListWithDistance = data.map {
            it.copy(
                distance = AppUtils.getDistanceFromTwoLatLong(
                    myLatitude,
                    myLongitude,
                    if (it.latitude.isEmpty()) 0.0 else it.latitude.toDouble(),
                    if (it.longitude.isEmpty()) 0.0 else it.longitude.toDouble()
                )
            )
        }
        storeListAdapter = StoreListAdapter(storeListWithDistance)
        rvStore.apply {
            layoutManager = LinearLayoutManager(this@StoreListActivity)
            adapter = storeListAdapter
        }
        storeListAdapter.setOnItemClickCallback(object : StoreListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Store) {
                navigateToDetail(data)
            }
        })
    }

    private fun ActivityStoreListBinding.setRecyclerViewData() {
        GlobalScope.launch(Dispatchers.Main) {
            storeList.clear()
            val storesCacheFromLocalDb = localDb.storeDao().getAll()
            val convertedStoreList = storesCacheFromLocalDb.map { storeCache ->
                Store(
                    storeId = storeCache.storeId.toString(),
                    storeCode = storeCache.storeCode,
                    storeName = storeCache.storeName,
                    address = storeCache.address,
                    dcId = storeCache.dcId,
                    dcName = storeCache.dcName,
                    accountId = storeCache.accountId,
                    accountName = storeCache.accountName,
                    subchannelId = storeCache.subChannelId,
                    subchannelName = storeCache.subChannelName,
                    channelId = storeCache.channelId,
                    channelName = storeCache.channelName,
                    areaId = storeCache.areaId,
                    areaName = storeCache.areaName,
                    regionId = storeCache.regionId,
                    regionName = storeCache.regionName,
                    latitude = storeCache.latitude,
                    longitude = storeCache.longitude,
                    isChecked = storeCache.isChecked
                )
            }
            storeList.addAll(convertedStoreList)
            setStoreListMarker(storeList)
            setAdapter(storeList)
        }
    }

    private fun navigateToDetail(store: Store) {
        Toast.makeText(this@StoreListActivity, "Navigate to detail ${store.storeName}", Toast.LENGTH_LONG).show()
    }

    companion object {
        const val FINE_LOCATION_ACCESS_REQUEST_CODE = 1001
    }
}