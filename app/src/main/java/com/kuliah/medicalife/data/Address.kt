package com.kuliah.medicalife.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Address(
    val fullName: String,
    val asrama: String,
    val kamar: String,
    val phone: String
) : Parcelable {
    constructor(): this("", "", "", "")
}
