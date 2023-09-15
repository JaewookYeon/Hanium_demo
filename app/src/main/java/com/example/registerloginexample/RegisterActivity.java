package com.example.registerloginexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id, et_pass, et_pass2, et_name, et_nickname, et_phone, et_address, et_passHint;
    private Button btn_register, btn_doubleCheck, btn_doubleCheck2;
    private AlertDialog dialog;
    private Spinner sp_passHint;
    //private TextView textView3;
    private boolean validate=false;

    String[] items = {"당신의 별명은?", "당신이 나온 초등학교는?", "당신이 가장 좋아하는 음식은?", "당신의 핸드폰 기종은?"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        // 아이디 값 찾아주기
        et_id = findViewById(R.id.et_id);
        et_pass = findViewById(R.id.et_pass);
        et_pass2 = findViewById(R.id.et_pass2);
        et_name = findViewById(R.id.et_name);
        et_nickname = findViewById(R.id.et_nickname);
        et_phone = findViewById(R.id.et_phone);
        et_address = findViewById(R.id.et_address);
        sp_passHint = findViewById(R.id.sp_passHint);
        et_passHint = findViewById(R.id.et_passHint);
        //textView3 = findViewById(R.id.textView3);


        btn_register = findViewById(R.id.btn_register);
        btn_doubleCheck = findViewById(R.id.btn_doubleCheck);
        btn_doubleCheck2 = findViewById(R.id.btn_doubleCheck2);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        sp_passHint.setAdapter(adapter);
        sp_passHint.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //textView3.setText(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //textView3.setText("선택: ");
            }
        });

        btn_doubleCheck.setOnClickListener(new View.OnClickListener() {//id중복체크
            @Override
            public void onClick(View view) {
                String login_id=et_id.getText().toString();
                if(validate)
                {
                    return;
                }
                if(login_id.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                    dialog=builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 아이디입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                et_id.setEnabled(false);
                                validate=true;
                                btn_doubleCheck.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 아이디입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(login_id,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);

            }
        });

        btn_doubleCheck2.setOnClickListener(new View.OnClickListener() {//닉네임 중복체크
            @Override
            public void onClick(View view) {
                String nickname=et_nickname.getText().toString();
                if(validate)
                {
                    return;
                }
                if(nickname.equals("")){
                    AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                    dialog=builder.setMessage("닉네임을 입력해주세요.")
                            .setPositiveButton("확인",null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse=new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 있는 닉네임입니다.")
                                        .setPositiveButton("확인",null)
                                        .create();
                                dialog.show();
                                et_nickname.setEnabled(false);
                                validate=true;
                                btn_doubleCheck2.setText("확인");
                            }
                            else{
                                AlertDialog.Builder builder=new AlertDialog.Builder( RegisterActivity.this );
                                dialog=builder.setMessage("사용할 수 없는 닉네임입니다.")
                                        .setNegativeButton("확인",null)
                                        .create();
                                dialog.show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(nickname,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(validateRequest);

            }
        });


        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String login_id = et_id.getText().toString();
                final String login_password = et_pass.getText().toString();
                final String name = et_name.getText().toString();
                final String nickname = et_nickname.getText().toString();
                final String phone = et_phone.getText().toString();
                final String address = et_address.getText().toString();
                final String password_hintNum = String.valueOf(sp_passHint.getSelectedItemPosition());
                final String password_hint = et_passHint.getText().toString();

                // 아이디, 비밀번호, 이름 등 필수 입력 필드가 비어있는지 확인
                if (login_id.isEmpty() || login_password.isEmpty() || name.isEmpty() || nickname.isEmpty() || phone.isEmpty() || address.isEmpty() || password_hint.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "모든 정보를 입력하세요.", Toast.LENGTH_SHORT).show();
                    return;
                }

                // 비밀번호 일치 여부 확인
                if (!login_password.equals(et_pass2.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                    return;
                }

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");

                            if (success) {
                                // 서버가 "success"가 true를 반환한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            } else {
                                // 서버가 "success"가 false를 반환한 경우
                                Toast.makeText(getApplicationContext(), "회원 등록에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), "JSON 파싱 오류: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                };

                // 서버로 Volley를 이용해서 요청
                RegisterRequest registerRequest = new RegisterRequest(login_id, login_password, name, nickname, phone, address, password_hintNum, password_hint, responseListener);
                RequestQueue queue = Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}