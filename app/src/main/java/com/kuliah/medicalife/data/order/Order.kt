package com.kuliah.medicalife.data.order

import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.data.Cart
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

data class Order(
    val orderStatus: String = "",
    val totalPrice: Int = 0,
    val products: List<Cart> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val orderId: Long = nextLong(0, 100_000_000) + totalPrice.toLong()
) {

}
