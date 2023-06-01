package com.example.dayleader;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.content.res.Resources;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import java.util.ArrayList;



import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {


    public TextView Engs;//영어 명언 텍스트뷰 이름 지정
    public TextView Kors;//한글 명언 텍스트뷰 이름 지정
    public TextView Whose;//명언 말한사람 이름 텍스트뷰 이름 지정
    public ImageView reloadButtonImageview;//버튼 이미지뷰 이름 지정
    public ArrayList<Quote> quoteList;//명언 저장할 배열 만들기
    public int index;//해당 인덱스의 명언 가져올 것







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Engs = (TextView) findViewById(R.id.Eng_Quotes);//영어 명언 표시할 텍스트뷰 지정
        Kors = (TextView) findViewById(R.id.Kor_Quotes);//한글 명언 표시할 텍스트뷰 지정
        Whose = (TextView) findViewById(R.id.by_whom);//명언 말한 사람 이름 표시할 텍스트뷰 지정
        reloadButtonImageview = (ImageView) findViewById(R.id.reload_button);//버튼 가져오기




        Resources res = getResources();//Resource 객체 생성

        String[] allEngs = res.getStringArray(R.array.EngQuotes);//res에 저장되어 있는 영어 명언 불러올 배열 만들기
        String[] allKors = res.getStringArray(R.array.KorQuotes);//res에 저장되어 있는 한글 명언 불러올 배열 만들기
        String[] allWhose = res.getStringArray(R.array.Eng_byWhom);//res에 저장되어 있는 명언 말한 사람 이름 불러올 배열 만들기
        quoteList = new ArrayList<>();
        addToQuoteList(allEngs, allKors, allWhose);//addToQuoteList 함수를 통해 배열에 세가지 요소 저장



        final int quotesLength = quoteList.size();
        index = getRandomQuote(quotesLength - 1);//인덱스에 랜덤 숫자 저장하기
        Engs.setText(quoteList.get(index).toString());
        Kors.setText(quoteList.get(index).toString(1));
        Whose.setText(quoteList.get(index).toString("a"));
        //랜덤으로 명언 발생시키기

        reloadButtonImageview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                index = getRandomQuote(quotesLength - 1);
                Engs.setText(quoteList.get(index).toString());//영어 명언 텍스트뷰로 가져오기
                Kors.setText(quoteList.get(index).toString(1));//한글 명언 텍스트뷰로 가져오기
                Whose.setText(quoteList.get(index).toString("a"));//말한 사람 텍스트뷰로 가져오기
            }
        });
        //클릭하는 경우 새로운 명언 발생시키기


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
    }//quoteList에 모든 영어 및 한글 명언, 말한 사람 추가하는 함수

    public int getRandomQuote(int length) {
        return (int) (Math.random() * length) + 1;
    }//0~length-1사이의 random한 숫자 생성하기

}
