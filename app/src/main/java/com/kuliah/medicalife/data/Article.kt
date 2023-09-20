package com.kuliah.medicalife.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Article (
    val judul: String,
    val desc: String,
    val image: Int
) : Parcelable