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

        initRecyclerView()
    }

    private fun initRecyclerView() {
        binding.rvSubscriber.layoutManager = LinearLayoutManager(this)
        displayData()
    }

    private fun displayData() {
        viewModel.subscriber.observe(this, {
            binding.rvSubscriber.adapter = RVAdapter(it) { selectedItem: Subscriber ->
                listItemClicked(
                    selectedItem
                )
            }
        })
    }

    private fun listItemClicked(subscriber: Subscriber) {
        Toast.makeText(this, "you clicked name ${subscriber.name}", Toast.LENGTH_SHORT).show()
        viewModel.initUpdateAndDelete(subscriber)
    }
}