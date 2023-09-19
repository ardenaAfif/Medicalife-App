package com.kuliah.medicalife.data

data class Cart(
    val product: Product,
    val quantity: Int
) {
    constructor(): this(Product(), 1)
}
