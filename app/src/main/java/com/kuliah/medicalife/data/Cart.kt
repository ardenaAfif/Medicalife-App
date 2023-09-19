package com.kuliah.medicalife.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Cart(
    val product: Product,
    val quantity: Int
): Parcelable {
    constructor(): this(Product(), 1)
}
