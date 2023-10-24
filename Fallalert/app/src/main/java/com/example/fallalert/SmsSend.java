package com.example.fallalert;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.os.Bundle;
import android.widget.Toast;

public class SmsSend extends AppCompatActivity {

    SharedPreferences pref;
    SharedPreferences.Editor editor;

    String PhoneNum;

    private static final int PERMISSIONS_REQUEST_SEND_SMS=2323;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sms_send);

        EditText inputPhoneNum = findViewById(R.id.input_phone_num);
        Button sendSMSBt = findViewById(R.id.send_sms_button);

        sendSMSBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strPhoneNumber = inputPhoneNum.getText().toString().replaceAll("[^0-9]", "");
                if(strPhoneNumber.equals("") || strPhoneNumber.length() < 10){
                    Toast.makeText(getApplicationContext(),"올바르지 않은 전화번호입니다.",Toast.LENGTH_LONG).show();
                    return;

                }
                pref = getSharedPreferences("pref", Activity.MODE_PRIVATE);
                editor = pref.edit();

                editor.putString("phoneNum", strPhoneNumber);
                editor.apply();

                PhoneNum = pref.getString("phoneNum", "_");
//                SmsSend(strPhoneNumber, "낙상이 발생했습니다.");
//                SendSmS();
                Toast.makeText(getApplicationContext(),PhoneNum + " 전화번호가 등록되었습니다.",Toast.LENGTH_LONG).show();
            }
        });

    }


//    private void SmsSend(String phoneNumber, String message){
////        SmsManager sms = SmsManager.getDefault();
////        sms.sendTextMessage(phoneNumber, null, message, null, null);
//    }
}