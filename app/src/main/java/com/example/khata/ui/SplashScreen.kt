package com.example.khata.ui

import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity
import com.example.khata.R
import com.example.khata.api.SharedPrefManager
import com.example.khata.pojo.exClearNewActivity


class SplashScreen : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        Handler().postDelayed({
            SharedPrefManager.init(this)
            if(!SharedPrefManager.userToken.isNullOrEmpty()){
                exClearNewActivity(this,HomeActivity::class.java)
            }else{
                exClearNewActivity(this,MainActivity::class.java)
            }
        }, 1500)
    }
}
