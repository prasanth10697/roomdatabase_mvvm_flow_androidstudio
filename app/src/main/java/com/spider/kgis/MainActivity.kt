@file:Suppress("NAME_SHADOWING")

package com.spider.kgis

import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.spider.kgis.viewmodel.UserViewModel
import com.spider.kgis.adapter.TaskFilterAdapter
import com.spider.kgis.databinding.ActivityMainBinding
import com.spider.kgis.databinding.DialogNegativeLayoutBinding
import com.spider.kgis.model.User
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var dialogNegativeLayoutBinding: DialogNegativeLayoutBinding
    private lateinit var mUserViewModel: UserViewModel
    private var inmateDialogBuilder: AlertDialog.Builder? = null
    private var inmateAlertDialog: AlertDialog? = null
    private var calendar: Calendar? = null
    private val cal = Calendar.getInstance()
    private val taskFilterAdapter: TaskFilterAdapter by lazy { TaskFilterAdapter() }

    @SuppressLint("SimpleDateFormat", "NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]
        supportActionBar?.setDisplayShowTitleEnabled(false)
        calendar = Calendar.getInstance()
        calendar!!.set(Calendar.MONTH, Calendar.NOVEMBER)
        calendar!!.set(Calendar.DAY_OF_MONTH, 9)
        calendar!!.set(Calendar.YEAR, 2012)
        calendar!!.add(Calendar.DAY_OF_MONTH, 1)
        calendar!!.add(Calendar.YEAR, 1)
        val simpleDateFormat = SimpleDateFormat("dd/MM/yyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        val arr: List<String> = currentDateAndTime.split("/")
        binding.textId.text = arr[0]
        cal[Calendar.MONTH] = arr[1].toInt()-1
        val amp = if(cal.get(Calendar.AM_PM)==0) "AM " else "PM "
        binding.currentTime.text = "${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)} $amp"
        binding.dateTime.text = "${cal.get(Calendar.HOUR)}:${cal.get(Calendar.MINUTE)} $amp"
        val monthName = SimpleDateFormat("MMM").format(cal.time)
        binding.monthId.text = monthName
        val str = "${arr[0]}/${arr[1].toInt()}/${arr[2]}"

        mUserViewModel.selectDateBase(str).observe(this) { list ->
            list.let {
                if (it.isNotEmpty()) {
                    taskFilterAdapter.setData(it)
                    binding.noData.isVisible = false
                    binding.taskCard.isVisible = true
                }else{
                    binding.noData.isVisible = true
                    binding.taskCard.isVisible = false
                }
            }
        }

        binding.listRv.layoutManager = LinearLayoutManager(this)
        binding.listRv.adapter = taskFilterAdapter

        binding.calendarView.setOnDateChangeListener { _, year, month, day ->
            binding.textId.text = day.toString()
            cal[Calendar.MONTH] = month
            val monthName = SimpleDateFormat("MMM").format(cal.time)
            binding.monthId.text = monthName
            mUserViewModel.selectDateBase("$day/${month + 1}/$year").observe(this) { list ->
                list.let {
                    if (it.isNotEmpty()) {
                        taskFilterAdapter.setData(it)
                        binding.noData.isVisible = false
                        binding.taskCard.isVisible = true
                    }else{
                        binding.noData.isVisible = true
                        binding.taskCard.isVisible = false
                    }
                }
            }
        }

        binding.addButton.setOnClickListener {
            showInmateAlertDialog()
        }

        binding.listMonthId.setOnClickListener {
            startActivity(Intent(this@MainActivity, MonthListActivity::class.java))
        }
    }

    private fun showInmateAlertDialog() {
        inmateDialogBuilder = AlertDialog.Builder(this@MainActivity)
        dialogNegativeLayoutBinding = DialogNegativeLayoutBinding.inflate(layoutInflater)
        inmateDialogBuilder!!.setView(dialogNegativeLayoutBinding.root)
        inmateAlertDialog = inmateDialogBuilder!!.create()
        inmateAlertDialog!!.window!!.attributes.windowAnimations = android.R.style.Animation_Dialog
        inmateAlertDialog!!.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        inmateAlertDialog!!.show()
        inmateAlertDialog!!.setCanceledOnTouchOutside(false)
        dialogNegativeLayoutBinding.btnDialog.setOnClickListener { inmateAlertDialog!!.dismiss() }

        dialogNegativeLayoutBinding.dateLayout.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    dialogNegativeLayoutBinding.taskDate.text = dat
                },
                year, month, day
            )
            datePickerDialog.show()
        }

        dialogNegativeLayoutBinding.btnSubmit.setOnClickListener {
            if (dialogNegativeLayoutBinding.taskName.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "please enter task title!", Toast.LENGTH_LONG).show()
            } else if (dialogNegativeLayoutBinding.taskDescription.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "please enter description!", Toast.LENGTH_LONG).show()
            } else if (dialogNegativeLayoutBinding.taskDate.text.toString().trim().isEmpty()) {
                Toast.makeText(this, "please select date!", Toast.LENGTH_LONG).show()
            } else {
                val taskName = dialogNegativeLayoutBinding.taskName.text.toString()
                val taskMessage = dialogNegativeLayoutBinding.taskDescription.text.toString()
                val dateTime = dialogNegativeLayoutBinding.taskDate.text.toString()
                val arr: List<String> = dateTime.split("/")
                val user = User(0, dateTime, taskName, taskMessage, arr[1])
                mUserViewModel.addUser(user)
                inmateAlertDialog!!.dismiss()
                Toast.makeText(this, "Successfully added!", Toast.LENGTH_LONG).show()
            }
        }
    }
}