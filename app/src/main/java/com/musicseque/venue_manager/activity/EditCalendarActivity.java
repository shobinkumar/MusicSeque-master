//package com.musicseque.venue_manager.activity;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.AppCompatImageView;
//import android.util.Log;
//import android.view.View;
//import android.widget.LinearLayout;
//import android.widget.RadioButton;
//import android.widget.RadioGroup;
//import android.widget.Toast;
//
//import com.google.gson.Gson;
//import com.kantech.krank.R;
//import com.kantech.krank.customtext.CustomRegularText;
//import com.kantech.krank.models.CalendarModal;
//import com.kantech.krank.models.InternalData;
//import com.kantech.krank.models.MyParkingTime;
//import com.kantech.krank.models.TimeModalClass;
//import com.kantech.krank.utilities.Constants;
//import com.kantech.krank.utilities.Utils;
//
//import java.text.DateFormat;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.ArrayList;
//import java.util.Calendar;
//import java.util.Date;
//import java.util.GregorianCalendar;
//import java.util.HashMap;
//import java.util.Iterator;
//import java.util.Map;
//import java.util.Set;
//
///**
// * Created by sourav on 11/7/17.
// */
//
//
//public class EditCalendarActivity extends AppCompatActivity implements View.OnClickListener {
//
//    DateFormat format;
//    SimpleDateFormat timeFormat;
//    Calendar calendar;
//    Calendar addSixMonthsCalendar;
//    private String sDateSixMonths;
//
//    //Toolbar
//    AppCompatImageView toolbar_back;
//    CustomRegularText tool_title;
//    AppCompatImageView menu_iv;
//    LinearLayout menu_ll;
//
//    LinearLayout tick_ll;
//    AppCompatImageView tick_iv;
//
//    RadioGroup radioGroup;
//    RadioButton fixed_rb, negotiable_rb;
//
//    AppCompatImageView previous_iv, next_iv;
//    CustomRegularText date_tv;
//
//    CustomRegularText textsunday_tv;
//    CustomRegularText dayOne_tv;
//    CustomRegularText textmonday_tv;
//    CustomRegularText dayTwo_tv;
//    CustomRegularText texttuesday_tv;
//    CustomRegularText dayThree_tv;
//    CustomRegularText textwednesday_tv;
//    CustomRegularText dayFour_tv;
//    CustomRegularText textthursday_tv;
//    CustomRegularText dayFive_tv;
//    CustomRegularText textfriday_tv;
//    CustomRegularText daySix_tv;
//    CustomRegularText textsaturday_tv;
//    CustomRegularText daySeven_tv;
//
//    LinearLayout slots_ll;
//
//
//    ArrayList<String> alTime = new ArrayList<>();
//
//
//    ArrayList<CalendarModal> alNext = new ArrayList<>();
//
//    String temp_date;
//    String last_temp_date;
//
//
//    String todayDate;
//    int weekDaysCount = 1;
//
//    String parking_type;
//    String isParkingFixed = "fixed";
//    boolean isForEditing;
//
//
//    String parking_price;
//    HashMap<String, ArrayList<TimeModalClass>> hashMap = new HashMap<>();
//    HashMap<String, ArrayList<TimeModalClass>> hashMapCopy = new HashMap<>();
//
//
//    Gson gson;
//    SimpleDateFormat sdf = new SimpleDateFormat("EEEE MMM d, yyyy");
//
//
//    Date currentDate;
//    Date clickedDate;
//    Date sixMonthDate;
//
//    String slot_id;
//    private String sSlotstatus;
//
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_calendar);
//
//        if (isForEditing) {
//            InternalData internalData = (InternalData) getIntent().getSerializableExtra("parkings");
//            try {
//                addDatatoHashMap(internalData);
//                hashMapCopy = ActivityEditParking.hashMapCopy;
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//
//        }
//
//
//        //Add Times "00:00" "01:00"
//        addTimeMethod();
//
//
//        try {
//            getCurrentDate();
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//
//
//        textsunday_tv.post(new Runnable() {
//            public void run() {
//                Constants.WIDTH = textsunday_tv.getWidth();
//                getWeekDayPrev();
//
//
//            }
//        });
//
//
//        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                // find which radio button is selected
//
//                if (checkedId == R.id.fixed_rb) {
//                    fixed_rb.setTextColor(getResources().getColor(R.color.black));
//                    negotiable_rb.setTextColor(getResources().getColor(R.color.round_grey_color));
//                    isParkingFixed = "fixed";
//                } else if (checkedId == R.id.negotiable_rb) {
//                    negotiable_rb.setTextColor(getResources().getColor(R.color.black));
//                    fixed_rb.setTextColor(getResources().getColor(R.color.round_grey_color));
//                    isParkingFixed = "negotiable";
//
//                }
//            }
//
//
//        });
//
//
//    }
//
//    private void addDatatoHashMap(InternalData internalData) throws ParseException {
//        sSlotstatus = internalData.getSlot_status();
//        slot_id = internalData.getSlot_id();
//        String priceType = internalData.getType();
//        if (priceType.equalsIgnoreCase("fixed"))
//            fixed_rb.setChecked(true);
//        else
//            negotiable_rb.setChecked(true);
//
//        for (int i = 0; i < internalData.getTimes().size(); i++) {
//
//            hashMap.put(internalData.getTimes().get(i).getDate(), internalData.getTimes().get(i).getSelect_times());
//        }
//
//
//    }
//
//
//    //Get current date to show selected circle
//    private void getCurrentDate() throws ParseException {
//        Calendar c = Calendar.getInstance();
//        System.out.println("Current time => " + c.getTime());
//        todayDate = format.format(c.getTime());
//        temp_date = todayDate;
//        date_tv.setText(sdf.format(format.parse(todayDate)));
//        currentDate = format.parse(todayDate);
//    }
//
//    private void getDateAfterSixMonths() throws ParseException {
//        addSixMonthsCalendar.add(Calendar.MONTH, 6);
//        sDateSixMonths = format.format(addSixMonthsCalendar.getTime());
//        sixMonthDate = format.parse(sDateSixMonths);
//    }
//
//    //Set the slots as 24*7
//    private void setSlots() {
//        for (int i = 0; i < alTime.size(); i++) {
//
//            View v = getLayoutInflater().inflate(R.layout.list_item_calendar, null);
//            final CustomRegularText time_tv = (CustomRegularText) v.findViewById(R.id.time_tv);
//
//            final View one_view = (View) v.findViewById(R.id.one_view);
//            if (i == 7 || i == 8) {
//                one_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                one_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//            final View two_view = (View) v.findViewById(R.id.two_view);
//
//
//            if (i == 7 || i == 8) {
//                two_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                two_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//
//            final View three_view = (View) v.findViewById(R.id.three_view);
//            if (i == 7 || i == 8) {
//                three_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                three_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//
//            final View four_view = (View) v.findViewById(R.id.four_view);
//            if (i == 7 || i == 8) {
//                four_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                four_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//
//            final View five_view = (View) v.findViewById(R.id.five_view);
//            if (i == 7 || i == 8) {
//                five_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                five_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//
//            final View six_view = (View) v.findViewById(R.id.six_view);
//            if (i == 7 || i == 8) {
//                six_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                six_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//
//            final View seven_view = (View) v.findViewById(R.id.seven_view);
//            if (i == 7 || i == 8) {
//                seven_view.setId(Integer.parseInt(alTime.get(i).substring(1, 2)));
//
//            } else {
//                seven_view.setId(Integer.parseInt(alTime.get(i).substring(0, 2)));
//
//            }
//
//            final int finalI = i;
//            final int finalI1 = i;
//
//
//            setData(one_view, two_view, three_view, four_view, five_view, six_view, seven_view);
//            one_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//
//
//                    String sDate = alNext.get(0).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (!canEdit(alNext.get(0).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(one_view, alNext.get(0).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(one_view, alNext.get(0).getDateString(), time_tv.getText().toString().trim(), "23:59");
//
//                    } else {
//                        callMethodForHashmap(one_view, alNext.get(0).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//
//                }
//            });
//            two_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Position", finalI + "");
//                    Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext.get(1).getDateString());
//
//
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//
//
//                    String sDate = alNext.get(1).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (!canEdit(alNext.get(1).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(two_view, alNext.get(1).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(two_view, alNext.get(1).getDateString(), time_tv.getText().toString().trim(), "23:59");
//                    } else {
//                        callMethodForHashmap(two_view, alNext.get(1).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//
//                }
//            });
//            three_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Position", finalI + "");
//                    Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext.get(2).getDateString());
//
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//
//
//                    String sDate = alNext.get(2).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (!canEdit(alNext.get(2).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(three_view, alNext.get(2).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(three_view, alNext.get(2).getDateString(), time_tv.getText().toString().trim(), "23:59");
//                    } else {
//                        callMethodForHashmap(three_view, alNext.get(2).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//                }
//            });
//            four_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Position", finalI + "");
//                    Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext.get(3).getDateString());
//
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//
//
//                    String sDate = alNext.get(3).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (!canEdit(alNext.get(3).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(four_view, alNext.get(3).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(four_view, alNext.get(3).getDateString(), time_tv.getText().toString().trim(), "23:59");
//                    } else {
//                        callMethodForHashmap(four_view, alNext.get(3).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//                }
//            });
//            five_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Position", finalI + "");
//
//                    Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext.get(4).getDateString());
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//                    String sDate = alNext.get(4).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (!canEdit(alNext.get(4).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(five_view, alNext.get(4).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(five_view, alNext.get(4).getDateString(), time_tv.getText().toString().trim(), "23:59");
//                    } else {
//                        callMethodForHashmap(five_view, alNext.get(4).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//                }
//            });
//            six_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Position", finalI + "");
//
//                    Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext.get(5).getDateString());
//
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//
//                    String sDate = alNext.get(5).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (!canEdit(alNext.get(5).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(six_view, alNext.get(5).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(six_view, alNext.get(5).getDateString(), time_tv.getText().toString().trim(), "23:59");
//                    } else {
//                        callMethodForHashmap(six_view, alNext.get(5).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//                }
//            });
//            seven_view.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    Log.e("Position", finalI + "");
//
//                    Log.i("dateTime", time_tv.getText().toString().trim() + ", " + alNext.get(6).getDateString());
//                    Date currentTime = getCurrentTime();
//                    Date selectedTime = getSelectedTime(alTime.get(finalI1));
//                    String sDate = alNext.get(6).getDateString();
//                    try {
//                        clickedDate = format.parse(sDate);
//                    } catch (ParseException e) {
//                        e.printStackTrace();
//                    }
//
//                    if (clickedDate.before(currentDate)) {
//                        Toast.makeText(EditCalendarActivity.this, "Date has passed", Toast.LENGTH_SHORT).show();
//                    } else if (clickedDate.equals(currentDate) && currentTime.after(selectedTime)) {
//                        Toast.makeText(EditCalendarActivity.this, "Time has Passed", Toast.LENGTH_SHORT).show();
//                    } else if (!canEdit(alNext.get(6).getDateString(), time_tv.getText().toString().trim())) {
//                        Toast.makeText(EditCalendarActivity.this, "You can't edit booked slot", Toast.LENGTH_SHORT).show();
//
//
//                    } else if (clickedDate.after(sixMonthDate)) {
//                        Utils.showToast(EditCalendarActivity.this, getResources().getString(R.string.str_not_after_six));
//
//                    } else if (clickedDate.equals(currentDate) && !currentTime.after(selectedTime) && !alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(seven_view, alNext.get(6).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//
//                    } else if (alTime.get(finalI).equalsIgnoreCase("23:00")) {
//                        callMethodForHashmap(seven_view, alNext.get(6).getDateString(), time_tv.getText().toString().trim(), "23:59");
//                    } else {
//                        callMethodForHashmap(seven_view, alNext.get(6).getDateString(), time_tv.getText().toString().trim(), alTime.get(finalI + 1));
//                    }
//
//                }
//            });
//            time_tv.getLayoutParams().width = Constants.WIDTH;
//            time_tv.getLayoutParams().height = Constants.WIDTH;
//            one_view.getLayoutParams().width = Constants.WIDTH;
//            one_view.getLayoutParams().height = Constants.WIDTH;
//            two_view.getLayoutParams().width = Constants.WIDTH;
//            two_view.getLayoutParams().height = Constants.WIDTH;
//            three_view.getLayoutParams().width = Constants.WIDTH;
//            three_view.getLayoutParams().height = Constants.WIDTH;
//            four_view.getLayoutParams().width = Constants.WIDTH;
//            four_view.getLayoutParams().height = Constants.WIDTH;
//            five_view.getLayoutParams().width = Constants.WIDTH;
//            five_view.getLayoutParams().height = Constants.WIDTH;
//            six_view.getLayoutParams().width = Constants.WIDTH;
//            six_view.getLayoutParams().height = Constants.WIDTH;
//            seven_view.getLayoutParams().width = Constants.WIDTH;
//            seven_view.getLayoutParams().height = Constants.WIDTH;
//            time_tv.setText(alTime.get(i));
//
//            slots_ll.addView(v);
//        }
//
//    }
//
//    private boolean canEdit(String dateString, String start_time) {
//        boolean value = true;
//
//        if (hashMap.containsKey(dateString)) {
//            ArrayList<TimeModalClass> al = hashMap.get(dateString);
//            for (int i = 0; i < al.size(); i++) {
//                if (al.get(i).getStart_time().equalsIgnoreCase(start_time)) {
//                    if (al.get(i).getSlot_status().equalsIgnoreCase("0")) {
//                        value = false;
//                        break;
//                    }
//
//                }
//            }
//
//        } else {
//            value = true;
//
//        }
//        return value;
//    }
//
//    private Date getSelectedTime(String time) {
//
//
//        try {
//            calendar.setTime(timeFormat.parse(time));
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        calendar.add(Calendar.MINUTE, 45);
//        time = timeFormat.format(calendar.getTime());
//        Date selectedTime = null;
//        try {
//            // selectedTime = new SimpleDateFormat("hh:mm").parse(time);
//            selectedTime = timeFormat.parse(time);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return selectedTime;
//
//    }
//
//    private Date getCurrentTime() {
//        calendar = Calendar.getInstance();
//
//        String str = timeFormat.format(calendar.getTime());
//        Date currentTime = null;
//        try {
//            currentTime = timeFormat.parse(str);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return currentTime;
//    }
//
//    private void setData(View one_view, View two_view, View three_view, View four_view, View five_view, View six_view, View seven_view) {
//        for (int i = 0; i < alNext.size(); i++) {
//            if (hashMap.containsKey(alNext.get(i).getDateString())) {
//                int id = one_view.getId();
//                if (id == 0) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "00:00", i, alNext.get(i).getDateString());
//                } else if (id == 1) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "01:00", i, alNext.get(i).getDateString());
//
//                } else if (id == 2) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "02:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 3) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "03:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 4) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "04:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 5) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "05:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 6) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "06:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 7) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "07:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 8) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "08:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 9) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "09:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 10) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "10:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 11) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "11:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 12) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "12:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 13) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "13:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 14) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "14:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 15) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "15:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 16) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "16:00", i, alNext.get(i).getDateString());
//
//                } else if (id == 17) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "17:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 18) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "18:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 19) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "19:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 20) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "20:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 21) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "21:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 22) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "22:00", i, alNext.get(i).getDateString());
//
//
//                } else if (id == 23) {
//                    refillColor(one_view, two_view, three_view, four_view, five_view, six_view, seven_view, "23:00", i, alNext.get(i).getDateString());
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
//    private void refillColor(View one_view, View two_view, View three_view, View four_view, View five_view, View six_view, View seven_view, String id, int currentListValue, String dateString) {
//        ArrayList<TimeModalClass> alTime = hashMap.get(dateString);
//
//        if (currentListValue == 0) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        one_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        one_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//        if (currentListValue == 1) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        two_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        two_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//        if (currentListValue == 2) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        three_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        three_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//        if (currentListValue == 3) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        four_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        four_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//        if (currentListValue == 4) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        five_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        five_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//        if (currentListValue == 5) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        six_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        six_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//        if (currentListValue == 6) {
//            for (int i = 0; i < alTime.size(); i++) {
//                if (alTime.get(i).getStart_time().equalsIgnoreCase(id + "")) {
//                    if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        seven_view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                    } else {
//                        seven_view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                    }
//                }
//            }
//        }
//
//
//    }
//
//    private void callMethodForHashmap(View view, String dateString, String start_time, String end_time) {
//        final int sdk = android.os.Build.VERSION.SDK_INT;
//        boolean dateContainInOld = false;
//        TimeModalClass timeModalClass = new TimeModalClass();
//        timeModalClass.setStart_time(start_time);
//        timeModalClass.setEnd_time(end_time);
//        if (hashMapCopy.containsKey(dateString)) {
//            dateContainInOld = true;
//        }
//        if (hashMap.containsKey(dateString) || hashMapCopy.containsKey(dateString)) {
//            boolean isValue = false;
//            int pos = -1;
//            ArrayList<TimeModalClass> timeData = hashMap.get(dateString);
//            ArrayList<TimeModalClass> timeDataTemp = hashMapCopy.get(dateString);
//            if (hashMap.containsKey(dateString)) {
//
//
//                for (int i = 0; i < timeData.size(); i++) {
//
//
//                    if (timeData.get(i).getStart_time().equalsIgnoreCase(timeModalClass.getStart_time())) {
//
//
//                        isValue = true;
//                        pos = i;
//                        break;
//                    } else {
//
//                    }
//                }
//            } else {
//
//            }
//
//            if (!isValue) {
//                if (dateContainInOld) {
//                    boolean containDate = false;
//                    for (int i = 0; i < timeDataTemp.size(); i++) {
//                        if (timeDataTemp.get(i).getStart_time().equalsIgnoreCase(timeModalClass.getStart_time())) {
//                            timeModalClass.setId(timeDataTemp.get(i).getId());
//                            timeModalClass.setSlot_status(timeDataTemp.get(i).getSlot_status());
//                            containDate = true;
//                            break;
//
//                        } else {
//
//                        }
//                    }
//                    if (!containDate) {
//                        timeModalClass.setId("0");
//                        timeModalClass.setSlot_status("-1");
//                    }
//                } else {
//                    timeModalClass.setId("0");
//                    timeModalClass.setSlot_status("-1");
//                }
//                timeData.add(timeModalClass);
//                hashMap.put(dateString, timeData);
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//                } else {
//                    view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//                }
//
//            } else {
//                timeData.remove(pos);
//                hashMap.put(dateString, timeData);
//                ArrayList<TimeModalClass> al = hashMap.get(dateString);
//               /* if (al.size() == 0) {
//                    hashMap.remove(dateString);
//                }*/
//
//                if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                    view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_whites));
//                } else {
//                    view.setBackground(getResources().getDrawable(R.drawable.rectangle_whites));
//                }
//            }
//
//
//        } else {
//            ArrayList<TimeModalClass> timeData = new ArrayList<>();
//            timeModalClass.setId("0");
//            timeModalClass.setSlot_status("-1");
//
//            timeData.add(timeModalClass);
//            hashMap.put(dateString, timeData);
//            if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                view.setBackgroundDrawable(getResources().getDrawable(R.drawable.rectangle_green));
//            } else {
//                view.setBackground(getResources().getDrawable(R.drawable.rectangle_green));
//            }
//        }
//
//
//    }
//
//
//    //Add Time from 24 hrs
//
//    private void addTimeMethod() {
//        alTime.add("00:00");
//        alTime.add("01:00");
//        alTime.add("02:00");
//        alTime.add("03:00");
//        alTime.add("04:00");
//        alTime.add("05:00");
//        alTime.add("06:00");
//        alTime.add("07:00");
//        alTime.add("08:00");
//        alTime.add("09:00");
//        alTime.add("10:00");
//        alTime.add("11:00");
//        alTime.add("12:00");
//        alTime.add("13:00");
//        alTime.add("14:00");
//        alTime.add("15:00");
//        alTime.add("16:00");
//        alTime.add("17:00");
//        alTime.add("18:00");
//        alTime.add("19:00");
//        alTime.add("20:00");
//        alTime.add("21:00");
//        alTime.add("22:00");
//        alTime.add("23:00");
//
//
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()) {
//
//            case R.id.toolbar_back:
//                finish();
//                overridePendingTransition(R.anim.back_in, R.anim.back_out);
//                break;
//            case R.id.tick_ll:
//                String name = getIntent().getStringExtra("calling_activity");
//                if (name.equalsIgnoreCase("com.kantech.krank.activities.my_parking.ActivityEditParking")) {
//                    if (parking_type.contains("Car")) {
//                        sendDataToCallingActivity(10);
//
//
//                    } else if (parking_type.contains("Bike")) {
//                        sendDataToCallingActivity(11);
//
//
//                    } else {
//                        sendDataToCallingActivity(12);
//
//
//                    }
//
//
//                } else if (name.equalsIgnoreCase("com.kantech.krank.activities.my_parking.ActivityAddNewParking")) {
//                    if (parking_type.contains("Car")) {
//                        sendDataToCallingActivity(7);
//
//
//                    } else if (parking_type.contains("Bike")) {
//                        sendDataToCallingActivity(8);
//
//
//                    } else {
//                        sendDataToCallingActivity(9);
//
//
//                    }
//                }
//
//
//
//
//
//
//                /*finish();
//                overridePendingTransition(R.anim.back_in, R.anim.back_out);*/
//                break;
//            case R.id.previous_iv:
//                if (alNext.size() > 0) {
//                    for (int i = 0; i < alNext.size(); i++) {
//                        alNext.get(i).setCurrentDate(false);
//
//                    }
//                    chageSelectedColor(dayFive_tv, dayOne_tv, daySeven_tv, dayFour_tv, dayThree_tv, dayTwo_tv, daySix_tv);
//                    slots_ll.removeAllViews();
//                }
//                alNext.clear();
//                getWeekDayPrev();
//                getSelector();
//                break;
//            case R.id.next_iv:
//                if (alNext.size() > 0) {
//                    for (int i = 0; i < alNext.size(); i++) {
//                        alNext.get(i).setCurrentDate(false);
//
//                    }
//                    chageSelectedColor(dayFive_tv, dayOne_tv, daySeven_tv, dayFour_tv, dayThree_tv, dayTwo_tv, daySix_tv);
//                }
//                alNext.clear();
//                getWeekDayNext();
//                getSelector();
//                break;
//            case R.id.dayOne_tv:
//
//                if (!todayDate.equals(alNext.get(0).getDateString())) {
//                    if (!temp_date.equals(alNext.get(0).getDateString())) {
//                        temp_date = alNext.get(0).getDateString();
//                        date_tv.setText(alNext.get(0).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//            case R.id.dayTwo_tv:
//                if (!todayDate.equals(alNext.get(1).getDateString())) {
//                    if (!temp_date.equals(alNext.get(1).getDateString())) {
//                        temp_date = alNext.get(1).getDateString();
//                        date_tv.setText(alNext.get(1).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//            case R.id.dayThree_tv:
//
//                if (!todayDate.equals(alNext.get(2).getDateString())) {
//                    if (!temp_date.equals(alNext.get(2).getDateString())) {
//                        temp_date = alNext.get(2).getDateString();
//                        date_tv.setText(alNext.get(2).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//            case R.id.dayFour_tv:
//
//                if (!todayDate.equals(alNext.get(3).getDateString())) {
//                    if (!temp_date.equals(alNext.get(3).getDateString())) {
//                        temp_date = alNext.get(3).getDateString();
//                        date_tv.setText(alNext.get(3).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//            case R.id.dayFive_tv:
//
//                if (!todayDate.equals(alNext.get(4).getDateString())) {
//                    if (!temp_date.equals(alNext.get(4).getDateString())) {
//                        temp_date = alNext.get(4).getDateString();
//                        date_tv.setText(alNext.get(4).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//            case R.id.daySix_tv:
//
//                if (!todayDate.equals(alNext.get(5).getDateString())) {
//                    if (!temp_date.equals(alNext.get(5).getDateString())) {
//                        temp_date = alNext.get(5).getDateString();
//                        date_tv.setText(alNext.get(5).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//            case R.id.daySeven_tv:
//
//                if (!todayDate.equals(alNext.get(6).getDateString())) {
//                    if (!temp_date.equals(alNext.get(6).getDateString())) {
//                        temp_date = alNext.get(6).getDateString();
//                        date_tv.setText(alNext.get(6).getFullDate());
//                        getSelector();
//                    }
//                }
//                break;
//
//        }
//    }
//
//    private void getSelector() {
//        for (int i = 0; i < alNext.size(); i++) {
//            if (i == 0) {
//                dayOne_tv.setTextColor(getResources().getColor(R.color.calendar_sat_sun_color));
//                dayOne_tv.setBackgroundResource(0);
//            } else if (i == 1) {
//                dayTwo_tv.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//                dayTwo_tv.setBackgroundResource(0);
//            } else if (i == 2) {
//                dayThree_tv.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//                dayThree_tv.setBackgroundResource(0);
//            } else if (i == 3) {
//                dayFour_tv.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//                dayFour_tv.setBackgroundResource(0);
//            } else if (i == 4) {
//                dayFive_tv.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//                dayFive_tv.setBackgroundResource(0);
//            } else if (i == 5) {
//                daySix_tv.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//                daySix_tv.setBackgroundResource(0);
//            } else if (i == 6) {
//                daySeven_tv.setTextColor(getResources().getColor(R.color.calendar_sat_sun_color));
//                daySeven_tv.setBackgroundResource(0);
//            }
//            if (alNext.get(i).getDateString().equalsIgnoreCase(todayDate)) {
//                final int sdk = android.os.Build.VERSION.SDK_INT;
//                if (i == 0) {
//                    dayOne_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayOne_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayOne_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 1) {
//                    dayTwo_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayTwo_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayTwo_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 2) {
//                    dayThree_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayThree_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayThree_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 3) {
//                    dayFour_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFour_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayFour_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 4) {
//                    dayFive_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFive_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayFive_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 5) {
//                    daySix_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySix_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        daySix_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 6) {
//                    daySeven_tv.setTextColor(getResources().getColor(R.color.white));
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySeven_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        daySeven_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                }
//            }
//        }
//        for (int i = 0; i < alNext.size(); i++) {
//            if (alNext.get(i).getDateString().equalsIgnoreCase(temp_date)) {
//                final int sdk = android.os.Build.VERSION.SDK_INT;
//
//                if (i == 0) {
//                    dayOne_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayOne_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayOne_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 1) {
//                    dayTwo_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayTwo_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayTwo_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 2) {
//                    dayThree_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayThree_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayThree_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 3) {
//                    dayFour_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFour_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayFour_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 4) {
//                    dayFive_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        dayFive_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        dayFive_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 5) {
//                    daySix_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySix_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        daySix_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                } else if (i == 6) {
//                    daySeven_tv.setTextColor(getResources().getColor(R.color.white));
//
//                    if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
//                        daySeven_tv.setBackgroundDrawable(getResources().getDrawable(R.drawable.circle_blue));
//                    } else {
//                        daySeven_tv.setBackground(getResources().getDrawable(R.drawable.circle_blue));
//                    }
//                }
//                last_temp_date = temp_date;
//            }
//        }
//
//
//    }
//
//    private void chageSelectedColor(CustomRegularText selected, CustomRegularText child1, CustomRegularText child2, CustomRegularText child3, CustomRegularText child4, CustomRegularText child5, CustomRegularText child6) {
//        selected.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//        child1.setTextColor(getResources().getColor(R.color.calendar_sat_sun_color));
//        child2.setTextColor(getResources().getColor(R.color.calendar_sat_sun_color));
//        child3.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//        child4.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//        child5.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//        child6.setTextColor(getResources().getColor(R.color.calendar_days_day_color));
//        child1.setBackgroundResource(0);
//        child1.setBackgroundResource(0);
//        child2.setBackgroundResource(0);
//        child3.setBackgroundResource(0);
//        child4.setBackgroundResource(0);
//        child5.setBackgroundResource(0);
//        child6.setBackgroundResource(0);
//    }
//
//
//    //Get the previous week
//    public void getWeekDayPrev() {
//
//        weekDaysCount--;
//        Calendar now1 = Calendar.getInstance();
//        Calendar now = (Calendar) now1.clone();
//
//        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
//        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
//        now.add(Calendar.DAY_OF_MONTH, delta);
//        for (int i = 0; i < 7; i++) {
//
//            CalendarModal calendarModal = new CalendarModal();
//            String day = format.format(now.getTime()).substring(0, 2);
//            calendarModal.setIntDay(day);
//            detailDate(calendarModal, format.format(now.getTime()), now, i);
//        }
//        showCustomCalendar();
//        slots_ll.removeAllViews();
//        setSlots();
//
//    }
//
//
//    //GEt the next week from current week
//    public void getWeekDayNext() {
//
//
//        weekDaysCount++;
//        Calendar now1 = Calendar.getInstance();
//        Calendar now = (Calendar) now1.clone();
//
//        int delta = -now.get(GregorianCalendar.DAY_OF_WEEK) + 1;
//        now.add(Calendar.WEEK_OF_YEAR, weekDaysCount);
//        now.add(Calendar.DAY_OF_MONTH, delta);
//        for (int i = 0; i < 7; i++) {
//            CalendarModal calendarModal = new CalendarModal();
//            String day = format.format(now.getTime()).substring(0, 2);
//            calendarModal.setIntDay(day);
//            detailDate(calendarModal, format.format(now.getTime()), now, i);
//        }
//        showCustomCalendar();
//        slots_ll.removeAllViews();
//        setSlots();
//
//    }
//
//
//    //Set the week days on the row
//    private void showCustomCalendar() {
//        dayOne_tv.setText(alNext.get(0).getIntDay());
//        dayTwo_tv.setText(alNext.get(1).getIntDay());
//        dayThree_tv.setText(alNext.get(2).getIntDay());
//        dayFour_tv.setText(alNext.get(3).getIntDay());
//        dayFive_tv.setText(alNext.get(4).getIntDay());
//        daySix_tv.setText(alNext.get(5).getIntDay());
//        daySeven_tv.setText(alNext.get(6).getIntDay());
//        getSelector();
//    }
//
//
//    //Get the Full Date to show "Wed, 17 July 2017"
//    private void detailDate(CalendarModal calendarModal, String string, Calendar calendar, int pos) {
//
//        try {
//            Date date = format.parse(string);
//            String formattedDate = sdf.format(date);
//            calendarModal.setFullDate(formattedDate);
//            calendarModal.setDateString(string);
//            alNext.add(calendarModal);
//
//
//            //\ selectedDay(string);
//
//            //Here we compare position to get the current and select the box
//           /* if (format.parse(todayDate).equals(format.parse(string))) {
//                alNext.get(pos).setCurrentDate(true);
//                selectedDefaultDay();
//            }*/
//            calendar.add(Calendar.DAY_OF_MONTH, 1);
//
//        } catch (ParseException e) {
//            e.printStackTrace();
//        } finally {
//        }
//    }
//
////    void sendDataToCallingActivity(int callingActivity) {
////        InternalData internalData = new InternalData();
////        internalData.setType(isParkingFixed);
////        internalData.setPrice(parking_price);
////        internalData.setSlot_id(slot_id);
////        internalData.setSlot_status(sSlotstatus);
////        internalData.setSlot_name(parking_type);
////        Set<Map.Entry<String, ArrayList<TimeModalClass>>> setMap = hashMap.entrySet();
////        Iterator<Map.Entry<String, ArrayList<TimeModalClass>>> iteratorMap = setMap.iterator();
////        ArrayList<MyParkingTime> alTime = new ArrayList<>();
////        while (iteratorMap.hasNext()) {
////
////            Map.Entry<String, ArrayList<TimeModalClass>> entry = (Map.Entry<String, ArrayList<TimeModalClass>>) iteratorMap.next();
////
////            String key = entry.getKey();
////
////            ArrayList<TimeModalClass> values = entry.getValue();
////            MyParkingTime myParkingTime = new MyParkingTime();
////
////            myParkingTime.setDate(key);
////            myParkingTime.setArrayList(values);
////            alTime.add(myParkingTime);
////            internalData.setTimes(alTime);
////        }
////        if (hashMap.isEmpty()) {
////            Toast.makeText(this, "Please select the time.", Toast.LENGTH_SHORT).show();
////        } else {
////            Intent intent = new Intent();
////            intent.putExtra("my_list", internalData);
////            setResult(callingActivity, intent);
////            finish();
////        }
////
////
////    }
////
////    @Override
////    public void onBackPressed() {
////        super.onBackPressed();
////    }
////
//
////    public class CloneDeep implements Cloneable {
////        HashMap<String, ArrayList<TimeModalClass>> map;
////
////        public CloneDeep(HashMap<String, ArrayList<TimeModalClass>> map) {
////            this.map = map;
////        }
////
////
////        /*
////         * override clone method for doing deep copy.
////         */
////        @Override
////        public CloneDeep clone() {
////            System.out.println("Doing deep copy");
////
////            HashMap<String, ArrayList<TimeModalClass>> map = new HashMap<String, ArrayList<TimeModalClass>>();
////            Iterator<String> it = this.map.keySet().iterator();
////            while (it.hasNext()) {
////                String key = it.next();
////                map.put(key, this.map.get(key));
////            }
////
////            CloneDeep cloneDetailedDeep = new CloneDeep(map);
////
////            return cloneDetailedDeep;
////        }
////    }
//
//}
