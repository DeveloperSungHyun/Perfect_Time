package com.example.perfect_time;

import android.app.ActivityManager;
import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.view.Window;

import androidx.annotation.RequiresApi;
import androidx.core.app.NotificationCompat;

import com.example.perfect_time.Activity.TimerSettings;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class TimerService extends Service {
    NotificationManager manager;

    NotificationCompat.Builder builder;
    private List<All_Time> all_timeList;
    private Beforehand beforehand;
    private List<Beforehand> beforehandList;

    All_Time NextTimer = null;
    Beforehand NextBeforehand = null;

    Calendar calendar;

    Thread thread;
    int count;

    OneDayTimeList oneDayTimeList;

    int y, m, d;
    int NowTime_H, NowTime_M;

    //String AllNextTimeList;
    ArrayList<String> AllNextTimeList = new ArrayList<>();
    String str_AllNextTimeList;

    public TimerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        calendar = Calendar.getInstance();

        y = calendar.get(Calendar.YEAR);
        m = calendar.get(Calendar.MONDAY) + 1;
        d = calendar.get(Calendar.DATE);

        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NowTime_M = calendar.get(Calendar.MINUTE);

        oneDayTimeList = new OneDayTimeList(this, y, m, d);//하루일정 가져오기

        all_timeList = oneDayTimeList.getTimeList();
        beforehandList = new ArrayList<>();



        int Time_h, Time_m;
        String TimerName = null;
        beforehandList.clear();

        for(All_Time item : oneDayTimeList.getTimeList()){

            if(item.isBeforehand()){
                if(item.getTime_Minute() >= item.getBeforehandTime() / 60){
                    Time_m = item.getTime_Minute() - item.getBeforehandTime() / 60;
                    Time_h = item.getTime_Hour();
                }else{
                    Time_m = (item.getTime_Minute() + 60) - item.getBeforehandTime() / 60;
                    Time_h = item.getTime_Hour() - 1;
                }

                TimerName = item.getName();

                beforehand = new Beforehand(Time_h, Time_m, TimerName);
                beforehandList.add(beforehand);
            }


        }

        beforehand = null;
        NextTimer = null;
        BackgroundServiceLogic(calendar);

        if(NextTimer != null && NextTimer.isVibration_Activate())ForeGroundService("다음 일정", NextTimer.getName(), AllNextTimeList, false);
        else ForeGroundService("알람이 없습니다.", null, null, false);

        if("start".equals(intent.getAction())){

            //ForeGroundService(NextTimer, false);

            if(thread == null){
                thread = new Thread("Service"){
                    @Override
                    public void run() {
                        while (true){
                            try {
                                count++;
                                Thread.sleep(5000);
                            }catch (InterruptedException e){
                                break;
                            }
                            calendar = Calendar.getInstance();
                            BackgroundServiceLogic(calendar);

                            Log.d("서비스 실행중", calendar.get(Calendar.HOUR_OF_DAY) + " : " + calendar.get(Calendar.MINUTE) + " : " + calendar.get(Calendar.SECOND));
                        }
                    }
                };
                thread.start();
            }

        }
        return START_STICKY;//서비스가 중지되도 다시시작

    }

    private void ForeGroundService(String Title, String Content, List<String> AllNextTimeList, boolean NotificationHead){

        Intent intent = new Intent(this, MainActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_MUTABLE | PendingIntent.FLAG_UPDATE_CURRENT);
        //builder.setContentIntent(pendingIntent);

        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if(all_timeList != null){
            if(NotificationHead){
                builder = new NotificationCompat.Builder(this, "HeadUp");
            }else{
                builder = new NotificationCompat.Builder(this, "NoneHeadUp");
            }

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
            builder.setSmallIcon(R.drawable.calendar_icon);
            builder.setTicker("알람 간단한 설명");
            builder.setContentTitle(Title);
            builder.setContentText(Content);
            if(AllNextTimeList != null){

//                for (String a : AllNextTimeList){
//                    str_AllNextTimeList += a + "\n";
//                }
                str_AllNextTimeList = AllNextTimeList.get(0);
                for (int i = 1; i < AllNextTimeList.size(); i++) {
                    str_AllNextTimeList += "\n" + AllNextTimeList.get(i);
                }
                builder.setStyle(new NotificationCompat.BigTextStyle()
                        .bigText(str_AllNextTimeList));
            }

            builder.setContentIntent(pendingIntent);
            if(NotificationHead){
                builder.setFullScreenIntent(pendingIntent, true);
            }

            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
                if(NotificationHead){
                    manager.createNotificationChannel(new NotificationChannel("HeadUp", "HeadUp", NotificationManager.IMPORTANCE_HIGH));
                }else {
                    manager.createNotificationChannel(new NotificationChannel("NoneHeadUp", "NoneHeadUp", NotificationManager.IMPORTANCE_LOW));
                }

            }

        }else{//알람 데이터가 없다면
            builder = new NotificationCompat.Builder(this, "NoneHeadUp");

            builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.drawable.calendar_icon));
            builder.setSmallIcon(R.drawable.calendar_icon);
            builder.setTicker("알람 간단한 설명");
            builder.setContentTitle("없음");
            builder.setContentText("없음");

            builder.setContentIntent(pendingIntent);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                manager.createNotificationChannel(new NotificationChannel("NoneHeadUp", "NoneHeadUp", NotificationManager.IMPORTANCE_LOW));
            }
        }

        startForeground(1, builder.build());
    }

    boolean TimerListUpData = false;
    private void BackgroundServiceLogic(Calendar calendar){

        NowTime_H = calendar.get(Calendar.HOUR_OF_DAY);
        NowTime_M = calendar.get(Calendar.MINUTE);

        if(NowTime_H == 0 && NowTime_M > 0 && TimerListUpData == true){

            y = calendar.get(Calendar.YEAR);
            m = calendar.get(Calendar.MONDAY) + 1;
            d = calendar.get(Calendar.DATE);

            oneDayTimeList = new OneDayTimeList(this, y, m, d);//하루일정 가져오기

            all_timeList = oneDayTimeList.getTimeList();


            TimerListUpData = false;
        } else {
            TimerListUpData = true;
        }


        //============================================================알람 발생코드
        if(all_timeList != null){
            if(NextTimer == null){
                AllNextTimeList.clear();
                for (int i = 0; i < all_timeList.size(); i++) {
                    if(all_timeList.get(i).isVibration_Activate()){
                        if(all_timeList.get(i).getTime_Hour() > NowTime_H ||
                                (all_timeList.get(i).getTime_Hour() == NowTime_H && all_timeList.get(i).getTime_Minute() > NowTime_M)){

                            if(NextTimer == null)NextTimer = all_timeList.get(i);//다음 알림
                            //AllNextTimeList += all_timeList.get(i) + "\n";
                            AllNextTimeList.add((i + 1) + ". " + all_timeList.get(i).getName() + " (" + all_timeList.get(i).getTime_Hour() + "시 " + all_timeList.get(i).getTime_Minute() + "분)");
                        }else{//다음 알림이 없을때
                            //ForeGroundService("오늘 일정", "일정이 없습니다.", null);
                            //Log.d("ForeGroundService", "알림내용 없음1");
                        }
                    }

                }


            }else if(NextTimer.getTime_Hour() == NowTime_H && NextTimer.getTime_Minute() == NowTime_M){//알람시간이 됬을경우
                if(NextTimer != null){
                    AllNextTimeList.clear();

                    AllNextTimeList.add(NextTimer.getMemo());
                    ForeGroundService(NextTimer.getName(), NextTimer.getMemo(), AllNextTimeList, false);
                    NextTimer = null;
                }

            }

        }else{
            ForeGroundService("오늘 일정", "일정이 없습니다.", null, false);
        }
        //=========================================================================

        //=============================================================예고 발생코드
        if(beforehandList != null) {

            if(beforehand == null) {
                Log.d("==================1", "");
                for (int i = 0; i < beforehandList.size(); i++) {
                    Log.d("==================2", "");
                    if (beforehandList.get(i).Time_h > NowTime_H ||
                            (beforehandList.get(i).Time_h == NowTime_H && beforehandList.get(i).Time_m > NowTime_M)) {

                        beforehand = beforehandList.get(i);//다음 알림
                        Log.d("beforehand", "=============" + beforehand.TimerName);
                        break;
                    }
                }
            }else if(beforehand.Time_h == NowTime_H && beforehand.Time_m == NowTime_M){//알람시간이 됬을경우
                if(beforehand != null){
                    ForeGroundService(beforehand.TimerName, "곧 알람이 울립니다.", null, true);
                    beforehand = null;
                }

            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        Log.d("서비스 중지", "OnDestroy");

        if(thread != null){
            thread.interrupt();
            thread = null;
            count = 0;
        }
    }


    public static boolean isServiceRunning(Context context) {
        ActivityManager am = (ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE);

        for (ActivityManager.RunningServiceInfo rsi : am.getRunningServices(Integer.MAX_VALUE)) {
            if (TimerService.class.getName().equals(rsi.service.getClassName())) //[서비스이름]에 본인 것을 넣는다.
            return true;
        }

        return false;
    }

    public class Beforehand{

        int Time_h, Time_m;
        String TimerName = null;
        public Beforehand(int Time_h, int Time_m, String TimerName){
            this.Time_h = Time_h;
            this.Time_m = Time_m;
            this.TimerName = TimerName;

        }
    }
}