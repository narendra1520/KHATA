package com.example.khata.ui

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.khata.R
import com.example.khata.api.CustomerResponse
import com.example.khata.api.RetrofitClient
import com.example.khata.api.SharedPrefManager
import com.example.khata.pojo.Customer
import com.example.khata.pojo.exClearNewActivity
import kotlinx.android.synthetic.main.activity_home.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeActivity : AppCompatActivity() {
    private val ADD_CODE: Int = 400
    private lateinit var adapter: HomeAdapter
    private var customer = listOf<Customer>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        custom_recy.layoutManager = LinearLayoutManager(this)
        getCustomerData()

        add_customer.setOnClickListener {
            val i = Intent(this, AddCustomerActivity::class.java)
            startActivityForResult(i,ADD_CODE)
        }
    }

    private fun getCustomerData(){
        val token = SharedPrefManager.userToken
        val customerCall = RetrofitClient.mInstance.getCustomer(token!!)
        customerCall.enqueue(object : Callback<CustomerResponse> {
            override fun onResponse(call: Call<CustomerResponse>, response: Response<CustomerResponse>) {
                if(response.body()?.isError!!){

                }else{
                    val customer = response.body()?.data
                    adapter = HomeAdapter(customer!!,this@HomeActivity)
                    custom_recy.adapter = adapter
                }
            }

            override fun onFailure(call: Call<CustomerResponse>, t: Throwable) {

            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.home, menu)

        val searchview = menu?.findItem(R.id.action_search)?.actionView as SearchView
        searchview.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                adapter.filter.filter(query)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.filter.filter(newText)
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if(requestCode == ADD_CODE && resultCode == Activity.RESULT_OK){
            getCustomerData()
        }
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.action_account)
            startActivity(Intent(this,AccountActivity::class.java))
        if(item.itemId == R.id.action_logout){
            SharedPrefManager.clear()
            exClearNewActivity(this,MainActivity::class.java)
            finish()
        }

        return super.onOptionsItemSelected(item)
    }
}
