package com.example.fallalert;

import android.graphics.Color;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Dimension;
import androidx.appcompat.app.AppCompatActivity;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Movement_detect extends AppCompatActivity {

    private static final int INTERVAL = 3; // 10 seconds
    private TextView textView;
    private ImageView imageView;
    private ScheduledExecutorService executorService;
    String action_num;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movement_detect);

        textView = findViewById(R.id.action_name);
        textView.setTextColor(Color.parseColor("#000000"));
        textView.setTextSize(Dimension.SP, 30);
        textView.setBackgroundColor(Color.parseColor("#FFFFFF"));

        imageView = findViewById(R.id.act_image);

        // 주기적으로 실행되도록 ScheduledExecutorService 생성
        executorService = Executors.newSingleThreadScheduledExecutor();
        executorService.scheduleAtFixedRate(new RequestTask(), 0, INTERVAL, TimeUnit.SECONDS);
    }

    class RequestTask implements Runnable {
        @Override
        public void run() {
            try {
                action_num = ((MainActivity)MainActivity.context).action_num;
                textView.setText(action_explain(action_num));

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

    public String action_explain(String action){
        String explain = null;

        if (action.equals("0")){
            explain = "누워있습니다.";
            imageView.setImageResource(R.drawable.lying);
        }
        else if (action.equals("1")){
            explain = "달리고 있습니다.";
            imageView.setImageResource(R.drawable.running);
        }
        else if (action.equals("2")){
            explain = "의자에 앉아있습니다.";
            imageView.setImageResource(R.drawable.sitchair);
        }
        else if (action.equals("3")){
            explain = "바닥에 앉아있습니다.";
            imageView.setImageResource(R.drawable.sitfloor);
        }
        else if (action.equals("4")){
            explain = "서 있습니다.";
            imageView.setImageResource(R.drawable.standing);
        }
        else if (action.equals("5")){
            explain = "걷고 있습니다.";
            imageView.setImageResource(R.drawable.walking);
        }
        else if (action.equals("6")){
            explain = "비틀거리고 있습니다.";
            imageView.setImageResource(R.drawable.groggy);
        }
        else if (action.equals("7")){
            explain = "낙상이 발생하였습니다.";
            imageView.setImageResource(R.drawable.fall);
        }
        else
            explain = "오류";

        return explain;
    }

}
