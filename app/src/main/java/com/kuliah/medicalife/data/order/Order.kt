package com.kuliah.medicalife.data.order

import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.data.Cart

data class Order(
    val orderStatus: String,
    val totalPrice: Int,
    val products: List<Cart>,
    val address: Address
)
