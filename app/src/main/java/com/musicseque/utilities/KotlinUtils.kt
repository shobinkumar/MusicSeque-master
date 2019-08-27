package com.musicseque.utilities

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.content.Context
import android.net.ConnectivityManager
import android.os.Build
import android.support.annotation.RequiresApi
import android.widget.Toast
import com.musicseque.event_manager.activity.CreateEventActivity
import com.musicseque.interfaces.DateTimeInterface
import java.text.SimpleDateFormat
import java.time.format.DateTimeFormatter
import java.util.*

class KotlinUtils {


    companion object {

        public fun checkEmpty(value: String): Boolean {
            var isEmpty = false
            val mEmpty = if (value.equals("")) {
                isEmpty = true
            } else {
                isEmpty = false
            }
            return isEmpty
        }

        public fun setDate(con: Context, intrface: DateTimeInterface) {
            // Get Current Date
            val calc = Calendar.getInstance()
            val mYear = calc.get(Calendar.YEAR)
            val mDay = calc.get(Calendar.DAY_OF_MONTH)
            val daysInMonth = calc.getActualMaximum(Calendar.DAY_OF_MONTH)
            val mMonth = calc.get(Calendar.MONTH)
            val datePickerDialog = DatePickerDialog(con,
                    DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                        var mMonth = "0"
                        var iMonth = monthOfYear + 1
                        if (monthOfYear < 10)
                            mMonth = "0" + iMonth
                        else
                            mMonth = iMonth.toString()
                        intrface.returnDateTime(dayOfMonth.toString() + "/" + mMonth + "/" + year.toString())
                    }, mYear, mMonth, mDay)
            datePickerDialog.show()
        }


        public fun setTime(con: Context, intrface: DateTimeInterface) {
            val c = Calendar.getInstance()
            val hour = c.get(Calendar.HOUR_OF_DAY)
            val minute = c.get(Calendar.MINUTE)

            val tpd = TimePickerDialog(con, TimePickerDialog.OnTimeSetListener(function = { view, h, m ->
                val dateFormat = SimpleDateFormat("HH:mm")
                val mTime = dateFormat.format(dateFormat.parseObject(h.toString() + ":" + m))



                intrface.returnDateTime(mTime)

            }), hour, minute, false)


            tpd.show()
        }


        public fun compareDates(mFromDate: String, mToDate: String): Boolean {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateFrom = dateFormat.parse(mFromDate)
            val dateTo = dateFormat.parse(mToDate)
            if (dateFrom.after(dateTo))
                return false
            else
                return true

        }

        public fun isDateEqual(mFromDate: String, mToDate: String): Boolean {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val dateFrom = dateFormat.parse(mFromDate)
            val dateTo = dateFormat.parse(mToDate)
            if (dateFrom.equals(dateTo))
                return true
            else
                return false

        }

        public fun compareTimes(mFromTime: String, mToTime: String): Boolean {
            val dateFormat = SimpleDateFormat("HH:mm")
            val timeFrom = dateFormat.parse(mFromTime)
            val timeTo = dateFormat.parse(mToTime)
            if (timeFrom.after(timeTo))
                return false
            else
                return true

        }

        public fun monthToReadFormat(date: String): String {
            val oldFormat = SimpleDateFormat("dd/MM/yyyy")
            val newFormat = SimpleDateFormat("dd/MMM/yyyy")

            return newFormat.format(oldFormat.parse(date))


        }

        public fun compareCurrentDate(date: String): Boolean {
            val dateFormat = SimpleDateFormat("dd/MM/yyyy")
            val mSelectedDate = dateFormat.parse(date)

            val cDate = Calendar.getInstance()
            if (mSelectedDate.before(cDate.time)) {
                return false
            } else {
                return true
            }


        }

        fun isNetConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return cm.activeNetworkInfo != null
        }

        fun dateFormatToSend(mDate1: String, mDate2: String): Pair<String, String> {
            val oldDateFormat = SimpleDateFormat("dd/MMM/yyyy");
            val newDateFormat = SimpleDateFormat("MM-dd-yyyy");
            return Pair(newDateFormat.format(oldDateFormat.parse(mDate1)), newDateFormat.format(oldDateFormat.parse(mDate1)))
        }
    }


}