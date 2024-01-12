package com.liyaan.car


import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication

class App: MultiDexApplication() {

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        MultiDex.install(base)
    }

}