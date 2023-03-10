package com.fanyfernaldi.pitjarustest.misc

class DataConstants {
    // key name for shared preferences
    companion object {
        const val IS_LOGGED_IN = "IS_LOGGED_IN"
        const val IDP_USERNAME = "IDP_USERNAME"
        // for testing, because lat long from login response is more than 100 meter from my location
        // so I need data that is less than 100 meters from my location
        const val IS_DUMMY_DATA_STORE_CREATED = "IS_DUMMY_DATA_STORE_CREATED"
    }
}

class KeyConstants {
    companion object {
        const val STORE = "STORE"
        const val PHOTO_SELFIE = "PHOTO_SELFIE"
        const val IS_UPDATE_STORE = "IS_FROM_STORE_DETAIL"
    }
}