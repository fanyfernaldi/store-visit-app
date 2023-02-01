package com.fanyfernaldi.pitjarustest.misc

class DataConstants {
    // key name for shared preferences
    companion object {
        const val IS_LOGGED_IN = "IS_LOGGED_IN"
        const val IDP_USERNAME = "IDP_USERNAME"
        const val STORE = "STORE"
        // for testing, because lat long from login response is more than 100 meter from my location
        // so I need data that is less than 100 meters from my location
        const val IS_DUMMY_DATA_STORE_CREATED = "IS_DUMMY_DATA_STORE_CREATED"
    }
}