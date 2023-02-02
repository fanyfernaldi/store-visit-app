# Store Visit App

before running this application, you must declare **MAPS_API_KEY** in the file **local.properties** first
```
MAPS_API_KEY=your_maps_api_key
```
### Note
The latitude and longitude provided in the login response were nowhere close to my location, so I added a dummy data with latitude and longitude around my location. You can replace the dummy data with the latitude and longitude at your location to make the testing process easier

change latitude and longitude dummy data in **LoginActivity.kt** file
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
Link: [Click here](https://www.loom.com/share/4466f7519d5a40cfb3fe56196725b047)