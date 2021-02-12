package com.example.myapplication;

import android.app.*;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.PowerManager;
import android.util.Log;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.JobIntentService;
import androidx.core.app.NotificationCompat;
import androidx.preference.Preference;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.SortedList;

import java.io.*;
import java.util.*;

public class MyService extends Service {
    private MainActivity activity;
    private PowerManager.WakeLock wakeLock;
    private static List<Data> listdata = new ArrayList<Data>();
    private final NoticeIbinder noticeIbinder=new NoticeIbinder();




    @Override
    public void onCreate() {
        super.onCreate();
        readdata();

    }
    private void readdata(){

    }
    private void savedata(){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        acquirewakelock();
        Bundle bundle=intent.getBundleExtra("data");
        boolean notifice=intent.getBooleanExtra("notifice",false);
        if(bundle!=null) {
            if(notifice){
                Data data = (Data) bundle.getSerializable("data");
                if(activity!=null){
                    activity.save_done_data(data);
                }else{
                    DataSQl dataSQl=new DataSQl(this,"datastore.db",null,1);
                    SQLiteDatabase db = dataSQl.getWritableDatabase();
                    ContentValues values=new ContentValues();
                    values.put("checked",!data.isChecked());
                    Long globalid=data.getGlobalid();
                    Log.d("asd", String.valueOf(globalid));
                    db.update("Data",values,"globalid=?",new String[]{String.valueOf(globalid)});
                }
                listdata.remove(0);
                startService(new Intent(this,MyService.class));
                NotificationManager notificationManager=(NotificationManager)getSystemService(Context.NOTIFICATION_SERVICE);
                notificationManager.notify(data.getId()+2,sendNotification(data));
            }else {
                Data data = (Data) bundle.getSerializable("data");
                boolean find=false;
                for(int i=0;i<listdata.size();i++){
                    Data currentdata=listdata.get(i);
                    if(currentdata.getGlobalid().equals(data.getGlobalid())){
                        listdata.set(i,data);
                        find=true;
                    }
                }
                if(!find) {
                    listdata.add(data);
                }
                Collections.sort(listdata, new Comparator<Data>() {
                    @Override
                    public int compare(Data o1, Data o2) {
                        if(o1.getStartdate()<o2.getStartdate()){
                            return -1;
                        }else if(o1.getStartdate()==o2.getStartdate()){
                            return 0;
                        }else{
                            return 1;
                        }

                    }
                });
                Data nowdata=listdata.get(0);
                Date date=new Date();
                if(nowdata.getStartdate()>date.getTime()) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent noticeintent = new Intent(this, MyService.class);
                    Bundle newbundle = new Bundle();
                    newbundle.putSerializable("data", nowdata);
                    noticeintent.putExtra("data", newbundle);
                    noticeintent.putExtra("notifice", true);
                    PendingIntent pendingIntent = PendingIntent.getService(this, 0, noticeintent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nowdata.getStartdate(), pendingIntent);
                }
            }
        }else {
            startForeground(1,sendnotification());
            if(listdata.size()>0){
                Collections.sort(listdata, new Comparator<Data>() {
                    @Override
                    public int compare(Data o1, Data o2) {
                        if(o1.getStartdate()<o2.getStartdate()){
                            return -1;
                        }else if(o1.getStartdate()==o2.getStartdate()){
                            return 0;
                        }else{
                            return 1;
                        }

                    }
                });
                Data nowdata=listdata.get(0);
                Date date=new Date();
                if(nowdata.getStartdate()>date.getTime()) {
                    AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
                    Intent noticeintent = new Intent(this, MyService.class);
                    Bundle newbundle = new Bundle();
                    newbundle.putSerializable("data", nowdata);
                    noticeintent.putExtra("data", newbundle);
                    noticeintent.putExtra("notifice", true);
                    PendingIntent pendingIntent = PendingIntent.getService(this, 0, noticeintent, PendingIntent.FLAG_UPDATE_CURRENT);
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, nowdata.getStartdate(), pendingIntent);
                }
            }
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
        return noticeIbinder;
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
                .setContentIntent(pendingIntent)
                .setAutoCancel(true);


        Notification notification=builder.build();
        return notification;

    }

    public class NoticeIbinder extends Binder {
        public void pushContext(MainActivity activity){
            MyService.this.activity=activity;
        }
        public void addlistdata(List<Data> data){
            listdata=data;
        }
    }
    private void acquirewakelock(){
        if(wakeLock==null){
            PowerManager powerManager=(PowerManager)getSystemService(Context.POWER_SERVICE);
            wakeLock=powerManager.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK,"myapp:wakelock");
            if(wakeLock!=null){
                wakeLock.acquire();
            }
        }
    }
    private  void releasewakelock(){
        if(wakeLock!=null){
            wakeLock.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasewakelock();
        savedata();
    }

}
