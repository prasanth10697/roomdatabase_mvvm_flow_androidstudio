package com.spider.kgis

import android.app.DatePickerDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.spider.kgis.viewmodel.UserViewModel
import com.spider.kgis.adapter.TaskFilterAdapter
import com.spider.kgis.databinding.ActivityMonthListBinding
import com.spider.kgis.util.MonthYearPickerDialog
import java.util.Calendar
import java.util.Date

@Suppress("NAME_SHADOWING")
class MonthListActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMonthListBinding
    private val cal = Calendar.getInstance()
    private lateinit var mUserViewModel: UserViewModel
    private val taskFilterAdapter: TaskFilterAdapter by lazy { TaskFilterAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMonthListBinding.inflate(layoutInflater)
        setContentView(binding.root)
        mUserViewModel = ViewModelProvider(this)[UserViewModel::class.java]

        binding.calenderId.setOnClickListener {
            startActivity(Intent(this@MonthListActivity, MainActivity::class.java))
        }
        binding.listRv.layoutManager = LinearLayoutManager(this)
        binding.listRv.adapter = taskFilterAdapter

        binding.monthPicker.setOnClickListener {
            MonthYearPickerDialog(Date()).apply {
                setListener { _, _, month, _ ->
                    mUserViewModel.selectMonthBase((month+1).toString()).observe(this) { list ->
                        list.let {
                            if (it.isNotEmpty()) {
                                taskFilterAdapter.setData(it)
                                binding.noData.isVisible = false
                                binding.listRv.isVisible = true
                            }else{
                                binding.noData.isVisible = true
                                binding.listRv.isVisible = false
                            }
                        }
                    }
                }
                show(supportFragmentManager, "MonthYearPickerDialog")
            }
        }

        binding.datePicker.setOnClickListener {
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this, { _, year, monthOfYear, dayOfMonth ->
                    val dat = (dayOfMonth.toString() + "/" + (monthOfYear + 1) + "/" + year)
                    mUserViewModel.selectDateBase(dat).observe(this) { list ->
                        list.let {
                            if (it.isNotEmpty()) {
                                taskFilterAdapter.setData(it)
                                binding.noData.isVisible = false
                                binding.listRv.isVisible = true
                            }else{
                                binding.noData.isVisible = true
                                binding.listRv.isVisible = false
                            }
                        }
                    }
                },
                year, month, day
            )
            datePickerDialog.show()
        }

    }
}