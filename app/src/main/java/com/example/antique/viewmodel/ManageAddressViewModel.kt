package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.*
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.AddressRepository
import com.example.antique.model.remote.entity.Address
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ManageAddressViewModel(application: Application) : AndroidViewModel(application) {

    private val addressRepository = AddressRepository
    private var userId = ""

    fun setUser(userId: String) {
        this.userId = userId
    }

    var contactName by mutableStateOf("")
    var contactNumber by mutableStateOf("")
    var street by mutableStateOf("")
    var ward by mutableStateOf("")
    var district by mutableStateOf("")
    var city by mutableStateOf("")
    var poBox by mutableStateOf("")

    var nameError by mutableStateOf(false)
    var numberError by mutableStateOf(false)
    var streetError by mutableStateOf(false)
    var wardError by mutableStateOf(false)
    var districtError by mutableStateOf(false)
    var cityError by mutableStateOf(false)
    var poBoxError by mutableStateOf(false)

    fun resetFields() {
        contactName = ""
        contactNumber = ""
        street = ""
        ward = ""
        district = ""
        city = ""
        poBox = ""

        nameError = false
        numberError = false
        streetError = false
        wardError = false
        districtError = false
        cityError = false
        poBoxError = false
    }

    fun validateAddress(): Boolean {
        nameError = contactName.isBlank()
        numberError = contactNumber.isBlank()
        streetError = street.isBlank()
        wardError = ward.isBlank()
        districtError = district.isBlank()
        cityError = city.isBlank()
        poBoxError = poBox.isBlank()

        return !(nameError || numberError || streetError || wardError || districtError || cityError || poBoxError)
    }

    fun setCurrentAddress(addressId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val address = addressRepository.getAddressById(addressId)
            address?.let {
                contactName = it.name
                contactNumber = it.phone
                street = it.street
                ward = it.ward
                district = it.district
                city = it.city
                poBox = it.poBox.toString()
            }
        }
    }

    fun addAddress() {
        val address = Address(
            uid = userId,
            name = contactName,
            phone = contactNumber,
            street = street,
            ward = ward,
            district = district,
            city = city,
            poBox = poBox.toIntOrNull() ?: 0
        )

        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.insertAddress(address)
        }
    }

    fun updateAddress(addressId: String) {
        val address = Address(
            id = addressId,
            uid = userId,
            name = contactName,
            phone = contactNumber,
            street = street,
            ward = ward,
            district = district,
            city = city,
            poBox = poBox.toIntOrNull() ?: 0
        )

        viewModelScope.launch(Dispatchers.IO) {
            addressRepository.updateAddress(address)
        }
    }
}
