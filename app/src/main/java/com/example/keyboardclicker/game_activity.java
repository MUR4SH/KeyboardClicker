package com.example.keyboardclicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

class Chat extends AppCompatActivity {
    private String nickname;
    private String message;
    private String phrase;
    private int num=0;
    private String lang="";
    public Chat(Context ctx){
        Random r = new Random();

        this.nickname = ctx.getResources().getStringArray(R.array.names_array)[r.nextInt(20)];
        this.SetLang(ctx);
        this.SetMessage(ctx,0);
        this.SetPhrase(ctx,0);
        this.num = 0;
    }
    public void SetLang(Context ctx){
        String lang="";
        if (ctx.getResources().getString(R.string.settings_language).equals("system")) {
            lang = Locale.getDefault().getLanguage();
        }else{
            lang = ctx.getResources().getString(R.string.settings_language);
        }
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
        if(this.lang.equals("")){
            this.SetLang(ctx);
        }
        if (this.lang.equals("ru") || this.lang.equals("RU") || this.lang.equals("Russian") || this.lang.equals("RUSSIAN") || this.lang.equals("russian")) {
            this.message = ctx.getResources().getStringArray(R.array.friend_ru)[num];
        }else{
            this.message = ctx.getResources().getStringArray(R.array.friend_eng)[num];
        }
    }
    public void SetPhrase(Context ctx,int num){
        if(this.lang.equals("")){
            this.SetLang(ctx);
        }
        if (this.lang.equals("ru") || this.lang.equals("RU") || this.lang.equals("Russian") || this.lang.equals("RUSSIAN") || this.lang.equals("russian")) {
            this.phrase = ctx.getResources().getStringArray(R.array.player_ru)[num];
        }else{
            this.phrase =  ctx.getResources().getStringArray(R.array.player_eng)[num];
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

    public int CheckString(Context ctx, EditText input, TextView hint){
        String sample = this.phrase;
        String user_input = input.getText().toString();
        if(user_input.length()>sample.length()){
            return 0; //Неверно
        }
        hint.setText(sample.substring(user_input.length()-1,sample.length()));
        for(int i=0;i<user_input.length()-1;i++){
            if(!(user_input.substring(i,i+1).equals(sample.substring(i,i+1)))){
                return 0; //Неверно
            }
        }
        if(user_input.length()==sample.length()){
            this.num++;
            return 2; //Строки совпали
        }else {
            return 1; //Подстроки верны
        }
    }
}

public class game_activity extends AppCompatActivity {
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
        TextView hint = (TextView)findViewById(R.id.TextHint);
        hint.setText(user.GetPhrase());
        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                int check = user.CheckString(ctx,input,hint);
                if(check == 0){
                    input.setTextColor(Color.parseColor("#FFFF0000"));
                }else{
                    input.setTextColor(Color.parseColor("#FF000000"));
                    if(check == 2){
                        chat_window.addView(user.GenerateUserMessage(ctx));
                        user.SetMessage(ctx,user.GetNum());
                        user.SetPhrase(ctx,user.GetNum());
                        chat_window.addView(user.GenerateChatMessage(ctx));
                        hint.setText(user.GetPhrase());
                        input.setText(null);
                    }
                }
                return;
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { }
            @Override
            public void afterTextChanged(Editable s) {}
        });




        /*TextView = (Button)findViewById(R.id.btn_return);//InputText
        btn_rtrn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(game_activity.this,MainActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });*/
    }
    public void showKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.requestFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Activity.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

        /*CustomScrollView csv = (CustomScrollView)findViewById(R.id.scroll_view);
        csv.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));*/
    }

    public void hideSoftKeyboard(EditText mEtSearch, Context context) {
        mEtSearch.clearFocus();
        InputMethodManager imm = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEtSearch.getWindowToken(), 0);
    }
}
