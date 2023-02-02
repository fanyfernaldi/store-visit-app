# Store Visit App

before running this application, you must declare **MAPS_API_KEY** in the file **local.properties** first
```
<meta-data
    android:name="com.google.android.geo.API_KEY"
    android:value="${MAPS_API_KEY}" />
```
### Note
The latitude and longitude provided in the login response were nowhere close to my location, so I added a dummy data with latitude and longitude around my location. You can replace the dummy data with the latitude and longitude at your location to make the testing process easier

cara merubahnya ada di file **LoginActivity.kt**
```
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
```

# ✨ Video Evidence ✨
Link: [Click here](https://www.loom.com/share/27e53b4f4d5a48fc8d4ac7e95378ca8b)