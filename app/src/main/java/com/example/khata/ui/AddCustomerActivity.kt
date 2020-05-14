package com.example.khata.ui

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.provider.ContactsContract
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.attendence.POJO.DefaultResponse
import com.example.khata.R
import com.example.khata.api.RetrofitClient
import com.example.khata.api.SharedPrefManager
import com.example.khata.pojo.toast
import com.example.khata.pojo.validName
import com.example.khata.pojo.validPhone
import com.example.khata.pojo.validZeroMore
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.activity_add_customer.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AddCustomerActivity : AppCompatActivity() {
    private var name: String = ""
    private var mobile: String = ""
    private var rsValue: String = ""
    private var requestFlag: Boolean = false
    private var REQUEST_CONTACT_CODE: Int = 1
    private var PICK_CON = 300

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_customer)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        btn_select_contact.setOnClickListener {
            requestContactPermission()
            if(requestFlag) {
                val i = Intent(Intent.ACTION_PICK)
                i.type = ContactsContract.CommonDataKinds.Phone.CONTENT_TYPE
                startActivityForResult(i, PICK_CON)
            }
        }

        btn_add_customer.setOnClickListener {
            if(validateForm()){
                val token = SharedPrefManager.userToken
                val loginCall = RetrofitClient.mInstance.addCustomer(name,rsValue,mobile,token!!)
                loginCall.enqueue(object : Callback<DefaultResponse> {
                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        toast(this@AddCustomerActivity,response.body()?.message)
                        val i = Intent()
                        setResult(Activity.RESULT_OK, i)
                        finish()
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        toast(this@AddCustomerActivity,t.message)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode==PICK_CON && resultCode== Activity.RESULT_OK){
            val dis_name = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME
            val dis_con = ContactsContract.CommonDataKinds.Phone.NUMBER
            val uri = data?.data?:return
            val project = arrayOf(dis_name,dis_con)
            val cursor =  contentResolver.query(uri,project,null,null,null)
            if(cursor!=null && cursor.moveToFirst()){
                txt_name.setText(cursor.getString(cursor.getColumnIndex(dis_name)))
                edt_mobile.setText(cursor.getString(cursor.getColumnIndex(dis_con)))
            }
        }
    }

    private fun requestContactPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CONTACTS)
                != PackageManager.PERMISSION_GRANTED
            ) {
                requestPermissions(
                    arrayOf(android.Manifest.permission.READ_CONTACTS),
                    REQUEST_CONTACT_CODE
                )
            }else{
                requestFlag = true
            }
        }
    }

    fun validateForm(): Boolean{
        ly_name.error = null
        ly_mobile.error = null
        ly_value.error = null

        name = txt_name.text.toString()
        mobile = edt_mobile.text.toString()
        rsValue = edt_value.text.toString()

        if(rsValue.isEmpty())
            rsValue = "0"

        val erName = validName(name)
        val erPhone = validPhone(mobile)
        val erVal = validZeroMore(rsValue)

        return if(erName == true && erPhone == true && erVal == true) {
            true
        }
        else {

            if (erName != true)
                ly_name.error = erName.toString()
            if (erPhone != true)
                ly_mobile.error = erPhone.toString()
            if (erVal != true)
                ly_value.error = erVal.toString()

            false
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            REQUEST_CONTACT_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Snackbar.make(btn_select_contact,"Permission required to access contact ",Snackbar.LENGTH_SHORT).also {
                        it.setAction("Re-Request") { requestContactPermission() }
                    }.run {
                        this.show()
                    }
                } else {
                    requestFlag = true
                }
            }
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }
}
