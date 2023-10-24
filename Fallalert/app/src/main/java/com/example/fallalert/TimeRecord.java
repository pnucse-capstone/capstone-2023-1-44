package com.example.fallalert;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import android.os.Bundle;
import android.os.Handler;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TimeRecord extends AppCompatActivity {

    private TextView display;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    String urlStr="http://10.0.2.2:5000/abnormal";
    Handler handler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_record);

//        display = (TextView)findViewById(R.id.fall_text);

        listView = findViewById(R.id.list);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(adapter);

        RequestThread thread = new RequestThread(); // Thread 생성
        thread.start();

    }

    class RequestThread extends Thread { // url을 읽을 때도 앱이 동작할 수 있게 하기 위해 Thread 생성
        @Override
        public void run() { // 이 쓰레드에서 실행 될 메인 코드
            try {
                URL url = new URL(urlStr); // 입력받은 웹서버 URL 저장
                HttpURLConnection conn = (HttpURLConnection) url.openConnection(); // url에 연결
                if(conn != null){ // 만약 연결이 되었을 경우
                    conn.setConnectTimeout(10000); // 10초 동안 기다린 후 응답이 없으면 종료
                    conn.setRequestMethod("GET"); // GET 메소드 : 웹 서버로 부터 리소스를 가져온다.

                    int resCode = conn.getResponseCode(); // 응답 코드를 리턴 받는다.
                    if(resCode == HttpURLConnection.HTTP_OK){ // 만약 응답 코드가 200(=OK)일 경우
                        BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                        StringBuilder sb = new StringBuilder();
                        String line = null; // 웹에서 가져올 데이터를 저장하기위한 변수
                        while ((line = br.readLine()) != null) {
                            sb.append(line);
                        }

                        JSONArray jsonArray = new JSONArray(sb.toString());

//                        // 이제 jsonArray에 있는 각 JSON 객체에 접근할 수 있습니다.
//                        final StringBuilder displayText = new StringBuilder();
//                        for (int i = 0; i < jsonArray.length(); i++) {
//                            JSONObject jsonObject = jsonArray.getJSONObject(i);
//
//                            // 여기서 필요한 작업을 수행
//                            // 예: jsonObject에서 필요한 데이터 추출
//                            int id = jsonObject.getInt("id");
//                            String name = action(jsonObject.getString("name"));
//                            String time = jsonObject.getString("time");
//
//                            // 추출한 데이터를 한 줄에 추가
//                            displayText.append("No. ").append(id)
//                                    .append("  행동: ").append(name)
//                                    .append(",  시간: ").append(time)
//                                    .append("\n");
//                        }
                        // UI 업데이트를 위해 Handler 사용
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                adapter.clear(); // 새 데이터를 추가하기 전에 어댑터를 지웁니다
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = null;
                                    try {
                                        jsonObject = jsonArray.getJSONObject(i);
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    int id = 0;
                                    try {
                                        id = jsonObject.getInt("id");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    String name = null;
                                    try {
                                        name = action(jsonObject.getString("name"));
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    String time = null;
                                    try {
                                        time = jsonObject.getString("time");
                                    } catch (JSONException e) {
                                        throw new RuntimeException(e);
                                    }

                                    adapter.add("No. " + id + "  행동: " + name + "\n           시간: " + time);
                                }
                                // 어댑터에 데이터가 변경되었음을 알리기 위해 호출
                                adapter.notifyDataSetChanged();
                            }
                        });
                    }
                    conn.disconnect(); // DB연결 해제
                }
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            } catch (JSONException ex) {
                throw new RuntimeException(ex);
            } catch (IOException ex) {
                throw new RuntimeException(ex);
            }
        }
    }

    public String action(String action){
        String act = null;

        if (action.equals("Groggy"))
            act = "비틀거림";
        else
            act = "낙상";

        return act;
    }
}