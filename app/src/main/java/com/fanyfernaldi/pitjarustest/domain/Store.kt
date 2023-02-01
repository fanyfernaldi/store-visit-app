package com.fanyfernaldi.pitjarustest.domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Store (
    val storeId: String = "",
    val storeCode: String = "",
    val storeName: String = "",
    val address: String = "",
    val dcId: String = "",
    val dcName: String = "",
    val accountId: String = "",
    val accountName: String = "",
    val subchannelId: String = "",
    val subchannelName: String = "",
    val channelId: String = "",
    val channelName: String = "",
    val areaId: String = "",
    val areaName: String = "",
    val regionId: String = "",
    val regionName: String = "",
    val latitude: String = "",
    val longitude: String = "",
    val distance: String = "",
    val isChecked: Boolean = false
) : Parcelable