package com.patelcodex.Supplimo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.patelcodex.Supplimo.R;

public class splash_screen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        try
        {
            this.getSupportActionBar().hide();
        }
        catch (Exception e){}
        new Handler().postDelayed(new Runnable() {
            public void run() {

                Intent intent = new Intent();
                intent.setClass(splash_screen.this,
                        MainActivity.class);
                splash_screen.this.startActivity(intent);
                overridePendingTransition(R.anim.enter, R.anim.exit);
                splash_screen.this.finish();

            }
        }, 2000);
    }
}