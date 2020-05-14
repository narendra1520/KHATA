package com.example.khata.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.khata.R
import com.example.khata.api.LoginResponse
import com.example.khata.api.RetrofitClient
import com.example.khata.api.SharedPrefManager
import com.example.khata.pojo.exClearNewActivity
import com.example.khata.pojo.toast
import com.example.khata.pojo.validPhone
import kotlinx.android.synthetic.main.activity_main.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {

    var flg:Boolean = false
    var mobile:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        get_otp.setOnClickListener{
            if(flg){
                //resend the otp
            }else{
                if(validateMobile()) {
                    get_otp.text = "Resend"
                    ly_mob.isVisible = false
                    dis_lay.isVisible = true
                    txt_mobile.text = mobile
                    flg = true

                    //send first new otp
                }
            }
        }

        btn_change.setOnClickListener {
            //toggling the view
            get_otp.text = "Send"
            ly_mob.isVisible = true
            dis_lay.isVisible = false
            flg = false
        }

        btn_verify.setOnClickListener {
            val loginCall = RetrofitClient.mInstance.login(mobile)
            loginCall.enqueue(object : Callback<LoginResponse>{
                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    toast(this@MainActivity,t.message)
                }

                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    if(response.body()?.isError!!){
                        toast(this@MainActivity,response.body()?.message)
                    }else{
                        val user = response.body()?.data
                        SharedPrefManager.init(this@MainActivity)
                        SharedPrefManager.user = user
                        toast(this@MainActivity,response.body()?.message)
                        exClearNewActivity(this@MainActivity,HomeActivity::class.java)
                        finish()
                    }
                }
            })
        }
    }

    fun validateMobile(): Boolean{
        mobile = edt_mobile.text.toString()
        return if(validPhone(mobile) == true){
            ly_mob.error = null
            true
        }else{
            ly_mob.error = validPhone(mobile).toString()
            false
        }
    }
}
