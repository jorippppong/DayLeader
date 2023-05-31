package com.example.dayleader;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    public TextView Engs;//영어 명언 불러올것
    public TextView Kors;//한글 명언 불러올것
    public TextView Whose;//명언 말한사람 이름 불러올것
    public ArrayList<Quote> quoteList;
    public int index;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Resources res = getResources();  //여기 에러 발생

        Engs = (TextView) findViewById(R.id.Eng_Quotes);//텍스트 표시할 텍스트뷰 지정
        Kors = (TextView) findViewById(R.id.Kor_Quotes);
        Whose = (TextView) findViewById(R.id.by_whom);

        String[] allEngs = res.getStringArray(R.array.EngQuotes);//res에 저장되어 있는 영어 명언 불러올 배열 만들기
        String[] allKors = res.getStringArray(R.array.KorQuotes);
        String[] allWhose = res.getStringArray(R.array.Eng_byWhom);
        quoteList = new ArrayList<>();
        addToQuoteList(allEngs, allKors, allWhose);

        final int quotesLength = quoteList.size();
        index = getRandomQuote(quotesLength - 1);
        Engs.setText(quoteList.get(index).toString());


        //=================//
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
                //String selectedDate = year + "-" + month + "-" + dayOfMonth;
                //Log.d("mainActivity", "calendar selectedDate: " + selectedDate);

                //정보 가지고 다음 Activity 로 이동
                Intent intent = new Intent(MainActivity.this, TodoActivity.class);
                intent.putExtra("year", year);
                intent.putExtra("month", month);
                intent.putExtra("day", dayOfMonth);
                startActivity(intent);
            }
        });
    }

    public void addToQuoteList(String[] allEngs, String[] allKors, String[] allWhose) {
        for(int i = 0; i < allEngs.length; i++) {
            String engs = allEngs[i];
            String kors = allKors[i];
            String author = allWhose[i];
            Quote quote = new Quote(engs, kors, author);
            quoteList.add(quote);
        }
    }//quoteList에 모든 영어 및 한글 명언, 말한 사람 추가하기

    public int getRandomQuote(int length) {
        return (int) (Math.random() * length) + 1;
    }//0~length-1사이의 random한 숫자 생성하기

}
