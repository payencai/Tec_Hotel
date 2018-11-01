package com.tec.hotel_com.JPush;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;


import com.tec.hotel_com.App;
import com.tec.hotel_com.R;
import com.tec.hotel_com.common.activity.LoginActivity;
import com.tec.hotel_com.common.activity.MainActivity;
import com.tec.hotel_com.common.activity.NotifyActivity;
import com.tec.hotel_com.utils.NotifyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Method;

import cn.jpush.android.api.BasicPushNotificationBuilder;
import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

public class JpushPresenter implements JpushContract {
    private Context mContext;
    public static final String TAG = "JpushPresenter";

    public JpushPresenter() {
    }

    public void setContext(Context context) {
        this.mContext = context;
    }

    /**
     * 接收到自定义的消息，调用自定义的通知显示出来
     */

    @Override
    public void doProcessPushMessage(Bundle bundle) {
        String alert = bundle.getString(JPushInterface.EXTRA_EXTRA);  //扩展消息
        Log.e("fff",alert);
        String title = "新旅盟酒店";
        String content = "";
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        try {
            JSONObject jsonObject = new JSONObject(extra);
            JSONObject data=jsonObject.getJSONObject("Result");
            String sound=data.getString("sound");
            content=data.getString("content");
            showNotification(mContext,title,content,sound,bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

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

    /**
     * 发送通知
     *
     * @param bundle
     */
    @Override
    public void doProcessPusNotify(Bundle bundle) {
        String alert = bundle.getString(JPushInterface.EXTRA_EXTRA);  //扩展消息
        Log.e("alert", alert);
        String title = "新旅盟酒店";
        String content = "";
        String extra = bundle.getString(JPushInterface.EXTRA_EXTRA);
        try {
            JSONObject jsonObject = new JSONObject(extra);
            JSONObject data=jsonObject.getJSONObject("Result");
            String sound=data.getString("sound");
            content=data.getString("content");
            showNotification(mContext,title,content,sound,bundle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //showNotify(bundle);
    }

//    private boolean processCustomMessage(Context context, Bundle bundle) throws JSONException {
//        JSONObject json = null;
//        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);
//        //这一步必须要有而且setSmallIcon也必须要，没有就会设置自定义声音不成功
//        notification.setAutoCancel(true).setSmallIcon(R.drawable.app_logo);
//        /**
//         *  这个地方特殊说明一下
//         *  因为我们发出推送大部分是分为两种，一种是在极光官网直接发送推送，第二是根据后台定义
//         * 的NOTIFICATION发送通知，所以在这里你要根据不同的方式获取不同的内容
//         **/
//        //如果是使用后台定的NOTIFICATION发送推送，那么使用下面的方式接收消息
//        String alert = bundle.getString(JPushInterface.EXTRA_EXTRA);  //扩展消息
//        String title = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);  //推送标题
//        String msg = bundle.getString(JPushInterface.EXTRA_ALERT);   //推送消息
//        Log.e("msg", alert);
//        notification.setSound(
//                Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.jiedan));
//        //这是点击通知栏做的对应跳转操作，可以根据自己需求定义
//        Intent mIntent = new Intent(context, MainActivity.class);
//        //mIntent.putExtra("msgId", json.getString("msgId"));
//        mIntent.putExtras(bundle);
//        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);
//
//        notification.setContentIntent(pendingIntent)
//                .setAutoCancel(true)
//                .setContentText(msg)
//                .setContentTitle(title.equals("") ? "title" : title)
//                .setSmallIcon(R.drawable.app_logo);
//        //获取推送id
//        int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
//        //bundle.get(JPushInterface.EXTRA_ALERT);推送内容
//        //最后刷新notification是必须的
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
//        notificationManager.notify(notifactionId, notification.build()); //在此处放入推送id —notifactionId
//        return true;
//    }

    public void showMsg(Bundle bundle) {
        Notification.Builder builder = new Notification.Builder(mContext);
        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        NotifyUtil.showNotify(mContext, message, extras);
    }

    /**
     * 使用Jpush内置的样式构建通知
     */

    public void showNotify(Bundle bundle) {
        int i = 0;
        Log.e("not", ++i + "");
        BasicPushNotificationBuilder builder = new BasicPushNotificationBuilder(mContext);
        builder.statusBarDrawable = R.drawable.app_logo;

        builder.notificationFlags = Notification.FLAG_AUTO_CANCEL
                | Notification.FLAG_SHOW_LIGHTS;  //设置为自动消失和呼吸灯闪烁
        builder.notificationDefaults = Notification.DEFAULT_SOUND
                | Notification.DEFAULT_VIBRATE
                | Notification.DEFAULT_LIGHTS;  // 设置为铃声、震动、呼吸灯闪烁都要
        JPushInterface.setPushNotificationBuilder(0, builder);
        //LogUtil.e(TAG,"=====doProcessPusNotify=======");
    }

    /**
     * 点击通知之后的操作在这里
     *
     * @param bundle
     */
    @Override
    public void doOpenPusNotify(Bundle bundle) {

        if (App.isLogin) {
            //打开自定义的Activity
            Intent i = new Intent(mContext, NotifyActivity.class);
            i.putExtras(bundle);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mContext.startActivity(i);
        } else {
            Log.d(TAG, "[MyReceiver] 用户点击打开了通知");
            Intent i = new Intent(mContext, LoginActivity.class);
            i.putExtras(bundle);
            i.putExtra("name", "notify");
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }

        //mContext.sendBroadcast(new Intent(mContext, NotifyActivity.class));
        //LogUtil.e(TAG,"=====doOpenPusNotify=======");
    }


}
