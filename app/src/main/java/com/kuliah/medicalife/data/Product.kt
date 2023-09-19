package com.kuliah.medicalife.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product (
    val id: String,
    val name: String,
    val price: Int,
    val description: String? = null,
    val rules: String? = null,
    val images: List<String>
) : Parcelable {
    constructor(): this("0", "", 0, images = emptyList())
}