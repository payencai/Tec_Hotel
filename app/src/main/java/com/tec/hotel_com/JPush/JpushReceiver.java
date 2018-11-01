package com.tec.hotel_com.JPush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.tec.hotel_com.R;
import com.tec.hotel_com.common.activity.MainActivity;
import com.tec.hotel_com.common.activity.NotifyActivity;


import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import cn.jpush.android.api.JPushInterface;

public class JpushReceiver extends BroadcastReceiver{
    public static final String TAG="JpushReceiver";
    private JpushPresenter jpushPresenter = null;
    private Context mContext;
    @Override
    public void onReceive(Context context, Intent intent) {
        this.mContext=context;
        Bundle bundle = intent.getExtras();
        if (bundle == null) {
            return;
        }
        if (jpushPresenter == null) {
            jpushPresenter = new JpushPresenter();
        }

        jpushPresenter.setContext(context);
        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            // SDK 向 JPush Server 注册所得到的注册 全局唯一的 ID
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.e("regid",regId);

            //send the Registration Id to your server...
        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
           // Log.e(TAG, "-推送消息: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
//            String alert = bundle.getString(JPushInterface.EXTRA_EXTRA);  //扩展消息
//            Log.e("fff",alert+"");
//            String title = "新旅盟酒店";
//            String content = "fdgg";
//            showNotification(mContext,title,content,"2.mp3",bundle);
//            String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
//            try {
//                JSONObject jsonObject = new JSONObject(extra);
//                JSONObject data=jsonObject.getJSONObject("Result");
//                String sound=data.getString("sound");
//                content=data.getString("content");

//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
            jpushPresenter.doProcessPushMessage(bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.e(TAG, "-推送通知: " + bundle.getString(JPushInterface.EXTRA_EXTRA));

            jpushPresenter.doProcessPusNotify(bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.e(TAG, "-点击推送的通知: " + bundle.getString(JPushInterface.EXTRA_ALERT));
            jpushPresenter.doOpenPusNotify(bundle);
        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.e(TAG, "-点击推送的callback: " + bundle.getString(JPushInterface.EXTRA_ALERT));
            mContext.startActivity(new Intent(mContext, NotifyActivity.class));
            //LogUtil.e(TAG,"-用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..
        } else if (JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            //Log.e( "-" + intent.getAction() + " connected state change to " + connected);
        } else {
            //LogUtil.e(TAG, "-Unhandled intent - " + intent.getAction());
        }
    }
//
//    作者：奔跑的佩恩
//    链接：https://www.jianshu.com/p/1b1dd62b2d13
//    來源：简书
//    简书著作权归作者所有，任何形式的转载都请联系作者获得授权并注明出处。
public static void showNotification(Context context, String title, String content, String sound, Bundle bundle) {
    Notification.Builder builder = new Notification.Builder(context);
    builder.setContentTitle(title);
    builder.setContentText(content);
    builder.setSmallIcon(R.drawable.app_logo);
    builder.setWhen(System.currentTimeMillis());
    builder.setAutoCancel(true);
    builder.setDefaults(NotificationCompat.DEFAULT_VIBRATE | NotificationCompat.DEFAULT_LIGHTS);
    Notification notification = null;
    try {
        Method method = Notification.Builder.class
                .getDeclaredMethod("build");
        if (method != null) {
            notification = builder.build();
        } else {
            notification = builder.getNotification();
        }
    } catch (NoSuchMethodException e) {
        notification = builder.getNotification();
    }
    NotificationManager notificationManager = (NotificationManager) context
            .getSystemService(Context.NOTIFICATION_SERVICE);

    Intent openedIntent = new Intent(JPushInterface.ACTION_NOTIFICATION_OPENED);
    openedIntent.putExtras(bundle);
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, openedIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    notification.contentIntent = pendingIntent;
    String pkgPath = "android.resource://" + context.getPackageName() + "/";

    if("1.mp3".equals(sound)){
        notification.sound = Uri.parse(pkgPath + R.raw.jiedan);
    }
    if("2.mp3".equals(sound)){
        notification.sound = Uri.parse(pkgPath + R.raw.unhandle);
    }
    if("3.mp3".equals(sound)){
        notification.sound = Uri.parse(pkgPath + R.raw.s1);
    }
    notification.flags |= Notification.FLAG_AUTO_CANCEL;
    notificationManager.notify(0, notification);

}

}
