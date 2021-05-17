package com.example.keyboardclicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Region;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;

public class OptionsActivity extends AppCompatActivity {
    static String lang="";
    static String username="";
    static Boolean test = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.options_activity);

        this.GetSettings(this);
        Button btn_return = (Button)findViewById(R.id.btn_back);
        Button btn_accept = (Button)findViewById(R.id.btn_accept);

        TextView username_opt_label = (TextView) findViewById(R.id.username_label);
        EditText username_edit = (EditText) findViewById(R.id.username_edit);
        TextView lang_opt_label = (TextView) findViewById(R.id.language_label);
        RadioButton ru_lang = (RadioButton) findViewById(R.id.ru_lang);
        RadioButton eng_lang = (RadioButton) findViewById(R.id.eng_lang);
        RadioGroup radioGroup = (RadioGroup) findViewById(R.id.lang_group);
        RadioButton tst_btn = (RadioButton) findViewById(R.id.test_btn);

        String arr[] = GetSettings(this);
        lang = arr[0];
        username = arr[1];
        this.SetLang(this);
        if(lang.equals("eng")){
            eng_lang.setChecked(true);
        }else if(lang.equals("ru")){
            ru_lang.setChecked(true);
        }

        username_edit.setText(username);
        lang_opt_label.setText(this.lang.equals("ru")?R.string.language_ru:R.string.language_eng);
        username_opt_label.setText(this.lang.equals("ru")?R.string.username_ru:R.string.username_eng);
        btn_return.setText(this.lang.equals("ru")?R.string.back_ru:R.string.back_eng);
        btn_accept.setText(this.lang.equals("ru")?R.string.accept_ru:R.string.accept_eng);
        tst_btn.setChecked(test);

        tst_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    test = !test;
                    tst_btn.setChecked(test);
                }catch(Exception e){

                }
            }
        });
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    Intent intent = new Intent(OptionsActivity.this,MainActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });
        Context ctx = this;
        btn_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try{
                    if(radioGroup.getCheckedRadioButtonId() == R.id.eng_lang){
                        lang = "eng";
                    }else if(radioGroup.getCheckedRadioButtonId() == R.id.ru_lang){
                        lang = "ru";
                    }
                    SetTest(tst_btn.isChecked());
                    if (username_edit.getText().toString().length() >= 3){
                        username = username_edit.getText().toString();
                        SetSettings(ctx);
                    }
                    else {
                        username ="Username";
                        SetSettings(ctx);
                    }
                    SetLang(ctx);
                    String arr[] = GetSettings(ctx);
                    lang = arr[0];
                    username = arr[1];

                    Intent intent = new Intent(OptionsActivity.this,MainActivity.class);
                    startActivity(intent);finish();
                }catch(Exception e){

                }
            }
        });
    }
    public void onBackPressed(){
        //super.onBackPressed();
        Intent intent = new Intent(OptionsActivity.this,MainActivity.class);
        startActivity(intent);finish();
    }
    public static void SetLang(Context ctx){
        if (GetSettings(ctx)[0].equals("")) {
            lang = Locale.getDefault().getLanguage();
        }
        if (lang.equals("ru") || lang.equals("RU") || lang.equals("Russian") || lang.equals("RUSSIAN") || lang.equals("russian")) {
            lang = "ru";
        }else{
            lang = "eng";
        }
    }

    public static void SetSettings(Context ctx){
        SQLiteDatabase db = ctx.openOrCreateDatabase("KeyboardClicker.db", MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS settings (id INT,language TEXT, username TEXT);");
        Cursor curs = db.rawQuery("SELECT * from settings WHERE id=?;", new String[]{"1"});
        Boolean check=true;
        try {
            curs.moveToFirst();
            check = curs.getString(1).isEmpty();
        }catch(Exception e){
            check = true;
        }
        if(!check){
            String arr[]= {lang,username,"1"};
            db.execSQL("UPDATE settings SET language=?, username=? WHERE id=?;",arr);
        }else{
            String arr[]= {"1",lang,username};
            db.execSQL("INSERT INTO settings (id,language,username) VALUES(?,?,?);",arr);
        }
        db.close();
    }

    public static  String[] GetSettings(Context ctx){
        SQLiteDatabase db = ctx.openOrCreateDatabase("KeyboardClicker.db", MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS settings (id INT,language TEXT, username TEXT);");

        Cursor query = db.rawQuery("SELECT * FROM settings WHERE id=?;", new String[]{"1"});
        String arr[]=new String[2];
        String lng="";
        String usrname="";
        try{
            query.moveToFirst();
            if(!query.getString(1).isEmpty()){
                lng = query.getString(1);
                usrname = query.getString(2);
            }else{
                lng="eng";
                usrname="username";
            }
        }catch(Exception err){
            lng="eng";
            usrname="username";
        }

        if(lng.equals("")) {
            lng = Locale.getDefault().getLanguage();
        }
        if (lng.equals("ru") || lng.equals("RU") || lng.equals("Russian") || lng.equals("RUSSIAN") || lng.equals("russian")) {
            lng = "ru";
        }else{
            lng = "eng";
        }
        arr[0] = lng;
        arr[1] = usrname;

        query.close();
        db.close();
        return arr;
    }
    public static void SetTest(Boolean t){
        test = t;
    }
    public static Boolean GetTest(){
        return test;
    }
}
