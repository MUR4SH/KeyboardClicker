package com.example.keyboardclicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.graphics.Region;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ScoresActivity extends AppCompatActivity {

    public void onCreate(Bundle savedInstanceState){
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
            ll.addView(FillScores(this,scores[i]));
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

    public static String[] GetScores(Context ctx){
        SQLiteDatabase db = ctx.openOrCreateDatabase("KeyboardClicker.db", MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS scores (name TEXT, time TEXT)");
        String arr[] = new String[255];
        int i=0;
        try{
            Cursor query = db.rawQuery("SELECT * FROM scores;", null);
            while(query.moveToNext()){
                String name = query.getString(0);
                String time = query.getString(1);;
                arr[i] = "Name: "+name+" Time: "+time;
                i++;
            }
            query.close();
        }catch (Exception err){
        }
        String ar[] = new String[i];
        for(int j=0;j<i;j++){
            ar[j] = arr[j];
        }
        db.close();
        return ar;
    }

    public static void SetScore(Context ctx,String name){
        SQLiteDatabase db = ctx.openOrCreateDatabase("KeyboardClicker.db", MODE_PRIVATE, null);
        String time = game_activity.CountTime();

        db.execSQL("CREATE TABLE IF NOT EXISTS scores (name TEXT, time TEXT);");
        String arr[] =  {OptionsActivity.GetSettings(ctx)[1],time};
        Cursor curs = db.rawQuery("SELECT * from scores WHERE name=?;",new String[]{OptionsActivity.GetSettings(ctx)[1]});
        Boolean check=true;
        try {
            curs.moveToFirst();
            check = curs.getString(1).isEmpty();
        }catch(Exception e){
            check = true;
        }
        if(!check){
            Cursor query = db.rawQuery("UPDATE scores SET time=? WHERE name=?", new String[]{time, OptionsActivity.GetSettings(ctx)[1]});
        }else{
            db.execSQL("INSERT INTO scores (name,time) VALUES(?,?);",arr);
        }

        db.close();
    }

    public static  String GetUsersScoreOffline(Context ctx){
        SQLiteDatabase db = ctx.openOrCreateDatabase("KeyboardClicker.db", MODE_PRIVATE, null);

        db.execSQL("CREATE TABLE IF NOT EXISTS scores (name TEXT, time TEXT);");
        String arr="";

        try {
            Cursor query = db.rawQuery("SELECT * FROM scores WHERE name=?;", new String[]{OptionsActivity.GetSettings(ctx)[1]});
            if(query.getCount() <= 0){
                arr = "0:0:0";
            }else {
                query.moveToFirst();
                String time = query.getString(1);
                arr = time;
            }
            query.close();
        }catch(Exception err){
            arr = "0:0:0";
        }

        db.close();
        return arr;
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
