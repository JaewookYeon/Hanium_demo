package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class SharedMain extends AppCompatActivity {

    private ImageView btn_want;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shared_main);

        btn_want=(ImageView) findViewById(R.id.btn_want);
        btn_want.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent  intent=new Intent(SharedMain.this,Want.class);
                startActivity(intent);
            }
        });

    }

}