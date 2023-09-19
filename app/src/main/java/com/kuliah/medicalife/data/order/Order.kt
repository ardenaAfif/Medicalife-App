package com.kuliah.medicalife.data.order

import android.os.Parcelable
import com.kuliah.medicalife.data.Address
import com.kuliah.medicalife.data.Cart
import kotlinx.parcelize.Parcelize
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.random.Random.Default.nextLong

@Parcelize
data class Order(
    val orderStatus: String = "",
    val totalPrice: Int = 0,
    val products: List<Cart> = emptyList(),
    val address: Address = Address(),
    val date: String = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()),
    val orderId: Long = nextLong(0, 100_000_000) + totalPrice.toLong()
) : Parcelable
