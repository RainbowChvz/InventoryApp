package com.example.inventory

import androidx.lifecycle.*
import com.example.inventory.data.Item
import com.example.inventory.data.ItemDao
import kotlinx.coroutines.launch
import java.lang.IllegalArgumentException

class InventoryViewModel(private val itemDao: ItemDao) : ViewModel() {

    val allItems: LiveData<List<Item>> = itemDao.getItemsAll().asLiveData()

    fun retrieveItem(id: Int): LiveData<Item> {
        return itemDao.getItemById(id).asLiveData()
    }

    private fun insertItem(i: Item) {
        viewModelScope.launch {
            itemDao.insertItem(i)
        }
    }

    private fun updateItem(i: Item) {
        viewModelScope.launch {
            itemDao.updateItem(i)
        }
    }

    fun deleteItem(i: Item) {
        viewModelScope.launch {
            itemDao.deleteItem(i)
        }
    }

    fun sellItem(i: Item) {
        if(i.quantityInStock > 0) {
            val newItem = i.copy(quantityInStock = i.quantityInStock - 1)
            updateItem(newItem)
        }
    }

    fun addNewItem(
        itemName: String,
        itemPrice: String,
        itemCount: String
    ) {
        val newItem = getNewItemEntry(itemName, itemPrice, itemCount)
        insertItem(newItem)
    }

    private fun getNewItemEntry(
        itemName: String,
        itemPrice: String,
        itemCount: String
    ): Item {
        return Item(
            itemName = itemName,
            itemPrice = itemPrice.toDouble(),
            quantityInStock = itemCount.toInt()
        )
    }

    fun isEntryValid(itemName: String, itemPrice: String, itemCount: String): Boolean {
        return !(itemName.isBlank() || itemPrice.isBlank() || itemCount.isBlank())
    }

    fun isStockAvailable(i: Item): Boolean {
        return i.quantityInStock > 0
    }
}

class InventoryViewModelFactory(private val itemDao: ItemDao) : ViewModelProvider.Factory {

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(InventoryViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return InventoryViewModel(itemDao) as T
        }

        throw IllegalArgumentException("Unknown ViewModel class")
    }
}