package com.dzakyhdr.roomdemo.db

class SubscriberRepository(private val dao: SubscriberDAO) {

    val subscriber = dao.getAllSubscriber()

    suspend fun insert(subscriber: Subscriber): Long{
        return dao.insertSubscriber(subscriber)
    }

    suspend fun update(subscriber: Subscriber): Int{
        return dao.updatedSubscriber(subscriber)
    }

    suspend fun delete(subscriber: Subscriber): Int{
        return dao.deleteSubscriber(subscriber)
    }

    suspend fun deleteAll(): Int{
        return dao.deleteAll()
    }
}