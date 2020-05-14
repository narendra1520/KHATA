package com.example.khata.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.attendence.POJO.DefaultResponse
import com.example.khata.R
import com.example.khata.api.RetrofitClient
import com.example.khata.pojo.CustomerHolder
import com.example.khata.pojo.toast
import com.example.khata.pojo.validNote
import com.example.khata.pojo.validOneMore
import kotlinx.android.synthetic.main.activity_bill.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class BillActivity : AppCompatActivity() {

    private var note:String = ""
    private var amount:String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_bill)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val opr = intent.getStringExtra("operation")

        btn_add_bill.setOnClickListener {
            if(validateForm()) {
                val transactCall = RetrofitClient.mInstance.doTransaction(
                    opr+amount,CustomerHolder.customer.token,note)
                transactCall.enqueue(object : Callback<DefaultResponse> {

                    override fun onResponse(call: Call<DefaultResponse>, response: Response<DefaultResponse>) {
                        if(response.body()?.isError!!){
                            toast(this@BillActivity, response.body()?.message)
                        }else{
                            if(opr=="-")
                                amount = "-$amount"

                            CustomerHolder.customer.rs = String.format("%.2f",CustomerHolder.customer.rs
                                    + amount.toFloat()).toFloat()

                            val i = Intent()
                            setResult(Activity.RESULT_OK, i)
                            finish()
                        }
                    }

                    override fun onFailure(call: Call<DefaultResponse>, t: Throwable) {
                        toast(this@BillActivity,t.message)
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

    private fun validateForm(): Boolean {

        ly_amount.error = null
        ly_note.error = null

        amount = edt_amount.text.toString()
        note = edt_note.text.toString()

        val erNote = validNote(note)
        val erAm = validOneMore(amount)

        return if(erNote == true && erAm == true) {
            true
        }
        else {

            if (erNote != true)
                ly_note.error = erNote.toString()
            if (erAm != true)
                ly_amount.error = erAm.toString()

            false
        }
    }
}
