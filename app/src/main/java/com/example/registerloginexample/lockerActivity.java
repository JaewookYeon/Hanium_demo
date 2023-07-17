package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class lockerActivity extends AppCompatActivity {

    private EditText editText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        Button btn [] = new Button[12];

        btn[0] = findViewById(R.id.btn0);
        btn[1] = findViewById(R.id.btn1);
        btn[2] = findViewById(R.id.btn2);
        btn[3] = findViewById(R.id.btn3);
        btn[4] = findViewById(R.id.btn4);
        btn[5] = findViewById(R.id.btn5);
        btn[6] = findViewById(R.id.btn6);
        btn[7] = findViewById(R.id.btn7);
        btn[8] = findViewById(R.id.btn8);
        btn[9] = findViewById(R.id.btn9);
        btn[10] = findViewById(R.id.btn_ok);
        btn[11] = findViewById(R.id.btn_clear);
        editText = findViewById(R.id.displayNumber);

        for (int i=0; i<12;i++) {
            btn[i].setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {
                    Button btn = (Button) v;
                    editText.append(btn.getText().toString());
                    if (editText.getText().toString().equals("0")) {
                        editText.setText("");
                    }
                    if (btn.getText().toString().equals("clear")) {
                        editText.setText("");
                    }
                    if (btn.getText().toString().equals("Ok")) {
                        //String str = editText.getText().toString();
                        //editText.setText(str.substring(0, str.length() - 2));
                        //Toast.makeText(getApplicationContext(), editText.getText(), Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(), "보관함이 열렸습니다.", Toast.LENGTH_SHORT).show();
                        editText.setText("");
                        Intent intent = new Intent(getApplicationContext(), Frag3.class);
                        startActivity(intent);
                    }
                }
            });
        }
    }
}