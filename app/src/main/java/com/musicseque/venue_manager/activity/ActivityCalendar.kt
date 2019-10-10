//package com.musicseque.venue_manager.activity
//
//import android.app.Activity
//import com.google.gson.Gson
//import android.os.Bundle
//import android.support.annotation.Nullable
//import android.util.DisplayMetrics
//import android.util.Log
//import android.view.View
//import android.widget.*
//import com.musicseque.R
//import com.musicseque.venue_manager.model.CalendarModal
//import com.musicseque.venue_manager.model.DateTimeModel
//import com.musicseque.venue_manager.model.TimeModalClass
//import kotlinx.android.synthetic.main.act_calendar.*
//import kotlinx.android.synthetic.main.toolbar_tick.*
//import java.text.DateFormat
//import java.text.ParseException
//import java.text.SimpleDateFormat
//import java.util.*
//import kotlin.collections.ArrayList
//import kotlin.collections.HashMap
//
//
//class ActivityCalendar : Activity(), View.OnClickListener {
//
//    lateinit var format: DateFormat
//    lateinit var timeFormat: SimpleDateFormat
//
//    lateinit var calendar: Calendar
//    lateinit var c: Calendar
//    var alTime: ArrayList<String> = ArrayList()
//
//    var WIDTH = 0
//    //Toolbar
//
//
//    var alNext: ArrayList<CalendarModal> = ArrayList()
//
//
//    lateinit var temp_date: String
//    lateinit var last_temp_date: String
//
//
//    lateinit var todayDate: String
//    internal var weekDaysCount = 1
//
//
//    lateinit var parking_price: String
//    var hashMap: HashMap<String, ArrayList<TimeModalClass>> = HashMap()
//    lateinit var gson: Gson
//    var sdf = SimpleDateFormat("EEEE MMM d, yyyy")
//
//
//    lateinit var currentDate: Date
//    lateinit var clickedDate: Date
//
//    private val currentTime: Date?
//        get() {
//            calendar = Calendar.getInstance()
//
//            val str = timeFormat.format(calendar.getTime())
//            var currentTime: Date? = null
//            try {
//                currentTime = timeFormat.parse(str)
//            } catch (e: ParseException) {
//                e.printStackTrace()
//            }
//
//            return currentTime
//        }
//
//    override fun onCreate(@Nullable savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.act_calendar)
//
//        ivTick.setOnClickListener(this)
//
//        val dimension = DisplayMetrics()
//        getWindowManager()?.getDefaultDisplay()?.getMetrics(dimension)
//        WIDTH = dimension.widthPixels
//        c = Calendar.getInstance()
//        previous_iv.setOnClickListener(this)
//        next_iv.setOnClickListener(this)
//        dayOne_tv.setOnClickListener(this)
//        dayTwo_tv.setOnClickListener(this)
//        dayThree_tv.setOnClickListener(this)
//        dayFour_tv.setOnClickListener(this)
//        dayFive_tv.setOnClickListener(this)
//        daySix_tv.setOnClickListener(this)
//        daySeven_tv.setOnClickListener(this)
//        gson = Gson()
//
//        format = SimpleDateFormat("dd-MM-yyyy")
//        timeFormat = SimpleDateFormat("HH:mm")
//
//        //Add Times "00:00" "01:00"
//        addTimeMethod()
//
//
//        try {
//            getCurrentDate()
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//
//        textsunday_tv.post {
//            WIDTH = textsunday_tv.width
//
//            getWeekDayPrev()
//        }
//
//
//    }
//
//
//    //Get current date to show selected circle
//    @Throws(ParseException::class)
//    private fun getCurrentDate() {
//
//        System.out.println("Current time => " + c.getTime())
//        todayDate = format.format(c.getTime())
//        temp_date = todayDate
//        date_tv.text = (sdf.format(format.parse(todayDate)))
//        currentDate = format.parse(todayDate)
//    }
//
//
//    //Set the slots as 24*7
//    private fun setSlots() {
//        for (i in 0 until alTime.size) {
//
//            val v = layoutInflater.inflate(R.layout.list_item_calendar, null)
//            val time_tv = v.findViewById(R.id.time_tv) as TextView
//
//            val one_view = v.findViewById(R.id.one_view) as View
//            if (i == 7 || i == 8) {
//                one_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                one_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//            val two_view = v.findViewById(R.id.two_view) as View
//
//
//            if (i == 7 || i == 8) {
//                two_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                two_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//
//            val three_view = v.findViewById(R.id.three_view) as View
//            if (i == 7 || i == 8) {
//                three_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                three_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//
//            val four_view = v.findViewById(R.id.four_view) as View
//            if (i == 7 || i == 8) {
//                four_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                four_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//
//            val five_view = v.findViewById(R.id.five_view) as View
//            if (i == 7 || i == 8) {
//                five_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                five_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//
//            val six_view = v.findViewById(R.id.six_view) as View
//            if (i == 7 || i == 8) {
//                six_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                six_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//
//            val seven_view = v.findViewById(R.id.seven_view) as View
//            if (i == 7 || i == 8) {
//                seven_view.setId(Integer.parseInt(alTime[i].substring(1, 2)))
//
//            } else {
//                seven_view.setId(Integer.parseInt(alTime[i].substring(0, 2)))
//
//            }
//
//            var myVal = i
//            setData(one_view, two_view, three_view, four_view, five_view, six_view, seven_view)
//            one_view.setOnClickListener {
//
//
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//
//
//                val sDate = alNext[0].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(one_view, alNext[0].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00")) {
//                    callMethodForHashmap(one_view, alNext[0].dateString, time_tv.getText().toString().trim(), "23:59")
//
//                } else {
//                    callMethodForHashmap(one_view, alNext[0].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//
//            }
//
//            two_view.setOnClickListener {
//                val va = alTime[i]
//                Log.e("Position", myVal.toString() + "")
//                Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext[1].dateString)
//
//
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//
//
//                val sDate = alNext[1].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(two_view, alNext[1].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00", true)) {
//                    callMethodForHashmap(two_view, alNext[1].dateString, time_tv.getText().toString().trim(), "23:59")
//                } else {
//                    callMethodForHashmap(two_view, alNext[1].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//
//            }
//
//            three_view.setOnClickListener {
//                Log.e("Position", i.toString() + "")
//                Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext[2].dateString)
//
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//
//
//                val sDate = alNext[2].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(three_view, alNext[2].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00")) {
//                    callMethodForHashmap(three_view, alNext[2].dateString, time_tv.getText().toString().trim(), "23:59")
//                } else {
//                    callMethodForHashmap(three_view, alNext[2].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//            }
//
//            four_view.setOnClickListener {
//                Log.e("Position", i.toString() + "")
//                Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext[3].dateString)
//
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//
//
//                val sDate = alNext[3].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(four_view, alNext[3].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00")) {
//                    callMethodForHashmap(four_view, alNext[3].dateString, time_tv.getText().toString().trim(), "23:59")
//                } else {
//                    callMethodForHashmap(four_view, alNext[3].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//            }
//
//            five_view.setOnClickListener {
//                Log.e("Position", i.toString() + "")
//
//                Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext[4].dateString)
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//                val sDate = alNext[4].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(five_view, alNext[4].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00")) {
//                    callMethodForHashmap(five_view, alNext[4].dateString, time_tv.getText().toString().trim(), "23:59")
//                } else {
//                    callMethodForHashmap(five_view, alNext[4].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//            }
//
//            six_view.setOnClickListener {
//                Log.e("Position", i.toString() + "")
//
//                Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext[5].dateString)
//
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//
//                val sDate = alNext[5].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(six_view, alNext[5].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00")) {
//                    callMethodForHashmap(six_view, alNext[5].dateString, time_tv.getText().toString().trim(), "23:59")
//                } else {
//                    callMethodForHashmap(six_view, alNext[5].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//            }
//
//            seven_view.setOnClickListener {
//                Log.e("Position", i.toString() + "")
//
//                Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext[6].dateString)
//                val currentTime = currentTime
//                val selectedTime = getSelectedTime(alTime[i])
//                val sDate = alNext[6].dateString
//                try {
//                    clickedDate = format.parse(sDate)
//                } catch (e: ParseException) {
//                    e.printStackTrace()
//                }
//
//                if (clickedDate.before(currentDate)) {
//                    Toast.makeText(this@ActivityCalendar, "Date has passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && currentTime!!.after(selectedTime)) {
//                    Toast.makeText(this@ActivityCalendar, "Time has Passed", Toast.LENGTH_SHORT).show()
//                } else if (clickedDate.equals(currentDate) && !currentTime!!.after(selectedTime) && !alTime[i].equals("23:00")) {
//                    callMethodForHashmap(seven_view, alNext[6].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//
//                } else if (alTime[i].equals("23:00")) {
//                    callMethodForHashmap(seven_view, alNext[6].dateString, time_tv.getText().toString().trim(), "23:59")
//                } else {
//                    callMethodForHashmap(seven_view, alNext[6].dateString, time_tv.getText().toString().trim(), alTime[i + 1])
//                }
//
//            }
//
//            time_tv.getLayoutParams().width = WIDTH
//            time_tv.getLayoutParams().height = WIDTH
//            one_view.getLayoutParams().width = WIDTH
//            one_view.getLayoutParams().height = WIDTH
//            two_view.getLayoutParams().width = WIDTH
//            two_view.getLayoutParams().height = WIDTH
//            three_view.getLayoutParams().width = WIDTH
//            three_view.getLayoutParams().height = WIDTH
//            four_view.getLayoutParams().width = WIDTH
//            four_view.getLayoutParams().height = WIDTH
//            five_view.getLayoutParams().width = WIDTH
//            five_view.getLayoutParams().height = WIDTH
//            six_view.getLayoutParams().width = WIDTH
//            six_view.getLayoutParams().height = WIDTH
//            seven_view.getLayoutParams().width = WIDTH
//            seven_view.getLayoutParams().height = WIDTH
//            time_tv.setText(alTime[i])
//
//            slots_ll.addView(v)
//        }
//
//    }
//
//    private fun getSelectedTime(time: String): Date? {
//        var time = time
//
//
//        try {
//            calendar.setTime(timeFormat.parse(time))
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        calendar.add(Calendar.MINUTE, 45)
//        time = timeFormat.format(calendar.getTime())
//        var selectedTime: Date? = null
//        try {
//            // selectedTime = new SimpleDateFormat("hh:mm").parse(time);
//            selectedTime = timeFormat.parse(time)
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        }
//
//        return selectedTime
//
//    }
//
//    private fun setData(one_view: View, two_view: View, three_view: View, four_view: View, five_view: View, six_view: View, seven_view: View) {
//        for (i in 0 until alNext.size) {
//            if (hashMap.containsKey(alNext[i].dateString)) {
//                val id = one_view.getId()
//                if (id == 0) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "00:00", i, alNext[i].dateString)
//                } else if (id == 1) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "01:00", i, alNext[i].dateString)
//
//                } else if (id == 2) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "02:00", i, alNext[i].dateString)
//
//
//                } else if (id == 3) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "03:00", i, alNext[i].dateString)
//
//
//                } else if (id == 4) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "04:00", i, alNext[i].dateString)
//
//
//                } else if (id == 5) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "05:00", i, alNext[i].dateString)
//
//
//                } else if (id == 6) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "06:00", i, alNext[i].dateString)
//
//
//                } else if (id == 7) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "07:00", i, alNext[i].dateString)
//
//
//                } else if (id == 8) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "08:00", i, alNext[i].dateString)
//
//
//                } else if (id == 9) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "09:00", i, alNext[i].dateString)
//
//
//                } else if (id == 10) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "10:00", i, alNext[i].dateString)
//
//
//                } else if (id == 11) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "11:00", i, alNext[i].dateString)
//
//
//                } else if (id == 12) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "12:00", i, alNext[i].dateString)
//
//
//                } else if (id == 13) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "13:00", i, alNext[i].dateString)
//
//
//                } else if (id == 14) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "14:00", i, alNext[i].dateString)
//
//
//                } else if (id == 15) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "15:00", i, alNext[i].dateString)
//
//
//                } else if (id == 16) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "16:00", i, alNext[i].dateString)
//
//                } else if (id == 17) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "17:00", i, alNext[i].dateString)
//
//
//                } else if (id == 18) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "18:00", i, alNext[i].dateString)
//
//
//                } else if (id == 19) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "19:00", i, alNext[i].dateString)
//
//
//                } else if (id == 20) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "20:00", i, alNext[i].dateString)
//
//
//                } else if (id == 21) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "21:00", i, alNext[i].dateString)
//
//
//                } else if (id == 22) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "22:00", i, alNext[i].dateString)
//
//
//                } else if (id == 23) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "23:00", i, alNext[i].dateString)
//
//
//                }
//
//
//            }
//        }
//
//
//    }
//
//    private fun refillColor(one_view: View, two_view: View, three_view: View, four_view: View, five_view: View, six_view: View, seven_view: View, id: String, currentListValue: Int, dateString: String) {
//        val alTime = hashMap[dateString]
//
//        if (currentListValue == 0) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        one_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        one_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//        if (currentListValue == 1) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        two_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        two_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//        if (currentListValue == 2) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        three_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        three_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//        if (currentListValue == 3) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        four_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        four_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//        if (currentListValue == 4) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        five_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        five_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//        if (currentListValue == 5) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        six_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        six_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//        if (currentListValue == 6) {
//            for (i in 0 until alTime!!.size) {
//                if (alTime.get(i).start_time.equals(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        seven_view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                    } else {
//                        seven_view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    private fun callMethodForHashmap(view: View, dateString: String, start_time: String, end_time: String) {
//        val sdk = android.os.Build.VERSION.SDK_INT
//        val timeModalClass = TimeModalClass(dateString,start_time, end_time)
//
//        if (hashMap.containsKey(dateString)) {
//            var isValue = false
//            var pos = -1
//            val timeData = hashMap[dateString]
//            for (i in 0 until timeData!!.size) {
//                if (timeData.get(i).start_time.equals(timeModalClass.start_time)) {
//                    isValue = true
//                    pos = i
//                } else {
//
//                }
//            }
//            if (!isValue) {
//                timeData!!.add(timeModalClass)
//                hashMap[dateString] = timeData
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//                } else {
//                    view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//                }
//
//            } else {
//                timeData.removeAt(pos)
//                hashMap[dateString] = timeData
//                val al = hashMap[dateString]
//                if (al!!.size === 0) {
//                    hashMap.remove(dateString)
//                }
//
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_timmings))
//                } else {
//                    view.setBackground(resources.getDrawable(R.drawable.sq_box_timmings))
//                }
//            }
//
//
//        } else {
//            val timeData = ArrayList<TimeModalClass>()
//            timeData.add(timeModalClass)
//            hashMap[dateString] = timeData
//            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                view.setBackgroundDrawable(resources.getDrawable(R.drawable.sq_box_orange))
//            } else {
//                view.setBackground(resources.getDrawable(R.drawable.sq_box_orange))
//            }
//        }
//
//
//    }
//
//
////Add Time from 24 hrs
//
//    private fun addTimeMethod() {
//        alTime.add("00:00")
//        alTime.add("01:00")
//        alTime.add("02:00")
//        alTime.add("03:00")
//        alTime.add("04:00")
//        alTime.add("05:00")
//        alTime.add("06:00")
//        alTime.add("07:00")
//        alTime.add("08:00")
//        alTime.add("09:00")
//        alTime.add("10:00")
//        alTime.add("11:00")
//        alTime.add("12:00")
//        alTime.add("13:00")
//        alTime.add("14:00")
//        alTime.add("15:00")
//        alTime.add("16:00")
//        alTime.add("17:00")
//        alTime.add("18:00")
//        alTime.add("19:00")
//        alTime.add("20:00")
//        alTime.add("21:00")
//        alTime.add("22:00")
//        alTime.add("23:00")
//
//
//    }
//
//
//    private fun getSelector() {
//        for (i in 0 until alNext.size) {
//            if (i == 0) {
//                dayOne_tv.setTextColor(resources.getColor(R.color.color_white))
//                dayOne_tv.setBackgroundResource(0)
//            } else if (i == 1) {
//                dayTwo_tv.setTextColor(resources.getColor(R.color.color_white))
//                dayTwo_tv.setBackgroundResource(0)
//            } else if (i == 2) {
//                dayThree_tv.setTextColor(resources.getColor(R.color.color_white))
//                dayThree_tv.setBackgroundResource(0)
//            } else if (i == 3) {
//                dayFour_tv.setTextColor(resources.getColor(R.color.color_white))
//                dayFour_tv.setBackgroundResource(0)
//            } else if (i == 4) {
//                dayFive_tv.setTextColor(resources.getColor(R.color.color_white))
//                dayFive_tv.setBackgroundResource(0)
//            } else if (i == 5) {
//                daySix_tv.setTextColor(resources.getColor(R.color.color_white))
//                daySix_tv.setBackgroundResource(0)
//            } else if (i == 6) {
//                daySeven_tv.setTextColor(resources.getColor(R.color.color_white))
//                daySeven_tv.setBackgroundResource(0)
//            }
//            if (alNext[i].dateString.equals(todayDate)) {
//                val sdk = android.os.Build.VERSION.SDK_INT
//                if (i == 0) {
//                    dayOne_tv.setTextColor(resources.getColor(R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayOne_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayOne_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 1) {
//                    dayTwo_tv.setTextColor(resources.getColor(R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayTwo_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayTwo_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 2) {
//                    dayThree_tv.setTextColor(resources.getColor(R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayThree_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayThree_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 3) {
//                    dayFour_tv.setTextColor(resources.getColor(R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFour_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayFour_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 4) {
//                    dayFive_tv.setTextColor(resources.getColor(com.musicseque.R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFive_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayFive_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 5) {
//                    daySix_tv.setTextColor(resources.getColor(R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySix_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        daySix_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 6) {
//                    daySeven_tv.setTextColor(resources.getColor(R.color.color_white))
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySeven_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        daySeven_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                }
//            }
//        }
//        for (i in 0 until alNext.size) {
//            if (alNext[i].dateString.equals(temp_date)) {
//                val sdk = android.os.Build.VERSION.SDK_INT
//
//                if (i == 0) {
//                    dayOne_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayOne_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayOne_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 1) {
//                    dayTwo_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayTwo_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayTwo_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 2) {
//                    dayThree_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayThree_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayThree_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 3) {
//                    dayFour_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFour_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayFour_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 4) {
//                    dayFive_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFive_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        dayFive_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 5) {
//                    daySix_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySix_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        daySix_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                } else if (i == 6) {
//                    daySeven_tv.setTextColor(resources.getColor(R.color.color_white))
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySeven_tv.setBackgroundDrawable(resources.getDrawable(R.drawable.circle_orange))
//                    } else {
//                        daySeven_tv.setBackground(resources.getDrawable(R.drawable.circle_orange))
//                    }
//                }
//                last_temp_date = temp_date
//            }
//        }
//
//
//    }
//
//    private fun chageSelectedColor(selected: TextView, child1: TextView, child2: TextView, child3: TextView, child4: TextView, child5: TextView, child6: TextView) {
//        selected.setTextColor(resources.getColor(R.color.color_white))
//        child1.setTextColor(resources.getColor(R.color.color_white))
//        child2.setTextColor(resources.getColor(R.color.color_white))
//        child3.setTextColor(resources.getColor(R.color.color_white))
//        child4.setTextColor(resources.getColor(R.color.color_white))
//        child5.setTextColor(resources.getColor(R.color.color_white))
//        child6.setTextColor(resources.getColor(R.color.color_white))
//        child1.setBackgroundResource(0)
//        child1.setBackgroundResource(0)
//        child2.setBackgroundResource(0)
//        child3.setBackgroundResource(0)
//        child4.setBackgroundResource(0)
//        child5.setBackgroundResource(0)
//        child6.setBackgroundResource(0)
//    }
//
//
//    //Get the previous week
//    fun getWeekDayPrev() {
//
//        weekDaysCount--
//        val now1 = Calendar.getInstance()
//        val now = now1.clone() as Calendar
//
//        val delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1
//        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount)
//        now.add(Calendar.DAY_OF_MONTH, delta)
//        for (i in 0..6) {
//
//            //val calendarModal = CalendarModal()
//            val day = format.format(now.getTime()).substring(0, 2)
//            // calendarModal.setIntDay(day)
//            detailDate(format.format(now.getTime()), now, i, day)
//        }
//        showCustomCalendar()
//        slots_ll.removeAllViews()
//        setSlots()
//
//    }
//
//
//    //GEt the next week from current week
//    fun getWeekDayNext() {
//
//
//        weekDaysCount++
//        val now1 = Calendar.getInstance()
//        val now = now1.clone() as Calendar
//
//        val delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1
//        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount)
//        now.add(Calendar.DAY_OF_MONTH, delta)
//        for (i in 0..6) {
//            // val calendarModal = CalendarModal()
//            val day = format.format(now.getTime()).substring(0, 2)
//            // calendarModal.setIntDay(day)
//            detailDate(format.format(now.getTime()), now, i, day)
//        }
//        showCustomCalendar()
//        slots_ll.removeAllViews()
//        setSlots()
//
//    }
//
//
//    //Set the week days on the row
//    private fun showCustomCalendar() {
//        dayOne_tv.setText(alNext[0].intDay)
//        dayTwo_tv.setText(alNext[1].intDay)
//        dayThree_tv.setText(alNext[2].intDay)
//        dayFour_tv.setText(alNext[3].intDay)
//        dayFive_tv.setText(alNext[4].intDay)
//        daySix_tv.setText(alNext[5].intDay)
//        daySeven_tv.setText(alNext[6].intDay)
//        getSelector()
//    }
//
//
//    //Get the Full Date to show "Wed, 17 July 2017"
//    private fun detailDate(string: String, calendar: Calendar, pos: Int, day: String) {
//
//        try {
//            val date = format.parse(string)
//            val formattedDate = sdf.format(date)
//            val calendarModal = CalendarModal(day, formattedDate, string, false)
//            alNext.add(calendarModal)
//
//
//            calendar.add(Calendar.DAY_OF_MONTH, 1)
//
//        } catch (e: ParseException) {
//            e.printStackTrace()
//        } finally {
//        }
//    }
//
//
//    override fun onClick(view: View) {
//        when (view.id) {
//
//            R.id.ivTick -> {
//                var alTime: ArrayList<DateTimeModel> = ArrayList()
//
//                for ((k, v) in hashMap.entries) {
//                   alTime.add(DateTimeModel(v))
//                }
//
//            Log.e("Hainji","")
//            }
//            R.id.previous_iv -> {
//                if (alNext.size > 0) {
//                    for (i in 0 until alNext.size) {
//                        alNext[i].isCurrentDate = false
//
//
//                    }
//                    chageSelectedColor(dayFive_tv, dayOne_tv, daySeven_tv, dayFour_tv, dayThree_tv, dayTwo_tv, daySix_tv)
//                    slots_ll.removeAllViews()
//                }
//                alNext.clear()
//                getWeekDayPrev()
//                getSelector()
//            }
//            R.id.next_iv -> {
//                if (alNext.size > 0) {
//                    for (i in 0 until alNext.size) {
//                        alNext[i].isCurrentDate = false
//
//                    }
//                    chageSelectedColor(dayFive_tv, dayOne_tv, daySeven_tv, dayFour_tv, dayThree_tv, dayTwo_tv, daySix_tv)
//                }
//                alNext.clear()
//                getWeekDayNext()
//                getSelector()
//            }
//            R.id.dayOne_tv ->
//
//                if (todayDate != alNext[0].dateString) {
//                    if (temp_date != alNext[0].dateString) {
//                        temp_date = alNext[0].dateString
//                        date_tv.setText(alNext[0].fullDate)
//                        getSelector()
//                    }
//                }
//            R.id.dayTwo_tv -> if (todayDate != alNext[1].dateString) {
//                if (temp_date != alNext[1].dateString) {
//                    temp_date = alNext[1].dateString
//                    date_tv.setText(alNext[1].fullDate)
//                    getSelector()
//                }
//            }
//            R.id.dayThree_tv ->
//
//                if (todayDate != alNext[2].dateString) {
//                    if (temp_date != alNext[2].dateString) {
//                        temp_date = alNext[2].dateString
//                        date_tv.setText(alNext[2].fullDate)
//                        getSelector()
//                    }
//                }
//            R.id.dayFour_tv ->
//
//                if (todayDate != alNext[3].dateString) {
//                    if (temp_date != alNext[3].dateString) {
//                        temp_date = alNext[3].dateString
//                        date_tv.setText(alNext[3].fullDate)
//                        getSelector()
//                    }
//                }
//            R.id.dayFive_tv ->
//
//                if (todayDate != alNext[4].dateString) {
//                    if (temp_date != alNext[4].dateString) {
//                        temp_date = alNext[4].dateString
//                        date_tv.setText(alNext[4].fullDate)
//                        getSelector()
//                    }
//                }
//            R.id.daySix_tv ->
//
//                if (todayDate != alNext[5].dateString) {
//                    if (temp_date != alNext[5].dateString) {
//                        temp_date = alNext[5].dateString
//                        date_tv.setText(alNext[5].fullDate)
//                        getSelector()
//                    }
//                }
//            R.id.daySeven_tv ->
//
//                if (todayDate != alNext[6].dateString) {
//                    if (temp_date != alNext[6].dateString) {
//                        temp_date = alNext[6].dateString
//                        date_tv.setText(alNext[6].fullDate)
//                        getSelector()
//                    }
//                }
//        }
//    }
//
//
//}
//
//
//
