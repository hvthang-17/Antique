package com.example.antique.viewmodel

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.antique.model.repository.CartRepository
import com.example.antique.model.remote.entity.CartWithProduct
import com.example.antique.model.remote.entity.Cart
import com.example.antique.model.remote.entity.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

class CartViewModel(val context: Application) : AndroidViewModel(context) {
    private val cartRepository = CartRepository

    var currentUser: User? = null
    fun setUser(user: User?) {
        this.currentUser = user
        getUserCart()
    }

    val userCart = mutableStateOf<List<CartWithProduct>>(emptyList())
    val totalPrice = mutableStateOf(0.0)
    var discount = 0.0
    val grandTotal = mutableStateOf(0.0)

    private fun getUserCart() {
        discount = when (currentUser?.type) {
            "Silver" -> 5.0
            "Gold" -> 10.0
            else -> 0.0
        }

        viewModelScope.launch(Dispatchers.IO) {
            userCart.value = cartRepository.getCartWithProductByUser(currentUser?.id ?: "")
            var sum = 0.0
            for (item in userCart.value) sum += (item.cart.quantity * item.product.price)
            totalPrice.value = (sum * 100.0).roundToInt() / 100.0
            grandTotal.value = ((sum - (sum * (discount / 100))) * 100.0).roundToInt() / 100.0
        }
    }

    fun updateQty(cartItem: CartWithProduct, quantity: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.updateQuantity(
                currentUser?.id ?: "",
                cartItem.product.id,
                quantity
            )

            getUserCart()
        }
    }

    fun deleteCartItem(cart: Cart) {
        viewModelScope.launch(Dispatchers.IO) {
            cartRepository.deleteCart(cart)
            userCart.value = userCart.value.filter { it.cart.id != cart.id }

            getUserCart()
        }
    }
}