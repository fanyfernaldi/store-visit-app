package com.fanyfernaldi.pitjarustest.daos

import androidx.room.*
import com.fanyfernaldi.pitjarustest.caches.StoreCache

@Dao
interface StoreDao {
    @Query("SELECT * FROM store_table")
    suspend fun getAll(): List<StoreCache>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(storeCache: StoreCache)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    @JvmSuppressWildcards
    suspend fun insertAll(storeCacheList: List<StoreCache>)

    @Delete
    suspend fun delete(storeCache: StoreCache)

    @Query("DELETE FROM store_table")
    suspend fun deleteAll()
}