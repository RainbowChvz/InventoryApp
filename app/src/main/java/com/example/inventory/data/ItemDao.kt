package com.example.inventory.data

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertItem(i: Item)

    @Update
    suspend fun updateItem(i: Item)

    @Delete
    suspend fun deleteItem(i: Item)

    @Query("SELECT * FROM item ORDER BY name ASC")
    fun getItemsAll() : Flow<List<Item>>

    @Query("SELECT * FROM item WHERE id = :id")
    fun getItemById(id: Int) : Flow<Item>

    @Query("SELECT * FROM item WHERE quantity > 0")
    fun getItemsInStock() : Flow<List<Item>>
}