package com.example.internethomework

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MyAdapter(private val viewModel: MyViewModel) :
    RecyclerView.Adapter<MyAdapter.ViewHolder>(){

    inner class ViewHolder(private val view: View) :RecyclerView.ViewHolder(view) {
        fun setContents(pos: Int){
            val textView = view.findViewById<TextView>(R.id.textView)

            with (viewModel.items[pos]){
                textView.text = adress
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyAdapter.ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_layout, parent, false)
        val viewHolder = ViewHolder(view)

        return viewHolder
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.setContents(position)
        val view = holder.itemView



    }

    override fun getItemCount() = viewModel.items.size

}