package com.spider.kgis.data

import androidx.room.*
import com.spider.kgis.model.User
import kotlinx.coroutines.flow.Flow
@Dao
interface UserDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun addUser(user: User)

    @Query("SELECT * FROM user_table WHERE dateAndTime LIKE :searchQuery")
    fun selectDatabase(searchQuery: String): Flow<List<User>>

    @Query("SELECT * FROM user_table WHERE taskMonth LIKE :searchQuery")
    fun selectMonthBase(searchQuery: String): Flow<List<User>>

}