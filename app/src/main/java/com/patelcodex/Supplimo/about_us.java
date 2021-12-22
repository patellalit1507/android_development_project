package com.patelcodex.Supplimo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;


import com.patelcodex.Supplimo.R;
public class about_us extends AppCompatActivity {
    TextView about_us;
    ImageButton img_btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        about_us=findViewById(R.id.about_us);
        img_btn=findViewById(R.id.about_back);
        img_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); 
            }
        });
        about_us.setText("SUPPLIMO \n" +
                "\n" +
                "SuppliMo is a one-stop-shop for all your supplements and health needs.SuppliMo is a platform that aims at providing u trustworthy and verified products at your doorsteps. \n" +
                "We always try you to know, the benefits of the products before buying the products.\n" +
                "\n" +
                "Co-Founders, PROTEIN NATION\n" +
                "Dheeraj Birla\n" +
                "Lalit Patel\n" +
                "Deepak Patel");
    }
}