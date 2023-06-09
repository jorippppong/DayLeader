package com.example.dayleader;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class FortuneActivity extends AppCompatActivity {

    ImageButton fortuneCookieButton;//변수 선언
    TextView messageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortune);


        fortuneCookieButton = findViewById(R.id.fortuneCookieButton); //포춘 쿠키 버튼 불러 오기
        messageView = findViewById(R.id.show_fortune_message);
        fortuneCookieButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFortune(); //클릭 시 함수 실행
            }
        });
    }

    private void showFortune() {
        String[] fortunes;
        if (Locale.getDefault().getLanguage().equals("ko")) {
            fortunes = getResources().getStringArray(R.array.fortunes_ko);
        } else {
            fortunes = getResources().getStringArray(R.array.fortunes_default);
        }//언어에 따른 출력되는 운세 메시지를 변경하는 함수

        Random random = new Random(); //랜덤 설정
        int index = random.nextInt(fortunes.length);
        String fortune = fortunes[index];

        messageView.setText(fortune);
        fortuneCookieButton.setImageResource(R.drawable.fortune_cookie_message_image);//사진도 변경 하는 명령어
    }
}