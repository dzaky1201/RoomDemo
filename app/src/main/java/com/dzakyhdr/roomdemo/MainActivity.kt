package com.dzakyhdr.roomdemo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.dzakyhdr.roomdemo.databinding.ActivityMainBinding
import com.dzakyhdr.roomdemo.db.Subscriber
import com.dzakyhdr.roomdemo.db.SubscriberDAO
import com.dzakyhdr.roomdemo.db.SubscriberDatabase
import com.dzakyhdr.roomdemo.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    private lateinit var dao: SubscriberDAO
    private lateinit var repository: SubscriberRepository
    private lateinit var factory: ViewModelFactory
    private lateinit var viewModel: SubscriberViewModel
    private lateinit var adapter: RVAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        dao = SubscriberDatabase.getInstance(application).subscriberDAO
        repository = SubscriberRepository(dao)
        factory = ViewModelFactory(repository)
        viewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        // assign viewmodel to databinding
        binding.viewmodel = viewModel
        // assign livedata to databinding
        binding.lifecycleOwner = this

        viewModel.message.observe(this, {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvSubscriber.layoutManager = LinearLayoutManager(this)
        adapter = RVAdapter { selectedItem: Subscriber ->
            listItemClicked(selectedItem)
        }
        binding.rvSubscriber.adapter = adapter
        displayData()
    }

    private fun displayData() {
        viewModel.subscriber.observe(this, {
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
//        Toast.makeText(this, "you clicked name ${subscriber.name}", Toast.LENGTH_SHORT).show()
        viewModel.initUpdateAndDelete(subscriber)
    }
}