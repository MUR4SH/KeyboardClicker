package com.example.keyboardclicker;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.StrictMode;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;


public class ScoresActivity extends AppCompatActivity  {
    static String server_url="http://192.168.117.17:8080";
    public void onCreate(Bundle savedInstanceState){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.scores_activity);

        OptionsActivity.SetLang(this);
        Button btn_return = (Button)findViewById(R.id.btn_back);
        LinearLayout ll = (LinearLayout)findViewById(R.id.chat_window);
        ll.setPadding(10,10,10,10);
        ll.setBackgroundResource(R.drawable.button_white_border);

        String arr[] = OptionsActivity.GetSettings(this);
        String lang = arr[0];
        String username = arr[1];

        String scores[] = GetScores(this);
        for(int i=0;i<scores.length;i++){
           // if(!scores[i].equals("")){
                ll.addView(FillScores(this,scores[i]));
           // }
        }

        btn_return.setText(lang.equals("ru")?R.string.back_ru:R.string.back_eng);

        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(ScoresActivity.this,MainActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });
    }
    public void onBackPressed(){
        //super.onBackPressed();
        try{
            Intent intent = new Intent(ScoresActivity.this,MainActivity.class);
            startActivity(intent);finish();
        }catch(Exception e){

        }
    }

    public static String[] GetScores(Context ctx) {
        String[] arr;
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(Request("{\"type\": \"get_all\",\"data\": {\"user_id\":\"\",\"username\":\"\",\"time\":\"\"}}",server_url)));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            arr = content.toString().split("\n");
            return arr;
        }catch(Exception err){
            return new String[1];
        }
    }

    public static void SetScore(Context ctx,String name){
        try {
            Request("{\"type\": \"set\",\"data\": {\"user_id\":\"\",\"username\":\"" + OptionsActivity.GetSettings(ctx)[1] + "\",\"time\":\"" + game_activity.CountTime() + "\"}}",server_url);
        }catch(Exception err){
            err=err;
        }
    }

    private static InputStream Request(String data, String urls){
        final InputStream[] arr = new InputStream[1];
        try{
            Thread thread1 = new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        URL url = null;
                        HttpURLConnection http = null;
                        try {
                            url = new URL(urls);
                            http = (HttpURLConnection)url.openConnection();
                            http.setRequestMethod("POST");
                        } catch (Exception err) {
                        }

                        http.setDoOutput(true);
                        http.setRequestProperty("Content-Type", "text/plain");

                        http.setConnectTimeout(2000);
                        http.setReadTimeout(2000);

                        byte[] out = data.getBytes(StandardCharsets.UTF_8);
                        OutputStream stream = http.getOutputStream();
                        stream.write(out);
                        stream.close();

                        arr[0] = http.getInputStream();
                        http.disconnect();
                    }catch (Exception er){
                        er =er;
                    }
                }
            });
            thread1.start();
            thread1.run();
            thread1.interrupt();
            return arr[0];
        }catch(Exception err){
            return arr[0];
        }
    }

    public static String GetUsersScoreOffline(Context ctx){
        String arr="";
        try{
            BufferedReader in = new BufferedReader(new InputStreamReader(Request("{\"type\": \"get_all\",\"data\": {\"user_id\":\"\",\"username\":\""+OptionsActivity.GetSettings(ctx)[1]+"\",\"time\":\"\"}}",server_url)));
            String inputLine;
            StringBuffer content = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                content.append(inputLine);
            }
            arr = content.toString();
            return arr;
        }catch(Exception err){
            return arr;
        }
    }

    public TextView FillScores(Context ctx,String text){
        TextView score = new TextView(ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,0);

        score.setLayoutParams(params);
        score.setText(text);

        return score;
    }
}
