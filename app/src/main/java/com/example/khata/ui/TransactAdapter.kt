package com.example.khata.ui

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.khata.R
import com.example.khata.pojo.Transact

class TransactAdapter(private val transList: List<Transact>, private var context: Context) :
    RecyclerView.Adapter<TransactAdapter.TransactViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransactViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.trans_rec_lay, parent, false)
        return TransactViewHolder(view)
    }

    override fun onBindViewHolder(holder: TransactViewHolder, position: Int) {
        val n = transList[position]
        holder.date.text = n.date
        holder.rs.text =  n.amount.toString()
        holder.note.text =  n.note
        if(n.amount<0){
            holder.rs.setTextColor(Color.parseColor("#F44336"))
        }
    }

    override fun getItemCount(): Int {
        return transList.size
    }

    inner class TransactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var note: TextView = itemView.findViewById(R.id.note)
        var rs: TextView = itemView.findViewById(R.id.txt_rs)
        var date: TextView = itemView.findViewById(R.id.date)
    }
}

