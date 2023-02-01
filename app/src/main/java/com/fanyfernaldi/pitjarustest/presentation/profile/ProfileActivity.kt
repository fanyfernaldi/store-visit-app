package com.fanyfernaldi.pitjarustest.presentation.profile

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.fanyfernaldi.pitjarustest.R
import com.fanyfernaldi.pitjarustest.databinding.ActivityProfileBinding
import com.fanyfernaldi.pitjarustest.misc.DataConstants
import com.fanyfernaldi.pitjarustest.misc.PreferenceHelper
import com.fanyfernaldi.pitjarustest.presentation.login.LoginActivity
import com.fanyfernaldi.pitjarustest.presentation.store.StoreListActivity

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var sharedPref: PreferenceHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPref = PreferenceHelper(this)

        binding.apply {
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
            tvValue.text = "150"
            tvLabel.text = getString(R.string.label_total_store)
        }
        includeActualStore.apply {
            ivIcon.setImageResource(R.drawable.ic_circle_check)
            tvValue.text = "150"
            tvLabel.text = getString(R.string.label_actual_store)
        }
        includePercentage.apply {
            ivIcon.setImageResource(R.drawable.ic_circle_swap_vertical)
            tvValue.text = "50%"
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
        tvMainMenu.setOnClickListener {
            // TODO: Move to main menu
        }
        includeMenuVisit.root.setOnClickListener {
            val intent = Intent(this@ProfileActivity, StoreListActivity::class.java)
            startActivity(intent)
        }
        includeMenuLogout.root.setOnClickListener {
            sharedPref.put(DataConstants.IS_LOGGED_IN, false)
            val intent = Intent(this@ProfileActivity, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent)
            finish()
        }
    }
}