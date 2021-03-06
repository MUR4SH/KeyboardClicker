package com.example.keyboardclicker;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

class MenuMessage extends MainActivity {
    private String nickname;
    private String message;
    public MenuMessage(Context ctx){

        Random r = new Random();
        //String names[] = getResources().getStringArray(R.array.names_array);
        this.nickname = ctx.getResources().getStringArray(R.array.names_array)[r.nextInt(20)];
        if (OptionsActivity.GetSettings(ctx)[0].equals("ru")) {
            //String phrases[] = getResources().getStringArray(R.array.phrases_ru_arr);
            this.message =  ctx.getResources().getStringArray(R.array.phrases_ru_arr)[r.nextInt(20)];
        }else{
            //String phrases[] = getResources().getStringArray(R.array.phrases_eng_arr);
            this.message =  ctx.getResources().getStringArray(R.array.phrases_eng_arr)[r.nextInt(20)];
        }
    }

    public String GetNick(){
        return this.nickname;
    }

    public String GetMessage(){
        return this.message;
    }
}

public class MainActivity extends AppCompatActivity {
    Handler hndlr = new Handler();
    int counter = 25;
    int timer = 3000; //ms
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        this.SetAppLocale(this);
        Context ctx = this;
        //this.SpamMessages(counter,timer);
        CustomScrollView myScrollView = (CustomScrollView) findViewById(R.id.scroll_view);
        myScrollView.setEnableScrolling(false); // disable scrolling

        Button btn_start = (Button)findViewById(R.id.btn_start);
        Button btn_options = (Button)findViewById(R.id.btn_options);
        Button btn_scores = (Button)findViewById(R.id.btn_scores);
        Button btn_about = (Button)findViewById(R.id.about);

        btn_start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(MainActivity.this,game_activity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });
        btn_options.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(MainActivity.this,OptionsActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });
        btn_scores.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(MainActivity.this,ScoresActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });
        btn_about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowAbout(ctx);
            }
        });
        /*Window w = getWindow();
        w.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);*/
    }
    public void onPause() {
        super.onPause();
        hndlr.removeCallbacksAndMessages(null);//stop msgs spam
    }
    public void onResume() {
        super.onResume();
        SpamMessages(counter,timer);
    }
    public void SetAppLocale(Context ctx){
        Button btn_start = (Button)findViewById(R.id.btn_start);
        Button btn_options = (Button)findViewById(R.id.btn_options);
        Button btn_scores = (Button)findViewById(R.id.btn_scores);
        Button btn_about = (Button)findViewById(R.id.about);

        if (OptionsActivity.GetSettings(ctx)[0].equals("ru")) {
            //String phrases[] = getResources().getStringArray(R.array.phrases_ru_arr);
            btn_start.setText(R.string.start_button_ru);
            btn_options.setText(R.string.options_button_ru);
            btn_scores.setText(R.string.scores_button_ru);
            btn_about.setText(R.string.about_btn_ru);

        }else{
            //String phrases[] = getResources().getStringArray(R.array.phrases_eng_arr);
            btn_start.setText(R.string.start_button_eng);
            btn_options.setText(R.string.options_button_eng);
            btn_scores.setText(R.string.scores_button_eng);
            btn_about.setText(R.string.about_btn_eng);
        }
    }
    public void SpamOne(final LinearLayout chat_win){
        MenuMessage user = new MenuMessage(this);
        TextView textView_msg = new TextView(this);
        TextView textView_user = new TextView(this);
        LinearLayout chat_cloud = new LinearLayout(this);

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,0);
        //params.gravity = turn?Gravity.START:Gravity.END;
        params.gravity = Gravity.START;
        chat_cloud.setLayoutParams(params);
        //chat_cloud.setGravity(turn?Gravity.START:Gravity.END);
        chat_cloud.setGravity(Gravity.START);
        chat_cloud.getGravity();
        //chat_cloud.setBackgroundResource(turn?R.drawable.chat_cloud_left:R.drawable.chat_cloud_right);
        chat_cloud.setBackgroundResource(R.drawable.chat_cloud_left);
        chat_cloud.setOrientation(LinearLayout.VERTICAL);

        textView_msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        textView_msg.setText(user.GetMessage());
        //textView_msg.setGravity(turn?Gravity.START:Gravity.END);
        textView_msg.setGravity(Gravity.START);
        textView_msg.setTextColor(Color.parseColor("#FF000000"));

        textView_user.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView_user.setText(user.GetNick());
        //textView_user.setGravity(turn?Gravity.START:Gravity.END);
        textView_user.setTextColor(Color.parseColor("#88000000"));
        textView_user.setGravity(Gravity.START);

        chat_cloud.addView(textView_msg);
        chat_cloud.addView(textView_user);

        chat_win.addView(chat_cloud);
    }
    public void SpamMessages(int count,int time){
        hndlr.postDelayed(new Runnable() {
            @Override
            public void run() {
                counter = count-1;
                SpamOne((LinearLayout)findViewById(R.id.chat_window));
                if(counter!=0){
                    SpamMessages(counter,time);
                }else{
                    return;
                }
            }
        }, time);
    }
    public void ShowAbout(Context ctx) {
        String text;
        String title="About";
        if(OptionsActivity.GetSettings(ctx)[0].equals("")){
            OptionsActivity.SetLang(ctx);
        }
        if (OptionsActivity.GetSettings(ctx)[0].equals("ru")) {
            text = getResources().getString(R.string.about_ru);
            title = "?? ??????????????????";
        }else{
            text = getResources().getString(R.string.about_eng);
            title="About";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title)
                .setMessage(text)
                .setCancelable(true);
        AlertDialog alert = builder.create();
        alert.show();
    }
}

