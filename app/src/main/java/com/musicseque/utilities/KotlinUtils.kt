package com.musicseque.utilities

import android.app.DatePickerDialog
import android.app.FragmentManager
import android.content.Context
import android.net.ConnectivityManager
import android.view.ViewTreeObserver
import android.widget.ListPopupWindow
import android.widget.TextView
import com.musicseque.R
import com.musicseque.interfaces.DateTimeInterface
import java.text.SimpleDateFormat
import java.util.*

class KotlinUtils {

    lateinit var listPopupWindow: ListPopupWindow
    val cal=Calendar.getInstance()

    companion object {
        var dateFormat=SimpleDateFormat("dd/MM/yyyy")

        public fun checkEmpty(value: String): Boolean {
            var isEmpty = false
            val mEmpty = if (value.equals("")) {
                isEmpty = true
            } else {
                isEmpty = false
            }
            return isEmpty
        }

        fun getCurrentDate(): Date
        {
            val cal=Calendar.getInstance().time

          return  dateFormat.parse( dateFormat.format(cal))


        }
        fun getDate(date:String): Date
        {


            return  dateFormat.parse( date)


        }
        public fun setDate(con: Context, intrface: DateTimeInterface, mMinDate: Date, TYPE: Int) {
            // Get Current Date


            val calc = Calendar.getInstance()
            calc.time=mMinDate
            val mYear = calc.get(Calendar.YEAR)
            val mDay = calc.get(Calendar.DAY_OF_MONTH)
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
            datePickerDialog.datePicker.minDate=mMinDate.time
            if(TYPE==2)
            datePickerDialog.datePicker.maxDate=mMinDate.time


            datePickerDialog.show()
        }


        public fun setTime(con: Context, intrface: DateTimeInterface, supportFragmentManager: FragmentManager, mHour: Int) {
            val mStartTimeChangedListener = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.OnTimeSetListener() { view: com.wdullaer.materialdatetimepicker.time.TimePickerDialog?, hourOfDay: Int, minute: Int, second: Int ->
                val dateFormat = SimpleDateFormat("HH:mm")
                val mTime = dateFormat.format(dateFormat.parseObject(hourOfDay.toString() + ":" + minute))
                intrface.returnDateTime(mTime)

            };

            val timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(mStartTimeChangedListener,
                    mHour,
                    0,
                    true
            );
            timePickerDialog.setThemeDark(false);
            timePickerDialog.setTitle("TimePicker Title");
            timePickerDialog.setTimeInterval(1, 60)
            timePickerDialog.setMinTime(mHour,0,0)
            timePickerDialog.setAccentColor(con.resources.getColor(R.color.color_orange));


            timePickerDialog.show(supportFragmentManager, "Timepickerdialog");







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
            if (timeFrom.after(timeTo) || timeFrom.equals(timeTo))
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
            val mCurrentDate = dateFormat.parse(dateFormat.format(cDate.time))

            if (mSelectedDate.equals(mCurrentDate)) {
                return true
            } else {
                return false
            }


        }

        fun isNetConnected(context: Context): Boolean {
            val cm = context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            return cm.activeNetworkInfo != null
        }


        fun createEvent(mDate1: String, mDate2: String): Pair<String, String> {
            val oldDateFormat = SimpleDateFormat("dd/MMM/yyyy");
            val newDateFormat = SimpleDateFormat("dd-MM-yyyy");
            return Pair(newDateFormat.format(oldDateFormat.parse(mDate1)), newDateFormat.format(oldDateFormat.parse(mDate2)))
        }

        fun dateFormatToSend(mDate1: String, mDate2: String): Pair<String, String> {
            val oldDateFormat = SimpleDateFormat("dd-MM-yyyy");
            val newDateFormat = SimpleDateFormat("MM-dd-yyyy");
            return Pair(newDateFormat.format(oldDateFormat.parse(mDate1)), newDateFormat.format(oldDateFormat.parse(mDate2)))
        }

        fun dateFormatToReceive(mDate1: String, mDate2: String): Pair<String, String> {
            val newDateFormat = SimpleDateFormat("dd/MMM/yyyy");
            val oldDateFormat = SimpleDateFormat("MM-dd-yyyy");
            return Pair(newDateFormat.format(oldDateFormat.parse(mDate1)), newDateFormat.format(oldDateFormat.parse(mDate2)))
        }

        public fun getViewWidth(textView: TextView): Int {
            var mWidth = 0

            textView.getViewTreeObserver().addOnGlobalLayoutListener(ViewTreeObserver.OnGlobalLayoutListener
            {
                mWidth = textView.getMeasuredWidth()
            }
            )

            return mWidth
        }

        fun getCurrentTime(): Pair<Int,Int> {
            var cal=Calendar.getInstance()

            var mHourGet=cal.get(Calendar.HOUR_OF_DAY)
            var mAddHour=mHourGet+1
            cal.add(Calendar.HOUR_OF_DAY,1)




            mHourGet=cal.get(Calendar.HOUR_OF_DAY)
            var mMinute=cal.get(Calendar.MINUTE)


            return Pair(mHourGet,mMinute)
        }
    }



}