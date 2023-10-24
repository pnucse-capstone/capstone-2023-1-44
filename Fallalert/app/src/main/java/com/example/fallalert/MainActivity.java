package com.example.fallalert;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class MainActivity extends AppCompatActivity {
    Button bt;

    // 클래스 선언
    private PermissionSupport permission;
    private static final String URL_STR = "http://10.0.2.2:5000/get_data";
    private static final int INTERVAL = 5; // 10 seconds
    private ScheduledExecutorService executorService;

    public static Context context;
    public String action_num;
    boolean flag1 = false;
    boolean flag2 = false;
    boolean prev_flag1 = false;
    boolean prev_flag2 = false;

    NotificationManager manager;
    NotificationCompat.Builder builder;

    private static String CHANNEL_ID = "channel1";
    private static String CHANEL_NAME = "Channel1";

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String PhoneNum;
    private static final int PERMISSIONS_REQUEST_SEND_SMS=2323;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;

        // ViewModel 초기화

        bt = findViewById(R.id.bt);
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Movement_detect.class);
                startActivity(intent);
            }
        });

        // 권한 체크
        permissionCheck();

        // '낙상 기록' 버튼 클릭시 액티비티 전환
        Button record_move_bt = (Button) findViewById(R.id.record_move_bt);
        record_move_bt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), TimeRecord.class);
                startActivity(intent);
            }
        });

        // '낙상시 문자전송' 버튼 클릭시 액티비티 전환
        Button sms_move_bt = (Button) findViewById(R.id.sms_move_bt);
        sms_move_bt.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view){
                Intent intent = new Intent(getApplicationContext(), SmsSend.class);
                startActivity(intent);
            }
        });

        // GET data가 주기적으로 실행되도록 ScheduledExecutorService 생성
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new MainActivity.RequestTask(), 0, INTERVAL, TimeUnit.SECONDS);

    }

    // 권한 체크
    private void permissionCheck(){
        // sdk 23버전 이하 버전에서는 permission이 필요하지 않음
        if(Build.VERSION.SDK_INT >= 23){

            // 클래스 객체 생성
            permission =  new PermissionSupport(this, this);

            // 권한 체크한 후에 리턴이 false일 경우 권한 요청을 해준다.
            if(!permission.checkPermission()){
                permission.requestPermission();
            }
        }
    }

    // Request Permission에 대한 결과 값을 받는다.
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        // 리턴이 false일 경우 다시 권한 요청
        if (!permission.permissionResult(requestCode, permissions, grantResults)){
            permission.requestPermission();
        }
    }

    class RequestTask implements Runnable {
        @Override
        public void run() {
            try {
                URL url = new URL(URL_STR);
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                if (conn != null) {
                    conn.setConnectTimeout(10000);
                    conn.setRequestMethod("GET");

                    int resCode = conn.getResponseCode();
                    if (resCode == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line;
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }
                        br.close();

                        final JSONObject responseJson = new JSONObject(sb.toString());

                        // UI 업데이트를 위해 runOnUiThread 사용
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    action_num = responseJson.getString("num");
                                    System.out.println("Main: " + action_num);

                                    abnormal_alert(action_num);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                        });
                    }
                    conn.disconnect();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // 액티비티 종료 시 ScheduledExecutorService 종료
        if (executorService != null && !executorService.isShutdown()) {
            executorService.shutdown();
        }
    }


    public void SendSmS(){
        pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
        editor = pref.edit();

        PhoneNum = pref.getString("phoneNum", "_");
        String message = "낙상이 발생했습니다.";

        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {//권한이 없다면

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.SEND_SMS},
                    PERMISSIONS_REQUEST_SEND_SMS);
        }else { //권한이 있다면 SMS를 보낸다.
            PendingIntent sendIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_SENT"), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);
            PendingIntent deliveredIntent = PendingIntent.getBroadcast(this, 0, new Intent("SMS_DELIVERED"), PendingIntent.FLAG_CANCEL_CURRENT | PendingIntent.FLAG_IMMUTABLE);

            SmsManager smsManager = SmsManager.getDefault();
            try {
                smsManager.sendTextMessage(PhoneNum, null, message, sendIntent, deliveredIntent);
                Toast.makeText(getApplicationContext(), PhoneNum + "에 메시지 전송 완료", Toast.LENGTH_LONG).show();
            } catch (Exception ex) {
                ex.printStackTrace();
                Toast.makeText(getBaseContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    // 상단 알림창 알림
    public void showNoti(){
        builder = null;
        manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        //버전 오레오 이상일 경우
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            manager.createNotificationChannel(
                    new NotificationChannel(CHANNEL_ID, CHANEL_NAME, NotificationManager.IMPORTANCE_DEFAULT)
            );

            builder = new NotificationCompat.Builder(this,CHANNEL_ID);

            //하위 버전일 경우
        }else{
            builder = new NotificationCompat.Builder(this);
        }

        //알림창 제목
        builder.setContentTitle("알림");

        //알림창 메시지
        builder.setContentText("낙상감지");

        //알림창 아이콘
        builder.setSmallIcon(android.R.drawable.ic_popup_reminder);


        Notification notification = builder.build();

        //알림창 실행
        manager.notify(1,notification);
    }

    public void abnormal_alert(String action_num){
        if(action_num.equals("6")){
            flag1 = true;
            flag2 = false;
            prev_flag2=false;
        }else if(action_num.equals("7")){
            flag2 = true;
            flag1 = false;
            prev_flag1=false;
        }else{
            prev_flag1=false;
            prev_flag2=false;
            flag1 = false;
            flag2 = false;
        }

        if(flag1 && !prev_flag1){
            SendSmS();
            showNoti();
            prev_flag1=true;
        }

        if(flag2 && !prev_flag2){
            SendSmS();
            showNoti();
            prev_flag2=true;
        }
    }
}