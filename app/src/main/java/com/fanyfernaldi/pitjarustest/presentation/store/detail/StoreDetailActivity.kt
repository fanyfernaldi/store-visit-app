package com.fanyfernaldi.pitjarustest.presentation.store.detail

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databinding.ActivityStoreDetailBinding
import com.fanyfernaldi.pitjarustest.domain.Store
import com.fanyfernaldi.pitjarustest.misc.DataConstants

class StoreDetailActivity : AppCompatActivity() {

    private var store: Store? = null
    private var picture: Bitmap? = null
    private var isLocationSuitable = false
    private lateinit var binding: ActivityStoreDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        store = intent.getParcelableExtra(DataConstants.STORE)
        store?.let {
            isLocationSuitable = if (it.distance.isNotEmpty()) it.distance.toDouble().toInt() < 100
            else false
        }

        binding.apply {
            bindData()
            initListeners()
        }
    }

    private fun ActivityStoreDetailBinding.bindData() {
        ivBack.setColorFilter(getColor(R.color.white))
        ivLocation.setColorFilter(getColor(R.color.orange_FF9104))
        ivStore.setColorFilter(getColor(R.color.orange_FF9104))
        ivLastVisit.setColorFilter(getColor(R.color.orange_FF9104))

        store?.let {
            tvLocation.text = if (isLocationSuitable)
                getString(R.string.label_location_is_suitable)
            else
                getString(R.string.label_location_is_not_suitable)
            tvStoreName.text = it.storeName
            tvStoreAddress.text = it.address
            tvOutletType.text = getString(R.string.label_colon_value, it.dcName)
            tvDisplayType.text = getString(R.string.label_colon_value, it.channelName)
            tvDisplaySubType.text = getString(R.string.label_colon_value, it.subchannelName)
            tvErtm.text = getString(R.string.label_colon_value, "Ya")
            tvPareto.text = getString(R.string.label_colon_value, "Ya")
            tvEMerchandising.text = getString(R.string.label_colon_value, "Ya")
            tvLastVisitDate.text = it.lastVisitDate.ifEmpty { "-" }
        }
    }

    private fun ActivityStoreDetailBinding.initListeners() {
        ivBack.setOnClickListener { this@StoreDetailActivity.onBackPressed() }
        btnNavigation.setOnClickListener { toastButtonClicked() }
        btnGpsFixed.setOnClickListener { toastButtonClicked() }
        btnReset.setOnClickListener { toastButtonClicked() }
        btnPhoto.setOnClickListener {
            openCamera()
        }
        btnNoVisit.setOnClickListener { this@StoreDetailActivity.onBackPressed() }
        btnVisit.setOnClickListener {
            if (!isLocationSuitable) {
                toastButtonClicked("Jarak terlalu jauh!")
                return@setOnClickListener
            }
            if (picture == null) {
                toastButtonClicked("Ambil foto terlebih dahulu!")
                return@setOnClickListener
            }
            // TODO: Navigate to main menu
        }
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_REQUEST_CODE)
        } else {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, ACTION_IMAGE_CAPTURE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_IMAGE_CAPTURE_REQUEST_CODE && data != null) {
            picture = data.getParcelableExtra("data")
            binding.ivPhoto.setImageBitmap(picture)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, ACTION_IMAGE_CAPTURE_REQUEST_CODE)
        }
    }

    private fun toastButtonClicked(message: String = "Button Clicked") {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }


    companion object {
        const val CAMERA_REQUEST_CODE = 1002
        const val ACTION_IMAGE_CAPTURE_REQUEST_CODE = 1003
    }
}