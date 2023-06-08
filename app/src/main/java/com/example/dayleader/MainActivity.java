package com.example.dayleader;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.view.View;
import android.widget.ImageView;


import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;


import java.util.ArrayList;

import java.util.Calendar;

public class MainActivity extends AppCompatActivity {


    public TextView Engs;//영어 명언 텍스트뷰
    public TextView Kors;//한글 명언 텍스트뷰
    public TextView Whose;//명언 말한사람 이름 텍스트뷰
    public ImageView reloadButtonImageview;//버튼 이미지뷰
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

        //알림 채널 생성
        NotificationChannel();


        //Calendar에 알림받을 시간을 set
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 37);
        calendar.set(Calendar.SECOND, 00);

        //설정된 시간을 지난 경우 달력이 하루씩 증가하여 다음 날 알림 예약
        if (Calendar.getInstance().after(calendar)) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }
        //알림이 발생 시, MemoBroadcast.class에게 방송하기
        Intent intent = new Intent(MainActivity.this, MemoBroadcast.class);
        //getApplicationContext()로 context얻고 PendingIntent를 통해 방송 예약하기
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_MUTABLE);

        //AlarmManger 메소드로 device(SystemService)에 미래에 대한 알림 등록
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        //setRepeating으로 알림 반복하는데 이때 RTC_WAKEUP: 실제 시간 기준으로 대기 상태일 경우 device를 활성 상태로 전환 후, 알림 전송, INTERVAL_DAY: 하루 간격
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) { //Android M이상의 기기의 경우 저전력 모드에서도 지정된 시간에 알림 발생
            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingIntent);
        }
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

    private void NotificationChannel() { //알림 채널 생성 함수

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //알림 채널의 이름
            CharSequence name = "DAYLEADER";
            //알림 채널에 대한 설명
            String description = "DAYLEADER`S CHANNEL";
            //알림 채널의 중요도 기본 수준으로
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            //채널의 고유 ID,이름,중요도 수준으로 새 개체로 생성
            NotificationChannel channel = new NotificationChannel("Notification", name, importance);
            //알림 채널에 대한 설명 설정
            channel.setDescription(description);


            //알림 관리 위한 시스템 서비스 가져오기
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            //알림 채널 생성
            notificationManager.createNotificationChannel(channel);


        }
    }

    public int getRandomQuote(int length) {
        return (int) (Math.random() * length) + 1;
    }//0~length-1사이의 random한 숫자 생성하기

}
