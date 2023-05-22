package com.example.dayleader;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TextView;


import org.w3c.dom.Document;

import java.io.IOException;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    public TextView Engs;//영어 명언 불러올것
    public TextView Kors;//한글 명언 불러올것
    public TextView Whose;//명언 말한사람 이름 불러올것


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender_sayings);//이 부분 원래 activity_main이었음

        Engs = (TextView) findViewById(R.id.Eng_Quotes);
        Kors = (TextView) findViewById(R.id.Kor_Quotes);
        Whose = (TextView) findViewById(R.id.by_whom);

        Resources res = getResources();
        String[] allEngs = res.getStringArray(R.array.engquotes);
        String[] allKors = res.getStringArray(R.array.KorQuotes);
        String[] allWhose = res.getStringArray(R.array.Eng_byWhom);


    }


}