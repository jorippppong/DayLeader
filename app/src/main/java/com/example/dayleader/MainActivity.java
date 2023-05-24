package com.example.dayleader;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MaterialCalendarView calendarView= findViewById(R.id.calendarView);
        calendarView.getSelectedDate();

        //캘린더
        //calendarView.setOnDateChangedListener( {
//            @Override
//            public void onSelectedDayChange(@NonNull CalendarView calendarView, int year, int month, int day) {
//                int formatYear = year;
//                int formatMonth = month+1;
//                int formatDay = day;
//
//                Log.d("main", Integer.toString(formatYear)+Integer.toString(formatMonth)+Integer.toString(formatDay));
//            }
        //});

    }
}
