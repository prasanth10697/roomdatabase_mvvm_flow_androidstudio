package com.spider.kgis.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.spider.kgis.data.UserDatabase
import com.spider.kgis.repository.UserRepository
import com.spider.kgis.model.User

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class UserViewModel(application: Application): AndroidViewModel(application) {
     private val repository: UserRepository

    init {
        val userDao = UserDatabase.getDatabase(application).userDao()
        repository= UserRepository(userDao)
    }

    fun addUser(user: User) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addUser(user)
        }
    }

    fun selectDateBase(searchQuery: String): LiveData<List<User>> {
        return repository.selectDateBase(searchQuery).asLiveData()
    }

    fun selectMonthBase(searchQuery: String): LiveData<List<User>> {
        return repository.selectMonthBase(searchQuery).asLiveData()
    }
}