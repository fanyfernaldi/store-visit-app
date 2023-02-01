package com.fanyfernaldi.pitjarustest.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.fanyfernaldi.pitjarustest.caches.StoreCache
import com.fanyfernaldi.pitjarustest.databases.LocalDatabase
import com.fanyfernaldi.pitjarustest.databinding.ActivityLoginBinding
import com.fanyfernaldi.pitjarustest.domain.Store
import com.fanyfernaldi.pitjarustest.misc.DataConstants
import com.fanyfernaldi.pitjarustest.misc.PreferenceHelper
import com.fanyfernaldi.pitjarustest.presentation.profile.ProfileActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import okhttp3.MultipartBody

class LoginActivity : AppCompatActivity() {

    lateinit var viewModel: LoginViewModel
    private lateinit var binding: ActivityLoginBinding
    private lateinit var localDb: LocalDatabase
    private lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        sharedPref = PreferenceHelper(this)
        val isLoggedIn = sharedPref.getBoolean(DataConstants.IS_LOGGED_IN)
        if (isLoggedIn) {
            navigateToProfile()
            return
        }
        setContentView(binding.root)
        localDb = LocalDatabase.getDatabase(this)

        binding.apply {
            bindData()
            initViewModel()
            initListeners()
        }
    }

    private fun ActivityLoginBinding.bindData() {
        val savedUsername = sharedPref.getString(DataConstants.IDP_USERNAME)
        if (!savedUsername.isNullOrEmpty()) {
            etUsername.setText(savedUsername)
            checkboxKeepUsername.isChecked = true
        }
    }

    private fun ActivityLoginBinding.initViewModel() {
        viewModel = ViewModelProvider(this@LoginActivity).get(LoginViewModel::class.java)
        viewModel.getLoginObserver().observe(this@LoginActivity) {
            if (it == null) {
                renderLoading(false)
                Toast.makeText(this@LoginActivity, "Login Failed", Toast.LENGTH_LONG).show()
            } else if (it.status == "failure") {
                renderLoading(false)
                Toast.makeText(this@LoginActivity, it.message, Toast.LENGTH_LONG).show()
            } else {
                insertStoreData(it.stores)
            }
        }
    }

    private fun ActivityLoginBinding.initListeners() {
        btnLogin.setOnClickListener {
            renderLoading(true)
            login()
        }
    }

    private fun ActivityLoginBinding.renderLoading(isLoading: Boolean = true) {
        if (isLoading) {
            btnLoading.visibility = View.VISIBLE
            btnLogin.visibility = View.GONE
        } else {
            btnLoading.visibility = View.GONE
            btnLogin.visibility = View.VISIBLE
        }
    }

    private fun ActivityLoginBinding.login() {
        val username = etUsername.text.toString().trim()
        val password = etPassword.text.toString().trim()
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("username", username)
            .addFormDataPart("password", password)
            .build()

        viewModel.login(requestBody)
    }

    private fun ActivityLoginBinding.insertStoreData(stores: List<Store>) {
        var storeCacheList = stores.map { store ->
            StoreCache(
                storeId = store.storeId.toInt(),
                storeCode = store.storeCode,
                storeName = store.storeName,
                address = store.address,
                dcId = store.dcId,
                dcName = store.dcName,
                accountId = store.accountId,
                accountName = store.accountName,
                subChannelId = store.subchannelId,
                subChannelName = store.subchannelName,
                channelId = store.channelId,
                channelName = store.channelName,
                areaId = store.areaId,
                areaName = store.areaName,
                regionId = store.regionId,
                regionName = store.regionName,
                latitude = store.latitude,
                longitude = store.longitude,
            )
        }.toMutableList()
        val isDummyDataCreated = sharedPref.getBoolean(DataConstants.IS_DUMMY_DATA_STORE_CREATED)
        if (!isDummyDataCreated) storeCacheList.add(dummyDataStore())
        GlobalScope.launch {
            localDb.storeDao().insertAll(storeCacheList)
            setSharedPref()
            navigateToProfile()
        }
        renderLoading(false)
    }

    private fun ActivityLoginBinding.setSharedPref() {
        sharedPref.put(DataConstants.IS_LOGGED_IN, true)
        if (checkboxKeepUsername.isChecked) {
            sharedPref.put(DataConstants.IDP_USERNAME, etUsername.text.toString().trim())
        } else {
            sharedPref.put(DataConstants.IDP_USERNAME, "")
        }
    }

    private fun navigateToProfile() {
        val intent = Intent(this, ProfileActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    /**
     * I use dummy data because in the login response there are no store located close to me.
     * You can change the latitude and longitude in this domain according to your location.
     * With this dummy data I can test store distances that are more or less than 100m.
     */
    private fun dummyDataStore(): StoreCache {
        sharedPref.put(DataConstants.IS_DUMMY_DATA_STORE_CREATED, true)
        return StoreCache(
            storeCode = "IDM00099",
            storeName = "Dummy data for test",
            address = "Jalan raya 99",
            dcId = "99",
            dcName = "dc 99",
            accountId = "99",
            accountName = "IDM DUMMY 99",
            subChannelId = "99",
            subChannelName = "sub 99",
            channelId = "99",
            channelName = "MT 99",
            areaId = "99",
            areaName = "area 99",
            regionId = "99",
            regionName = "region 99",
            latitude = "-7.389552",
            longitude = "109.055821",
            isChecked = false,
            lastVisitDate = ""
        )
    }
}