package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.AddressRepository
import com.example.antique.model.remote.entity.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddressViewModel(val context: Application) : AndroidViewModel(context) {
    private val addressRepo = AddressRepository

    private var userId = ""

    fun setUser(userId: String) {
        this.userId = userId
        setUserAddresses(userId)
    }

    var addresses = mutableStateOf<List<Address>>(emptyList())

    private fun setUserAddresses(userId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            addresses.value = addressRepo.getAddressesByUser(userId)
        }
    }

    fun deleteAddress(address: Address) {
        viewModelScope.launch(Dispatchers.IO) {
            addressRepo.deleteAddress(address)
            addresses.value = addresses.value.filter { it.id != address.id }
        }
    }
}