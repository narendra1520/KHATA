package com.example.khata.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.attendence.POJO.DefaultResponse
import com.example.khata.R
import com.example.khata.api.RetrofitClient
import com.example.khata.api.SharedPrefManager
import com.example.khata.pojo.*
import kotlinx.android.synthetic.main.activity_customer_profile.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class CustomerProfileActivity : AppCompatActivity(), View.OnClickListener {

    private var mobile: String = ""
    private var name: String = ""
    var flag: Boolean = false

    lateinit var customer: Customer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_customer_profile)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        customer = CustomerHolder.customer
        edt_name.setText(customer.name)
        edt_mobile.setText(customer.mobile)
        remain_rs.text = customer.rs.toString()

        btn_edit_customer.setOnClickListener(this)
        btn_delete_customer.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btn_edit_customer -> {
                if (flag && validateForm()) {
                    RetrofitClient.mInstance.updateCustomer(
                        SharedPrefManager.userToken!!, name, mobile,
                        customer.token
                    ).enqueue(object : Callback<DefaultResponse> {
                        override fun onResponse(
                            call: Call<DefaultResponse>,
                            response: Response<DefaultResponse>
                        ) {
                            if (response.body()!!.isError)
                                toast(this@CustomerProfileActivity, response.body()!!.message)
                            else {
                                toast(this@CustomerProfileActivity, response.body()!!.message)
                                customer.name = name
                                customer.mobile = mobile

                                val data = Intent()
                                setResult(Activity.RESULT_OK, data)
                                finish()
                            }
                        }

                        override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                            Log.d("Failure", t.message)
                        }
                    })
                } else {
                    toggleView()
                }
            }

            R.id.btn_delete_customer -> {
                RetrofitClient.mInstance.deleteCustomer(
                    SharedPrefManager.userToken!!, customer.token
                ).enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(
                        call: Call<DefaultResponse>,
                        response: Response<DefaultResponse>
                    ) {
                        if (response.body()!!.isError)
                            toast(this@CustomerProfileActivity, response.body()!!.message)
                        else {
                            toast(this@CustomerProfileActivity, response.body()!!.message)
                            customer.name = name
                            customer.mobile = mobile

                            exClearNewActivity(
                                this@CustomerProfileActivity,
                                HomeActivity::class.java
                            )
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        Log.d("Failure", t.message)
                    }
                })
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun toggleView() {
        if (!flag) {
            edt_name.isEnabled = true
            edt_mobile.isEnabled = true
            btn_edit_customer.text = "Save Profile"
            flag = true
        } else {
            edt_name.isEnabled = false
            edt_mobile.isEnabled = false
            btn_edit_customer.text = "Edit Profile"
            flag = false
        }
    }

    private fun validateForm(): Boolean {
        ly_name.error = null
        ly_mobile.error = null

        name = edt_name.text.toString()
        mobile = edt_mobile.text.toString()

        val erName = validName(name)
        val erPhone = validPhone(mobile)

        return if (erName == true && erPhone == true) {
            true
        } else {

            if (erName != true)
                ly_name.error = erName.toString()
            if (erPhone != true)
                ly_mobile.error = erPhone.toString()

            false
        }
    }
}
