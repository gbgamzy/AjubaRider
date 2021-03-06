package com.example.ajubarider

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DefaultItemAnimator
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView


class ViewPagerAdapter(

        val toBeDelivered:ArrayList<Order>, val toBeAccepted:ArrayList<Order>,val accepted:ArrayList<Order>,
        private val context: Context
) :
        RecyclerView.Adapter<ViewPagerAdapter.ViewHolder>(){


    inner class ViewHolder(view: View): RecyclerView.ViewHolder(view) {
        var rv: RecyclerView =view.findViewById(R.id.rvViewPager)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.layout_view_pager, parent, false)
        return ViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        if(position==0) {
            val adapter: Adapter = Adapter(toBeDelivered,context)
            holder.rv.layoutManager= LinearLayoutManager(context)
            holder.rv.itemAnimator= DefaultItemAnimator()
            holder.rv.adapter=adapter
            adapter.notifyDataSetChanged()
        }

        if(position==1) {
            val adapter: ToBeAcceptedAdapter = ToBeAcceptedAdapter(toBeAccepted,context)
            holder.rv.layoutManager= LinearLayoutManager(context)
            holder.rv.itemAnimator= DefaultItemAnimator()
            holder.rv.adapter=adapter
            adapter.notifyDataSetChanged()
        }
        if(position==2) {
            val adapter: AcceptedAdapter = AcceptedAdapter(accepted,context)
            holder.rv.layoutManager= LinearLayoutManager(context)
            holder.rv.itemAnimator= DefaultItemAnimator()
            holder.rv.adapter=adapter
            adapter.notifyDataSetChanged()
        }





    }



    override fun getItemCount(): Int {
        return 3
    }
}