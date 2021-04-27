package com.example.keyboardclicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;
import java.util.Random;

class Chat extends AppCompatActivity {
    private String nickname;
    private String message;
    private String phrase;
    public Chat(Context ctx){
        String lang;
        if (ctx.getResources().getString(R.string.settings_language).equals("system")) {
            lang = Locale.getDefault().getLanguage();
        }else{
            lang = ctx.getResources().getString(R.string.settings_language);
        }
        Random r = new Random();
        this.nickname = ctx.getResources().getStringArray(R.array.names_array)[r.nextInt(20)];
        if (lang.equals("ru") || lang.equals("RU") || lang.equals("Russian") || lang.equals("RUSSIAN") || lang.equals("russian")) {
            this.message = ctx.getResources().getString(R.string.initiate_text_ru);
        }else{
            this.message =  ctx.getResources().getString(R.string.initiate_text_eng);
        }
    }

    public String GetNick(){
        return this.nickname;
    }
    public String GetMessage(){
        return this.message;
    }
    public void SetNick(Context ctx){
        TextView TextNick = findViewById(R.id.userNick);
        TextNick.setText(this.nickname);
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
    public void CheckString(Context ctx){
        return;
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
