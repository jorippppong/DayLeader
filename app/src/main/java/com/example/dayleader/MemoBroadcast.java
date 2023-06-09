package com.example.dayleader;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.Manifest;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.content.ContextCompat;


public class MemoBroadcast extends BroadcastReceiver {


    //권한 없을 때 행동 따로 지정 안 했으므로 warning없애주는 SuppressLint사용
    @SuppressLint("MissingPermission")
    @Override
    public void onReceive(Context context, Intent intent) { //알림 시간이 되었을 때 onReceive실행


            //알림 click했을 때 MainActivity.class 실행되도록 하는 intent 생성
            Intent repeating_Intent = new Intent(context, MainActivity.class);
            //단일 instance 유지 관리위해 Flag사용
            repeating_Intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

            //PendingIntent 생성
            PendingIntent pendingIntent;
            //device SDK버전에 따라 Flag 다르게 지정
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                //repeating_Intent받아 사용하는 pendingIntent로 지정
                pendingIntent = PendingIntent.getActivity(context, 0, repeating_Intent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            }else {
                pendingIntent = PendingIntent.getActivity(context, 0, repeating_Intent, PendingIntent.FLAG_UPDATE_CURRENT);
            }
            //알림에 명시될 내용 설정(R.string.notify)
            String notificationContent = context.getString(R.string.notify);

            //NotificationManager로 안드로이드 상태바에 메세지 생성 후 알림 던지기 위한 서비스 불러옴
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context, "Notification")
                    //user가 알림 클릭 시 행해지는 intent
                    .setContentIntent(pendingIntent)
                    //상태표시줄에 나오는 작은 아이콘
                    .setSmallIcon(R.drawable.fortune_clover)
                    //알림 상세보기 누르면 나오는 큰 아이콘
                    .setLargeIcon(Bitmap.createScaledBitmap(BitmapFactory.decodeResource(context.getResources(), R.drawable.fortune_clover), 128, 128, false))
                    //알림 제목
                    .setContentTitle("DAYLEADER")
                    //알림에 세부 내용
                    .setContentText(notificationContent)
                    //알림 우선순위 기본으로 설정
                    .setPriority(Notification.PRIORITY_DEFAULT)
                    //user 상호작용(ex.알림 클릭) 시 자동 취소
                    .setAutoCancel(true);


            //알림 개체 생성
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);

            //알림 Id: 200, builder.build()로 표시할 최종 객체 빌드 후 표시
            notificationManager.notify(200, builder.build());




        }



    }


