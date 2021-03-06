package com.example.ajubarider

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView


class AcceptedAdapter(
        var list:ArrayList<Order>, val context: Context) :
        RecyclerView.Adapter<AcceptedAdapter.ViewHolder>(){
    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var layout: LinearLayout =view.findViewById(R.id.llOrder)
        var title: TextView =view.findViewById(R.id.tvContent)
        var price: TextView =view.findViewById(R.id.tvprice)
        var address: TextView =view.findViewById(R.id.tvaddress)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AcceptedAdapter.ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.ticket_order, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.title.text = list[position].contents
        holder.price.text = list[position].price.toString()
        holder.address.text = list[position].streetAddress.toString()

    }

    override fun getItemCount(): Int {
        return list.size
    }
}