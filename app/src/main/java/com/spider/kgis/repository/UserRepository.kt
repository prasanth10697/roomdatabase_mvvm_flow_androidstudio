package com.spider.kgis.repository

import com.spider.kgis.data.UserDao
import com.spider.kgis.model.User
import kotlinx.coroutines.flow.Flow


class UserRepository(private val userDao: UserDao) {

    suspend fun addUser(user: User) {
        userDao.addUser(user)
    }

    fun selectDateBase(searchQuery: String): Flow<List<User>> {
        return userDao.selectDatabase(searchQuery)
    }

    fun selectMonthBase(searchQuery: String): Flow<List<User>> {
        return userDao.selectMonthBase(searchQuery)
    }

}