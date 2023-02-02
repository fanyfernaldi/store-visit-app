package com.fanyfernaldi.pitjarustest.presentation.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databases.LocalDatabase
import com.fanyfernaldi.pitjarustest.databinding.ActivityProfileBinding
import com.fanyfernaldi.pitjarustest.domain.Store
import com.fanyfernaldi.pitjarustest.misc.DataConstants
import com.fanyfernaldi.pitjarustest.misc.KeyConstants
import com.fanyfernaldi.pitjarustest.misc.PreferenceHelper
import com.fanyfernaldi.pitjarustest.presentation.login.LoginActivity
import com.fanyfernaldi.pitjarustest.presentation.store.list.StoreListActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPref: PreferenceHelper
    private lateinit var localDb: LocalDatabase
    private var storeList = mutableListOf<Store>()

    private val storeListCallback = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        binding.setStoreData()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localDb = LocalDatabase.getDatabase(this)

        sharedPref = PreferenceHelper(this)

        binding.apply {
            setStoreData()
            bindData()
            initListeners()
        }
    }

    private fun ActivityProfileBinding.bindData() {
        // Profile
        tvProfileName.text = "Mitha Khairulnisa"
        tvProfileRole.text = getString(R.string.label_role_value, "Pegawai")
        tvProfileNik.text = getString(R.string.label_nik_value, "MD00001")

        // Monthly Card
        tvVisitInMonth.text = getString(R.string.label_visit_in_value, "Agustus 2020")
        includeTotalStore.apply {
            ivIcon.setImageResource(R.drawable.ic_circle_error)
            tvLabel.text = getString(R.string.label_total_store)
        }
        includeActualStore.apply {
            ivIcon.setImageResource(R.drawable.ic_circle_check)
            tvLabel.text = getString(R.string.label_actual_store)
        }
        includePercentage.apply {
            ivIcon.setImageResource(R.drawable.ic_circle_swap_vertical)
            tvLabel.text = getString(R.string.label_percentage)
        }

        // Menu
        includeMenuVisit.apply {
            ivIcon.setImageResource(R.drawable.ic_store)
            ivIcon.background.setTint(getColor(R.color.purple_E5ECFF))
            tvLabel.text = getString(R.string.label_visit)
        }
        includeMenuTargetInstall.apply {
            ivIcon.setImageResource(R.drawable.ic_fact_check)
            ivIcon.background.setTint(getColor(R.color.purple_E5ECFF))
            tvLabel.text = getString(R.string.label_target_install_cdfdpc)
        }
        includeMenuDashboard.apply {
            ivIcon.setImageResource(R.drawable.ic_dashboard)
            ivIcon.background.setTint(getColor(R.color.purple_E5ECFF))
            tvLabel.text = getString(R.string.label_dashboard)
        }
        includeMenuTransmissionHistory.apply {
            ivIcon.setImageResource(R.drawable.ic_history)
            ivIcon.background.setTint(getColor(R.color.purple_E5ECFF))
            tvLabel.text = getString(R.string.label_transmission_history)
        }
        includeMenuLogout.apply {
            ivIcon.setImageResource(R.drawable.ic_logout)
            ivIcon.background.setTint(getColor(R.color.purple_E5ECFF))
            tvLabel.text = getString(R.string.label_logout)
        }
    }

    private fun ActivityProfileBinding.initListeners() {
        tvMainMenu.setOnClickListener { toastButtonClicked("Main menu clicked") }
        ivRefresh.setOnClickListener { toastButtonClicked("Refresh icon clicked") }
        includeMenuVisit.root.setOnClickListener {
            val intent = Intent(this@ProfileActivity, StoreListActivity::class.java)
            storeListCallback.launch(intent)
        }
        includeMenuTargetInstall.root.setOnClickListener { toastButtonClicked("Menu target install CDFDPC clicked") }
        includeMenuDashboard.root.setOnClickListener { toastButtonClicked("Menu dashboard clicked") }
        includeMenuTransmissionHistory.root.setOnClickListener { toastButtonClicked("Menu transmission history clicked") }
        includeMenuLogout.root.setOnClickListener {
            sharedPref.put(DataConstants.IS_LOGGED_IN, false)
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }

    private fun ActivityProfileBinding.setStoreData() {
        GlobalScope.launch(Dispatchers.Main) {
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
                    isChecked = storeCache.isChecked,
                    lastVisitDate = storeCache.lastVisitDate
                )
            }
            storeList.clear()
            storeList.addAll(convertedStoreList)
            var totalStore = 0
            var actualStore = 0
            storeList.forEach { store ->
                totalStore++
                if (store.isChecked) {
                    actualStore++
                }
            }
            val percentage = if (totalStore == 0) {
                0
            } else {
                ((actualStore * 100) / totalStore).toDouble().toInt()
            }
            includeTotalStore.tvValue.text = "$totalStore"
            includeActualStore.tvValue.text = "$actualStore"
            includePercentage.tvValue.text = getString(R.string.label_percentage_value, "$percentage%")
        }
    }

    private fun toastButtonClicked(message: String = "Button Clicked") {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}