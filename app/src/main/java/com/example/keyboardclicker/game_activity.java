package com.example.keyboardclicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.SystemClock;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.io.CharArrayWriter;
import java.util.Locale;
import java.util.Random;

class Chat extends game_activity {
    private String nickname;
    private String message;
    private String phrase;
    private int num=0; //aka level


    public Chat(Context ctx){
        Random r = new Random();

        this.nickname = ctx.getResources().getStringArray(R.array.names_array)[r.nextInt(20)];
        OptionsActivity.SetLang(ctx);
        this.SetMessage(ctx,0);
        this.SetPhrase(ctx,0);
        this.num = 0;
    }

    public String GetNick(){
        return this.nickname;
    }
    public String GetMessage(){
        return this.message;
    }
    public String GetPhrase(){
        return this.phrase;
    }
    public int GetNum(){
        return this.num;
    }
    public void SetMessage(Context ctx,int num){
        if(OptionsActivity.GetSettings(ctx)[0].equals("")){
            OptionsActivity.SetLang(ctx);
        }
        if (OptionsActivity.GetSettings(ctx)[0].equals("ru")) {
            try {
                this.message = ctx.getResources().getStringArray(R.array.friend_ru)[num];
            }catch (Exception err){

            }
        }else{
            try {
                this.message = ctx.getResources().getStringArray(R.array.friend_eng)[num];
            }catch (Exception err){

            }
        }
    }
    public void SetPhrase(Context ctx,int num){
        if(OptionsActivity.GetSettings(ctx)[0].equals("")){
            OptionsActivity.SetLang(ctx);
        }
        if (OptionsActivity.GetSettings(ctx)[0].equals("ru")) {
            try {
                this.phrase = ctx.getResources().getStringArray(R.array.player_ru)[num];
            }catch (Exception err){

            }
        }else{
            try {
                this.phrase = ctx.getResources().getStringArray(R.array.player_eng)[num];
            }catch (Exception err){

            }
        }
    }
    public LinearLayout GenerateChatMessage(Context ctx){
        TextView textView_msg = new TextView(ctx);
        TextView textView_user = new TextView(ctx);
        LinearLayout chat_cloud = new LinearLayout(ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,0);

        params.gravity = Gravity.START;
        chat_cloud.setLayoutParams(params);

        chat_cloud.setGravity(Gravity.START);
        chat_cloud.getGravity();

        chat_cloud.setBackgroundResource(R.drawable.chat_cloud_left);
        chat_cloud.setOrientation(LinearLayout.VERTICAL);

        textView_msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        textView_msg.setText(this.GetMessage());

        textView_msg.setGravity(Gravity.START);
        textView_msg.setTextColor(Color.parseColor("#FF000000"));

        textView_user.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        textView_user.setText(this.GetNick());

        textView_user.setTextColor(Color.parseColor("#88000000"));
        textView_user.setGravity(Gravity.START);

        chat_cloud.addView(textView_msg);
        chat_cloud.addView(textView_user);

        return chat_cloud;
    }
    public LinearLayout GenerateUserMessage(Context ctx){
        TextView textView_msg = new TextView(ctx);
        TextView textView_user = new TextView(ctx);
        LinearLayout chat_cloud = new LinearLayout(ctx);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        params.setMargins(10,10,10,0);

        params.gravity = Gravity.END;
        chat_cloud.setLayoutParams(params);

        chat_cloud.setGravity(Gravity.END);
        chat_cloud.getGravity();

        chat_cloud.setBackgroundResource(R.drawable.chat_cloud_right);
        chat_cloud.setOrientation(LinearLayout.VERTICAL);

        textView_msg.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView_msg.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18f);
        textView_msg.setText(this.GetPhrase());

        textView_msg.setGravity(Gravity.END);
        textView_msg.setTextColor(Color.parseColor("#FF000000"));

        textView_user.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
        textView_user.setTextSize(TypedValue.COMPLEX_UNIT_SP, 10f);
        if(ctx.getResources().getText(R.string.settings_username).equals("")){
            textView_user.setText("Just a user");
        }else{
            textView_user.setText(ctx.getResources().getText(R.string.settings_username));
        }

        textView_user.setTextColor(Color.parseColor("#88000000"));
        textView_user.setGravity(Gravity.END);

        chat_cloud.addView(textView_msg);
        chat_cloud.addView(textView_user);

