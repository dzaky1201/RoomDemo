package com.dzakyhdr.roomdemo

import android.util.Patterns
import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dzakyhdr.roomdemo.db.Subscriber
import com.dzakyhdr.roomdemo.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable {
    val subscriber = repository.subscriber
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete: Subscriber
    private val statusMessage = MutableLiveData<Event<String>>()
    val message: LiveData<Event<String>>
        get() = statusMessage

    @Bindable
    val inputName = MutableLiveData<String>()

    @Bindable
    val inputEmail = MutableLiveData<String>()

    @Bindable
    val saveOrUpdateButton = MutableLiveData<String>()

    @Bindable
    val clearAllOrDeleteButton = MutableLiveData<String>()

    init {
        saveOrUpdateButton.value = "Save"
        clearAllOrDeleteButton.value = "Clear All"
    }

    fun saveOrUpdate() {
        if (inputName.value.isNullOrEmpty()) {
            statusMessage.value = Event("Nama Wajib Di isi")
        } else if (inputEmail.value.isNullOrEmpty()) {
            statusMessage.value = Event("Nama Wajib Di isi")
        } else if (!Patterns.EMAIL_ADDRESS.matcher(inputEmail.value!!).matches()) {
            statusMessage.value = Event("Format Email Salah")
        } else {
            if (isUpdateOrDelete) {
                subscriberToUpdateOrDelete.name = inputName.value!!
                subscriberToUpdateOrDelete.email = inputEmail.value!!
                update(subscriberToUpdateOrDelete)
            } else {
                val name = inputName.value!!
                val email = inputEmail.value!!
                insert(Subscriber(0, name, email))

                inputName.value = null
                inputEmail.value = null
            }
        }
    }

    fun clearAllOrDelete() {
        if (isUpdateOrDelete) {
            delete(subscriberToUpdateOrDelete)
        } else {
            clearAll()
        }
    }

    private fun insert(subscriber: Subscriber) {
        viewModelScope.launch {
            val newRowId = repository.insert(subscriber)
            if (newRowId > -1) {
                statusMessage.value = Event("$newRowId Berhasil Ditambahkan")
            } else {
                statusMessage.value = Event("Error Saat Menambahka data")
            }
        }
    }

    fun delete(subscriber: Subscriber) {
        viewModelScope.launch {
            val noOfRow = repository.delete(subscriber)
            if (noOfRow > 0) {
                inputEmail.value = null
                inputName.value = null
                isUpdateOrDelete = false
                statusMessage.value = Event("Data Behasil Di Hapus")

                saveOrUpdateButton.value = "Save"
                clearAllOrDeleteButton.value = "Delete All"
            } else {
                statusMessage.value = Event("Delete Error")
            }

        }
    }

    fun update(subscriber: Subscriber) {
        viewModelScope.launch {
            val noOfRow = repository.update(subscriber)
            if (noOfRow > 0) {
                inputEmail.value = null
                inputName.value = null
                isUpdateOrDelete = false
                statusMessage.value = Event(" $noOfRow Data Berhasil DiUpdate")

                saveOrUpdateButton.value = "Save"
                clearAllOrDeleteButton.value = "Delete All"
            } else {
                statusMessage.value = Event("Error")
            }

        }
    }

    fun clearAll() {
        viewModelScope.launch {
            val noOfRowDeleted = repository.deleteAll()
            if (noOfRowDeleted > 0) {
                statusMessage.value = Event("$noOfRowDeleted Data Berhasil Dihapus semua")
            } else {
                statusMessage.value = Event("Data Gagal Dihapus")
            }
        }
    }

    fun initUpdateAndDelete(subscriber: Subscriber) {
        inputEmail.value = subscriber.email
        inputName.value = subscriber.name
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber

        saveOrUpdateButton.value = "Update"
        clearAllOrDeleteButton.value = "Delete"
    }

    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }
}