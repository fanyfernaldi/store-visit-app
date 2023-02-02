package com.fanyfernaldi.pitjarustest.presentation.store.detail

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databases.LocalDatabase
import com.fanyfernaldi.pitjarustest.databinding.ActivityStoreDetailBinding
import com.fanyfernaldi.pitjarustest.domain.Dashboard
import com.fanyfernaldi.pitjarustest.domain.Store
import com.fanyfernaldi.pitjarustest.misc.KeyConstants
import com.fanyfernaldi.pitjarustest.misc.toFormattedDate
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.*

class StoreDetailActivity : AppCompatActivity() {

    private var photoSelfie: Bitmap? = null
    private var dashboardList = mutableListOf<Dashboard>()
    private var dashboardAdapter: DashboardAdapter = DashboardAdapter(dashboardList)
    private lateinit var store: Store
    private lateinit var localDb: LocalDatabase
    private lateinit var binding: ActivityStoreDetailBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoreDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        localDb = LocalDatabase.getDatabase(this)

        store = intent.getParcelableExtra(KeyConstants.STORE) ?: Store()
        photoSelfie = intent.getParcelableExtra(KeyConstants.PHOTO_SELFIE)

        binding.apply {
            bindData()
            initListeners()
            initDashboardAdapter(getDummyDataDashboardList())
        }
    }

    private fun ActivityStoreDetailBinding.bindData() {
        tvMarqueeText.isSelected = true

        // Store Information
        ivPhotoSelfie.setImageBitmap(photoSelfie)
        tvStoreId.text = store.storeId
        tvStoreName.text = store.storeName
        tvStoreCluster.text = getString(R.string.label_cluster_value, "Small")
        val storeDescription = "TT Regular - ERTM Big - Pareto"
        tvStoreDescription.text = storeDescription

        // Menu
        includeMenuInformation.apply {
            ivIcon.setImageResource(R.drawable.ic_italic)
            ivIcon.background.setTint(getColor(R.color.blue_4C7AE7))
            tvLabel.text = getString(R.string.label_information)
        }
        includeMenuProductCheck.apply {
            ivIcon.setImageResource(R.drawable.ic_local_drink)
            ivIcon.background.setTint(getColor(R.color.orange_FF9104))
            tvLabel.text = getString(R.string.label_product_check)
        }
        includeMenuSkuPromo.apply {
            ivIcon.setImageResource(R.drawable.ic_shopping_cart)
            ivIcon.background.setTint(getColor(R.color.blue_4C7AE7))
            tvLabel.text = getString(R.string.label_sku_promo)
        }
        includeMenuOosSummary.apply {
            ivIcon.setImageResource(R.drawable.ic_local_drink)
            ivIcon.background.setTint(getColor(R.color.red_C51F61))
            tvLabel.text = getString(R.string.label_oos_summary)
        }
        includeMenuStoreInvestment.apply {
            ivIcon.setImageResource(R.drawable.ic_show_chart)
            ivIcon.background.setTint(getColor(R.color.blue_285C9B))
            tvLabel.text = getString(R.string.label_store_investment)
        }

        if (store.isChecked) {
            ivPerfectStore.background.setTint(getColor(R.color.green_51CF66))
            ivPerfectStore.setImageResource(R.drawable.ic_check)
            includeMenuProductCheck.ivChecked.isVisible = true
        } else {
            ivPerfectStore.background.setTint(getColor(R.color.red))
            ivPerfectStore.setImageResource(R.drawable.ic_close)
        }
    }

    private fun ActivityStoreDetailBinding.initListeners() {
        ivBack.setOnClickListener { this@StoreDetailActivity.onBackPressed() }
        dashboardAdapter.setOnItemClickCallback(object : DashboardAdapter.OnItemClickCallback {
            override fun onItemClicked(data: Dashboard) {
                toastButtonClicked("${data.title} clicked")
            }
        })
        ivAppBarAction.setOnClickListener { toastButtonClicked("appBar clicked") }
        includeMenuInformation.root.setOnClickListener { toastButtonClicked("Menu information clicked") }
        includeMenuProductCheck.root.setOnClickListener { toastButtonClicked("Menu product check clicked") }
        includeMenuSkuPromo.root.setOnClickListener { toastButtonClicked("Menu SKU promo clicked") }
        includeMenuOosSummary.root.setOnClickListener { toastButtonClicked("Menu ringkasan OOS clicked") }
        includeMenuStoreInvestment.root.setOnClickListener { toastButtonClicked("Menu store investment clicked") }
        btnDone.setOnClickListener {
            updateStoreData()
        }
    }

    private fun ActivityStoreDetailBinding.initDashboardAdapter(data: List<Dashboard>) {
        dashboardAdapter = DashboardAdapter(data)
        rvDashboardCard.apply {
            layoutManager =
                LinearLayoutManager(this@StoreDetailActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = dashboardAdapter
        }
    }

    private fun updateStoreData() {
        GlobalScope.launch {
            localDb.storeDao().update(
                isChecked = true,
                lastVisitDate = Date().toFormattedDate("dd-MM-yyyy"),
                id = store.storeId
            )
            navigateToPreviousActivityWithData()
        }
    }

    private fun navigateToPreviousActivityWithData() {
        val resultIntent = Intent()
        resultIntent.putExtra(KeyConstants.IS_UPDATE_STORE, true)
        setResult(Activity.RESULT_OK, resultIntent)
        finish()
    }

    private fun toastButtonClicked(message: String = "Button Clicked") {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun getDummyDataDashboardList(): List<Dashboard> {
        return listOf(
            Dashboard(
                title = "OSA",
                date = "September 2020",
                percentage = "50%",
                target = "40",
                achievement = "20",
                color = "#f1b117",
            ),
            Dashboard(
                title = "NPD",
                date = "September 2020",
                percentage = "80%",
                target = "100",
                achievement = "80",
                color = "#1dcabd",
            ),
            Dashboard(
                title = "XYZ",
                date = "September 2020",
                percentage = "100%",
                target = "100",
                achievement = "10",
                color = "#3473b2",
            )
        )
    }
}