package com.example.registerloginexample;

import static android.widget.Toast.makeText;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


import java.util.HashMap;
import java.util.Map;

public class WriteActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private EditText mWriteNameText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);
        mWriteNameText = findViewById(R.id.write_name_text);

        findViewById(R.id.write_upload_button).setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        Map<String,Object> post = new HashMap<>();
        post.put("id", "");
        post.put("title", mWriteTitleText.getText().toString());
        post.put("contents", mWriteContentsText.getText().toString());
        post.put("name", mWriteNameText.getText().toString());

        Toast.makeText(WriteActivity.this, "게시가 완료되었습니다.", Toast.LENGTH_SHORT).show();
        finish();
    }


}