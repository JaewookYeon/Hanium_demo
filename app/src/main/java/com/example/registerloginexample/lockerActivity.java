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
    private StringBuilder inputStringBuilder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locker);

        Button btn [] = new Button[13]; // Change the array size to 13

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
        btn[10] = findViewById(R.id.btn_C); // Use btn_C for clear
        btn[11] = findViewById(R.id.btn_ok);
        btn[12] = findViewById(R.id.btn_AC); // Use btn_AC for AC
        editText = findViewById(R.id.displayNumber);

        inputStringBuilder = new StringBuilder();

        for (int i=0; i<13; i++) { // Change the loop to run through all 13 buttons
            btn[i].setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    editText.clearFocus();
                    Button btn = (Button) v;
                    String buttonText = btn.getText().toString();

                    if (buttonText.equals("AC")) {
                        inputStringBuilder.setLength(0);
                        editText.setText("");
                    } else if (buttonText.equals("Ok")) {
                        if (inputStringBuilder.length() == 4) {
                            Toast.makeText(getApplicationContext(), "보관함이 열렸습니다.", Toast.LENGTH_SHORT).show();
                            editText.setText("");
                            Intent intent = new Intent(getApplicationContext(), Frag3.class);
                            startActivity(intent);
                        } else {
                            Toast.makeText(getApplicationContext(), "4자리 숫자를 입력해주세요.", Toast.LENGTH_SHORT).show();
                        }
                    } else if (buttonText.equals("C")) { // Check for "C"
                        if (inputStringBuilder.length() > 0) {
                            inputStringBuilder.deleteCharAt(inputStringBuilder.length() - 1);
                            editText.setText(inputStringBuilder.toString());
                        }
                    } else {
                        if (inputStringBuilder.length() < 4) {
                            inputStringBuilder.append(buttonText);
                            editText.setText(inputStringBuilder.toString());
                        }
                    }
                }
            });
        }
    }
}
