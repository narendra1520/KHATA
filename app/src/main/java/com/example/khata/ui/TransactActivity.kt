package com.example.khata.ui

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khata.R
import com.example.khata.api.RetrofitClient
import com.example.khata.api.TransactionResponse
import com.example.khata.pojo.CustomerHolder
import com.example.khata.pojo.toast
import kotlinx.android.synthetic.main.activity_transact.*
import kotlinx.android.synthetic.main.app_bar.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class TransactActivity : AppCompatActivity(), View.OnClickListener {

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.add_bill -> {val i =Intent(this,BillActivity::class.java)
                i.putExtra("operation","+")
                startActivityForResult(i,BILLING_CODE)
            }

            R.id.remove_bill -> {val i =Intent(this,BillActivity::class.java)
                i.putExtra("operation","-")
                startActivityForResult(i,BILLING_CODE)
            }

            R.id.req_money -> {
                bottomSheetFragment.show(supportFragmentManager, bottomSheetFragment.tag)
            }
        }
    }

    private val PROFIFE_CODE: Int = 100
    private val BILLING_CODE: Int = 300

    private lateinit var bottomSheetFragment: PopBillingFrag

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_transact)
        updateActionbar()
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowTitleEnabled(false)

        transact_recy.layoutManager = LinearLayoutManager(this)
        transact_recy.addItemDecoration(DividerItemDecoration(this,DividerItemDecoration.VERTICAL))

        updateTransactions()

        bottomSheetFragment = PopBillingFrag()

        add_bill.setOnClickListener(this)
        remove_bill.setOnClickListener(this)
        req_money.setOnClickListener(this)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.menu_item_phone->{
                val intent = Intent(Intent.ACTION_DIAL)
                intent.data = Uri.parse("tel:+91${CustomerHolder.customer.mobile}")
                startActivity(intent)
            }
            R.id.menu_item_more -> startActivityForResult(
                Intent(this, CustomerProfileActivity::class.java),PROFIFE_CODE
            )
            android.R.id.home -> onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == PROFIFE_CODE && resultCode==Activity.RESULT_OK){
            updateActionbar()
        }
        if(requestCode == BILLING_CODE && resultCode==Activity.RESULT_OK){
            updateTransactions()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    private fun updateTransactions() {
        val transactCall = RetrofitClient.mInstance.getTransaction(CustomerHolder.customer.token)
        transactCall.enqueue(object : Callback<TransactionResponse> {
            override fun onResponse(call: Call<TransactionResponse>, response: Response<TransactionResponse>) {
                if(response.body()?.isError!!){
                    toast(this@TransactActivity,response.body()?.message)
                }else{
                    val transactList = response.body()?.data
                    val adapter = TransactAdapter(transactList!!,this@TransactActivity)
                    transact_recy.adapter = adapter
                }
            }

            override fun onFailure(call: Call<TransactionResponse>, t: Throwable) {
                Log.d("Billing",t.message)
            }
        })

        txt_rs.text = CustomerHolder.customer.rs.toString()
    }

    private fun updateActionbar() {
        tool_name.text = CustomerHolder.customer.name
        tool_contact.text = CustomerHolder.customer.mobile
    }
}
