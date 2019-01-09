package com.example.ilya4.koshpushover

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.util.Log
import android.widget.TextView
import java.text.SimpleDateFormat
import java.util.*

class DateTimePickers (private val context: Context,
                       private val delayedTime: Calendar,
                       private val datetime_tv: TextView) {
    private val now = Calendar.getInstance()

    private val mYear = now.get(Calendar.YEAR)
    private val mMonth = now.get(Calendar.MONTH)
    private val mDay = now.get(Calendar.DAY_OF_MONTH)

    private val mHour = now.get(Calendar.HOUR_OF_DAY)
    private val mMinute = now.get(Calendar.MINUTE)


    init {
        Log.v(TAG, "init")
    }

    fun getDateTimeCalendar(){
        datePicker()
    }

    private fun datePicker(){
        Log.v(TAG, "datePicker()")

        DatePickerDialog(context, DatePickerDialog.OnDateSetListener{view, year, month, dayOfMonth ->
            delayedTime.set(year, month, dayOfMonth)
            timePicker()
        }, mYear, mMonth, mDay).show()
    }

    private fun timePicker(){
        Log.v(TAG, "timePicker()")
        TimePickerDialog(context, TimePickerDialog.OnTimeSetListener{
            view, hourOfDay, minute ->
            delayedTime.set(Calendar.HOUR_OF_DAY, hourOfDay)
            delayedTime.set(Calendar.MINUTE, minute)
            datetime_tv.text = SimpleDateFormat("dd.MM.yyyy HH:mm", Locale.getDefault()).format(delayedTime.time)
        }, mHour, mMinute, true).show()
    }

    companion object {
        private val TAG = ".DateTimePickers"
    }
}