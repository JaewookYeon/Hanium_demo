package com.example.registerloginexample;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText et_id,et_pass,et_name,et_phone,et_address;
    private Button btn_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) { //액티비티 시작시 처음으로 실행되는 생명주기
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //아이디 값 찾아주기
        et_id=findViewById(R.id.et_id);
        et_pass=findViewById(R.id.et_pass);
        et_name=findViewById(R.id.et_name);
        et_phone=findViewById(R.id.et_phone);
        et_address=findViewById(R.id.et_address);

        //회원가입 버튼 클릭 시 수행
        btn_register=findViewById(R.id.btn_register);
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //EditText에 현재 입력되어 있는 값을 얻어온다.
                final String login_id=et_id.getText().toString();
                final String login_password=et_pass.getText().toString();
                final String name=et_name.getText().toString();
                final String phone=et_phone.getText().toString();
                final String address=et_address.getText().toString();

                Response.Listener<String> responseListener=new Response.Listener<String>() {
                    @Override

                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject=new JSONObject(response);
                            boolean success=jsonObject.getBoolean("success");
                            if(success){ //회원등록에 성공한 경우
                                Toast.makeText(getApplicationContext(),"회원 등록에 성공하였습니다.",Toast.LENGTH_SHORT).show();
                                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                                startActivity(intent);
                            }else{//회원등록에 실패한 경우
                                Toast.makeText(getApplicationContext(),"회원 등록에 실패하였습니다.",Toast.LENGTH_SHORT).show();

                                return;
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }public void onErrorResponse(VolleyError error) {
                        Toast.makeText(getApplicationContext(), "회원가입 실패: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                };
                //서버로 Volley를 이용해서 요청
                RegisterRequest registerRequest=new RegisterRequest(login_id,login_password,name,phone,address,responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(registerRequest);
            }
        });
    }
}