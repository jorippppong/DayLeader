package com.example.dayleader;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

public class fortune_activity extends AppCompatActivity {

    ImageButton fortuneCookieButton;//변수 선언
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fortune);


        fortuneCookieButton = findViewById(R.id.fortuneCookieButton); //포춘 쿠키 버튼 불러 오기
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
        }

        Random random = new Random(); //랜덤 설정
        int index = random.nextInt(fortunes.length);
        String fortune = fortunes[index];

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_fortune, null);
        builder.setView(dialogView);
        builder.setMessage(fortune)
                .setPositiveButton("확인", null)
                .create()
                .show(); //메시지 창이 뜨는 함수

        ImageView imageView = dialogView.findViewById(R.id.fortuneView);
        imageView.setImageResource(R.drawable.fortune_cookie_message_image);//운세 메시지와 함께 사진도 출력 하는 명령어
    }
}