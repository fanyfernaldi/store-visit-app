package com.fanyfernaldi.pitjarustest.misc

import android.location.Location

class AppUtils {
    companion object {
        fun getDistanceFromTwoLatLong(
            latA: Double,
            longA: Double,
            latB: Double,
            longB: Double
        ): String {
            if (latA == 0.0 || longA == 0.0 || latB == 0.0 || longB == 0.0) {
                return "0.0"
            } else {
                val locationA = Location("LOCATION_A").apply {
                    latitude = latA
                    longitude = longA
                }

                val locationB = Location("LOCATION_B").apply {
                    latitude = latB
                    longitude = longB
                }

                return locationA.distanceTo(locationB).toInt().toString()
            }
        }
    }
}