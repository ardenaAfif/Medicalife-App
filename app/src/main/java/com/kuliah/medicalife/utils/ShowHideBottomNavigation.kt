package com.kuliah.medicalife.utils

import android.view.View
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.kuliah.medicalife.R
import com.kuliah.medicalife.ui.home.HomeActivity

fun Fragment.hideBottomNavigationView() {
    val bottomNavigationView = (activity as HomeActivity).findViewById<BottomNavigationView>(R.id.bottomNavView)
    bottomNavigationView.visibility = View.GONE
}

fun Fragment.showBottomNavigationView() {
    val bottomNavigationView = (activity as HomeActivity).findViewById<BottomNavigationView>(R.id.bottomNavView)
    bottomNavigationView.visibility = View.VISIBLE
}