package com.fanyfernaldi.pitjarustest.caches

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "store_table")
data class StoreCache(
    @PrimaryKey(autoGenerate = true) val storeId: Int = 0,
    @ColumnInfo(name = "store_code") val storeCode: String = "",
    @ColumnInfo(name = "store_name") val storeName: String = "",
    @ColumnInfo(name = "address") val address: String = "",
    @ColumnInfo(name = "dc_id") val dcId: String = "",
    @ColumnInfo(name = "dc_name") val dcName: String = "",
    @ColumnInfo(name = "account_id") val accountId: String = "",
    @ColumnInfo(name = "account_name") val accountName: String = "",
    @ColumnInfo(name = "sub_channel_id") val subChannelId: String = "",
    @ColumnInfo(name = "sub_channel_name") val subChannelName: String = "",
    @ColumnInfo(name = "channel_id") val channelId: String = "",
    @ColumnInfo(name = "channel_name") val channelName: String = "",
    @ColumnInfo(name = "area_id") val areaId: String = "",
    @ColumnInfo(name = "area_name") val areaName: String = "",
    @ColumnInfo(name = "region_id") val regionId: String = "",
    @ColumnInfo(name = "region_name") val regionName: String = "",
    @ColumnInfo(name = "latitude") val latitude: String = "",
    @ColumnInfo(name = "longitude") val longitude: String = "",
    @ColumnInfo(name = "is_checked") val isChecked: Boolean = false,
    @ColumnInfo(name = "last_visit_date") val lastVisitDate: String = "",
)