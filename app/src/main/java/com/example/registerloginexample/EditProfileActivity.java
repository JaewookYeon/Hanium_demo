package com.example.registerloginexample;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextAge;
    private EditText editTextPhone;
    private Button buttonSave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // EditText와 Button을 findViewById를 통해 연결합니다.
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        buttonSave = findViewById(R.id.buttonSave); // 버튼 초기화 추가

        // 수정하기 버튼을 클릭했을 때의 동작을 정의합니다.
        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 수정된 정보를 가져와서 처리하는 코드를 작성합니다.
                String name = editTextName.getText().toString();
                String age = editTextAge.getText().toString();
                String phone = editTextPhone.getText().toString();
                Toast.makeText(EditProfileActivity.this, "정보수정이 완료되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
