package com.example.dayleader;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //캘린더 변수 설정
        MaterialCalendarView calendarView= findViewById(R.id.calendarView);

        //[캘린더] 현재 날짜로 설정
        calendarView.setSelectedDate(CalendarDay.today());

        //[캘린더] 선택한 날짜 가져 오기
        calendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                // 선택된 날짜 정보 가져 오기
                int year = date.getYear();
                int month = date.getMonth();
                int dayOfMonth = date.getDay();

                // 선택된 날짜 정보 출력
                String selectedDate = year + "-" + month + "-" + dayOfMonth;
                Log.d("mainActivity", "calendar selectedDate: " + selectedDate);

                //정보 가지고 다음 Activity 로 이동
                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("dateInfo", selectedDate);
                startActivity(intent);
            }
        });

    }
}
