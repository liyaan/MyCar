package com.liyaan.car

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import com.liyaan.car.abs.BaseActivity

class SplashActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler().postDelayed({startMainActivity()},1000)
    }
    private fun startMainActivity(){
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}