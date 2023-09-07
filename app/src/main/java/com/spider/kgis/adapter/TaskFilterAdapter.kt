package com.spider.kgis.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.spider.kgis.databinding.TaskListAdapterBinding
import com.spider.kgis.model.User

@SuppressLint("NotifyDataSetChanged")
class TaskFilterAdapter : RecyclerView.Adapter<TaskFilterAdapter.MenuViewHolder>() {

    private var sendPhotoList = emptyList<User>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = TaskListAdapterBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.binding.taskName.text = sendPhotoList[position].taskName
        holder.binding.taskDetails.text = sendPhotoList[position].taskDetails
        holder.binding.dateTime.text = sendPhotoList[position].dateAndTime
    }

    override fun getItemCount(): Int {
        return sendPhotoList.size
    }

    inner class MenuViewHolder(val binding: TaskListAdapterBinding) :
        RecyclerView.ViewHolder(binding.root)

    fun setData(newData: List<User>){
        sendPhotoList = newData
        notifyDataSetChanged()
    }
}

