package com.dzakyhdr.roomdemo

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.dzakyhdr.roomdemo.databinding.ListItemBinding
import com.dzakyhdr.roomdemo.db.Subscriber

class RVAdapter(private val subscriberList: List<Subscriber>): RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding: ListItemBinding = DataBindingUtil.inflate(layoutInflater, R.layout.list_item,parent,false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscriberList[position])
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }
}

class MyViewHolder(private val binding: ListItemBinding) : RecyclerView.ViewHolder(binding.root) {
    fun bind(subscriber: Subscriber) {
        binding.txtName.text = subscriber.name
        binding.txtEmail.text = subscriber.email
    }
}