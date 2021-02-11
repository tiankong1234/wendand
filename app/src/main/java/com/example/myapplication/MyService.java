package com.example.myapplication;

import android.app.*;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;

public class MyService extends Service {
    public MyService() {
    }



    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Bundle bundle=intent.getBundleExtra("data");
        boolean notifice=intent.getBooleanExtra("notifice",false);
        if(bundle!=null) {
            if(notifice){
                Data data = (Data) bundle.getSerializable("data");
                startForeground(1,sendNotification(data));
            }else {
                Data data = (Data) bundle.getSerializable("data");
                AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                Intent noticeintent = new Intent(this, MyService.class);
                Bundle newbundle = new Bundle();
                bundle.putSerializable("data", data);
                noticeintent.putExtra("data", bundle);
                noticeintent.putExtra("notifice", true);
                PendingIntent pendingIntent = PendingIntent.getService(this, 0, noticeintent, PendingIntent.FLAG_UPDATE_CURRENT);
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, data.getStartdate(), pendingIntent);
            }
        }else {
            startForeground(1,sendnotification());
        }
        return super.onStartCommand(intent, flags, startId);
    }
    private Notification sendnotification(){
        String channelId = "通知渠道";
        String channelName = "通知渠道";
        String channelDescription = "通知渠道";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_NONE);

            channel.enableLights(true); //设置开启指示灯，如果设备有的话
            channel.setLightColor(Color.RED); //设置指示灯颜色
            channel.setShowBadge(true); //设置是否显示角标
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);//设置是否应在锁定屏幕上显示此频道的通知
            channel.setDescription(channelDescription);//设置渠道描述
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 600});//设置震动频率
            channel.setBypassDnd(true);//设置是否绕过免打扰模式
            notificationManager.createNotificationChannel(channel);
           

        }
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle("Message")
                .setSmallIcon(R.drawable.ic_done)
                .setContentText("This notice makes your app works well!")
                .setWhen(System.currentTimeMillis());
        builder.setSmallIcon(R.drawable.ic_done);
        Notification notification=builder.build();
        return notification;

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

   

    private Notification sendNotification(Data data){
        String channelId = "通知渠道ID";
        String channelName = "通知渠道名称";
        String channelDescription = "通知渠道描述";
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(channelId,
                    channelName, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableLights(true); //设置开启指示灯，如果设备有的话
            channel.setLightColor(Color.RED); //设置指示灯颜色
            channel.setShowBadge(true); //设置是否显示角标
            channel.setLockscreenVisibility(Notification.VISIBILITY_PRIVATE);//设置是否应在锁定屏幕上显示此频道的通知
            channel.setDescription(channelDescription);//设置渠道描述
            channel.setVibrationPattern(new long[]{100, 200, 300, 400, 500, 600});//设置震动频率
            channel.setBypassDnd(true);//设置是否绕过免打扰模式
            notificationManager.createNotificationChannel(channel);

        }
        Intent intent=new Intent(this,NoticeActivity.class);
        Bundle bundle=new Bundle();
        bundle.putSerializable("data",data);
        intent.putExtras(bundle);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent=PendingIntent.getActivity(this,0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder;
        builder = new NotificationCompat.Builder(this, channelId);
        builder.setContentTitle(data.getTitle())
                .setSmallIcon(R.drawable.ic_done)
                .setContentText(data.getContent())
                .setWhen(System.currentTimeMillis())
                .setContentIntent(pendingIntent);


        Notification notification=builder.build();
        return notification;

    }
}
