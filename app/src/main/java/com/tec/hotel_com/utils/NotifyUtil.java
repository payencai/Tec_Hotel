package com.tec.hotel_com.utils;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.activity.MainActivity;
import com.tec.hotel_com.common.activity.NotifyActivity;


public class NotifyUtil {

    public static final String PRIMARY_CHANNEL = "default";
    public static final int pushId = 1;
    public static final void showNotify(Context context,String title,String content){
        NotificationManager mNotificationManager = (NotificationManager) context.getSystemService(context.NOTIFICATION_SERVICE);
        NotificationCompat.Builder mBuilder;
        //判断是否是8.0Android.O
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel chan1 = new NotificationChannel(PRIMARY_CHANNEL,
                    "Primary Channel", NotificationManager.IMPORTANCE_DEFAULT);
            chan1.setLightColor(Color.GREEN);
            chan1.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);
            mNotificationManager.createNotificationChannel(chan1);
            mBuilder = new NotificationCompat.Builder(context, PRIMARY_CHANNEL);
        } else {
            mBuilder = new NotificationCompat.Builder(context);
        }
        Intent notificationIntent = new Intent(context, NotifyActivity.class);
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        PendingIntent intent = PendingIntent.getActivity(context, 0,
                notificationIntent, 0);
        mBuilder.setContentTitle(title)//设置通知栏标题
                .setContentText(content)//设置通知栏内容
                .setContentIntent(intent) //设置通知栏点击意图
//                .setNumber(++pushNum) //设置通知集合的数量
                .setTicker(title + ":" + content) //通知首次出现在通知栏，带上升动画效果的
                .setWhen(System.currentTimeMillis())//通知产生的时间，会在通知信息里显示，一般是系统获取到的时间
                .setDefaults(Notification.DEFAULT_ALL)//向通知添加声音、闪灯和振动效果的最简单、最一致的方式是使用当前的用户默认设置，使用defaults属性，可以组合
                .setSmallIcon(R.drawable.app_logo);//设置通知小ICON
        Notification notify = mBuilder.build();
        notify.flags |= Notification.FLAG_AUTO_CANCEL;
        notify.sound= Uri.parse("android.resource://" +context.getPackageName()+ "/" +R.raw.s1);
        mNotificationManager.notify(pushId, notify);

    }
}
