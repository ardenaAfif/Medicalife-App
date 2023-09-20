package com.kuliah.medicalife.ui.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.kuliah.medicalife.R
import com.kuliah.medicalife.ui.auth.AuthActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({
            Intent(this, AuthActivity::class.java).also {
                startActivity(it)
                finish()
            }
        }, 3000)
    }
}