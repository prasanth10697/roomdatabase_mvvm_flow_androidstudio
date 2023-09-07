package com.spider.kgis.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "user_table")
data class User(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val dateAndTime: String,
    val taskName: String,
    val taskDetails: String,
    val taskMonth: String
) : Parcelable