        return chat_cloud;
    }

    public int CheckString(Context ctx, EditText input, TextView hint, int nummax){
        String sample = this.phrase;
        String user_input = input.getText().toString();
        if(user_input.length()>sample.length()){
            return 0; //Неверно
        }
        hint.setText(sample.substring(user_input.length(),(sample.length()>(user_input.length()+61)?user_input.length()+61:sample.length())));
        for(int i=0;i<user_input.length();i++){
            if(!(user_input.substring(i,i+1).equals(sample.substring(i,i+1)))){
                return 0; //Неверно
            }
        }
        if(user_input.length()==sample.length()){
            if(this.num<nummax) {
                this.num++;
            }
            return 2; //Строки совпали
        }else {
            return 1; //Подстроки верны
        }
    }


}

public class game_activity extends AppCompatActivity {
    static long start_time=0;
    static long time=0; //time in ms
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_activity);

        Button btn_rtrn = (Button)findViewById(R.id.btn_return);//InputText
        btn_rtrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(game_activity.this,MainActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });

        Chat user = new Chat(this);
        TextView TextNick = findViewById(R.id.userNick);
        TextNick.setText(user.GetNick());

        LinearLayout chat_window = (LinearLayout)findViewById(R.id.chat_window);
        chat_window.addView(user.GenerateChatMessage(this));

        Context ctx = this;

        EditText input = (EditText)findViewById(R.id.InputText);
        input.setText(null);
        TextView hint = (TextView)findViewById(R.id.TextHint);
        hint.setText(user.GetPhrase().substring(0,(Math.min(user.GetPhrase().length(), 61))));

        this.StartTimer();

        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                int check = user.CheckString(ctx,input,hint,4);
                if(check == 0){
                    input.setTextColor(Color.parseColor("#FFFF0000"));
                }else{
                    input.setTextColor(Color.parseColor("#FF000000"));
                    if(check == 2){
                        chat_window.addView(user.GenerateUserMessage(ctx));
                        user.SetMessage(ctx,user.GetNum());
                        user.SetPhrase(ctx,user.GetNum());
                        hint.setText(user.GetPhrase().substring(0,(Math.min(user.GetPhrase().length(), 61))));
                        input.setText(null);
                        if(user.GetNum() > 3){
                            StopTimer();
                            CountTime();
                            GoToMenuWindow(ctx);
                        }else{
                            chat_window.addView(user.GenerateChatMessage(ctx));
                        }
                    }
                }
            }
        });
    }
    public static void showKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        /*CustomScrollView csv = (CustomScrollView)findViewById(R.id.scroll_view);
        csv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));*/
    }

    public static void hideSoftKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
    }

    public static void StartTimer(){
        start_time = SystemClock.elapsedRealtime();
    }

    public static void StopTimer(){
        time = SystemClock.elapsedRealtime() - start_time;
    }

    public static String CountTime(){
        long totalSecs = time / 1000;
        long hours = totalSecs / 3600;
        long minutes = (totalSecs % 3600) / 60;
        long seconds = totalSecs % 60;

        return (hours+":"+minutes+":"+seconds);
    }

    public void GoToMenuWindow(Context ctx) {
        String last_score = ScoresActivity.GetUsersScoreOffline(ctx);
        String title ="";
        String time_text="";
        String save="";
        String not_save="";
        if(OptionsActivity.GetSettings(ctx)[0].equals("")){
            OptionsActivity.SetLang(ctx);
        }
        if (OptionsActivity.GetSettings(ctx)[0].equals("ru")) {
            if (last_score.compareToIgnoreCase(CountTime())>=0) title = "Конец";
            else title = "Новый рекорд!!!";

            time_text = "Время: ";
            save = "Сохранить время";
            not_save = "Не сохранять";
        }else{
            if (last_score.compareToIgnoreCase(CountTime())>=0) title = "The end";
            else title = "New record!!!";
            time_text = "Time: ";
            save = "Save time";
            not_save = "Don't save";
        }
        AlertDialog.Builder builder = new AlertDialog.Builder(ctx);
        builder.setTitle(title)
                .setMessage(time_text+CountTime())
                .setCancelable(false)
                .setPositiveButton(save,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            ScoresActivity.SetScore(ctx,"username");
                            Intent intent = new Intent(game_activity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch(Exception e){

                        }
                    }
                })
                .setNegativeButton(not_save,new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try{
                            Intent intent = new Intent(game_activity.this,MainActivity.class);
                            startActivity(intent);
                            finish();
                        }catch(Exception e){

                        }
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }
}
