package com.example.khata.ui

import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.example.attendence.POJO.DefaultResponse
import com.example.khata.R
import com.example.khata.api.RetrofitClient
import com.example.khata.api.SharedPrefManager
import com.example.khata.pojo.toast
import com.example.khata.pojo.validUpi
import kotlinx.android.synthetic.main.activity_account.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountActivity : AppCompatActivity() {

    private var upi: String = ""
    val user = SharedPrefManager.user
    var flag: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_account)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setValue()

        btn_upi.setOnClickListener {
            if (!flag) {
                ly_upi.isVisible = true
                edt_upi.setText(user?.upi)
                flag = true
            }else{
                if (validateForm()) {
                    RetrofitClient.mInstance.updateUser(user?.token.toString(), upi)
                        .enqueue(object : Callback<DefaultResponse> {
                            override fun onResponse(
                                call: Call<DefaultResponse>,
                                response: Response<DefaultResponse>
                            ) {
                                if (response.body()!!.isError)
                                    toast(this@AccountActivity, response.body()!!.message)
                                else {
                                    toast(this@AccountActivity, response.body()!!.message)
                                    user?.upi = upi
                                    SharedPrefManager.user = user
                                    setValue()
                                }
                            }

                            override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                                Log.d("Failure", t.message)
                            }
                        })
                }
            }
        }
    }

    fun setValue(){
        txt_mobile.text = user?.mobile
        if (!user?.upi.isNullOrEmpty()) {
            txt_upi.text = user?.upi
            btn_upi.text = "Edit upi account"
        }
        ly_upi.isVisible = false
    }

    private fun validateForm(): Boolean {
        ly_upi.error = null
        upi = edt_upi.text.toString()
        val erUpi = validUpi(upi)
        return if (erUpi == true)
            true
        else {
            ly_upi.error = erUpi.toString()
            false
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}
