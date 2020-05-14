package com.example.khata.ui

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.khata.R
import com.example.khata.pojo.Customer
import com.example.khata.pojo.CustomerHolder

class HomeAdapter(private var customerList: List<Customer>, private var context: Context) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>(), Filterable{

    var list:List<Customer> = customerList
    override fun getFilter(): Filter {
        return object:Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                val list2 = list.filter { it.name.contains(charString,true)
                        || it.mobile.contains(charString,true) }

                return FilterResults().apply {
                    values = if(list2.isEmpty())
                        list
                    else
                        list2
                }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                customerList = results?.values as List<Customer>
                notifyDataSetChanged()
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.cust_rec_lay, parent, false)
        return HomeViewHolder(view)
    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val n = customerList[position]
        holder.name.text = n.name
        holder.rs.text =  n.rs.toString()
        holder.con.text = n.mobile
        if(n.img!="none")
            Glide.with(context).load(n.img).into(holder.img)
    }

    override fun getItemCount(): Int {
        return customerList.size
    }

    inner class HomeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.findViewById(R.id.txt_name)
        var rs: TextView = itemView.findViewById(R.id.txt_rs)
        var con: TextView = itemView.findViewById(R.id.txt_contact)
        var img: ImageView = itemView.findViewById(R.id.img)
        var card: CardView = itemView.findViewById(R.id.cust_card)

        init {
            card.setOnClickListener {
                CustomerHolder.customer = customerList[adapterPosition]
                context.startActivity(Intent(context,TransactActivity::class.java))
            }
        }
    }
}

