package com.fanyfernaldi.pitjarustest.presentation.store.verification

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databinding.ActivityStoreVerificationBinding
import com.fanyfernaldi.pitjarustest.domain.Store
import com.fanyfernaldi.pitjarustest.misc.KeyConstants
import com.fanyfernaldi.pitjarustest.presentation.store.detail.StoreDetailActivity

class StoreVerificationActivity : AppCompatActivity() {

    private var store: Store? = null
    private var photoSelfie: Bitmap? = null
    private var isLocationSuitable = false
    private lateinit var binding: ActivityStoreVerificationBinding

    private val storeDetailCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == Activity.RESULT_OK && it.data != null) {
            val isUpdateStore = it.data?.getBooleanExtra(KeyConstants.IS_UPDATE_STORE, false)
            isUpdateStore?.let { value ->
                navigateToPreviousActivityWithData(value)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreVerificationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        store = intent.getParcelableExtra(KeyConstants.STORE)
        store?.let {
            isLocationSuitable = if (it.distance.isNotEmpty()) it.distance.toDouble().toInt() < 100
            else false
        }

        binding.apply {
            bindData()
            initListeners()
        }
    }

    private fun ActivityStoreVerificationBinding.bindData() {
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

    private fun ActivityStoreVerificationBinding.initListeners() {
        ivBack.setOnClickListener { this@StoreVerificationActivity.onBackPressed() }
        btnNavigation.setOnClickListener { toastButtonClicked("Button navigation clicked") }
        btnGpsFixed.setOnClickListener { toastButtonClicked("Button GPS clicked") }
        btnReset.setOnClickListener { toastButtonClicked("Button reset clicked") }
        btnPhoto.setOnClickListener {
            openCamera()
        }
        btnNoVisit.setOnClickListener { this@StoreVerificationActivity.onBackPressed() }
        btnVisit.setOnClickListener {
            if (!isLocationSuitable) {
                toastButtonClicked("Jarak terlalu jauh!")
                return@setOnClickListener
            }
            if (photoSelfie == null) {
                toastButtonClicked("Ambil foto terlebih dahulu!")
                return@setOnClickListener
            }
            navigateToStoreDetail()
        }
    }

    private fun navigateToStoreDetail() {
        val intent = Intent(this, StoreDetailActivity::class.java)
        store?.let {
            intent.putExtra(KeyConstants.STORE, it)
        }
        intent.putExtra(KeyConstants.PHOTO_SELFIE, photoSelfie)
        storeDetailCallback.launch(intent)
    }

    private fun navigateToPreviousActivityWithData(isUpdateStore: Boolean = false) {
        val resultIntent = Intent()
        resultIntent.putExtra(KeyConstants.IS_UPDATE_STORE, isUpdateStore)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun openCamera() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(Manifest.permission.CAMERA),
                CAMERA_REQUEST_CODE
            )
        } else {
            val i = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(i, ACTION_IMAGE_CAPTURE_REQUEST_CODE)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ACTION_IMAGE_CAPTURE_REQUEST_CODE && data != null) {
            photoSelfie = data.getParcelableExtra("data")
            binding.ivPhoto.setImageBitmap(photoSelfie)
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