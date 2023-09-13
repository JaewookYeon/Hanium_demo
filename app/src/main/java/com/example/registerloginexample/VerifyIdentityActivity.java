package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils; // 필요한 라이브러리 추가
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

public class VerifyIdentityActivity extends AppCompatActivity {
    private TextInputEditText editTextPassword;
    private Button buttonVerify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_identity);

        editTextPassword = findViewById(R.id.editTextPassword);
        buttonVerify = findViewById(R.id.buttonVerify);

        buttonVerify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                if (intent != null) {
                    String receivedLoginId = intent.getStringExtra("login_id");

                    String enteredPassword = editTextPassword.getText().toString();

                    if (TextUtils.isEmpty(enteredPassword)) {
                        Toast.makeText(VerifyIdentityActivity.this, "비밀번호를 입력하세요.", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (verifyPassword(receivedLoginId, enteredPassword)) {
                        // 비밀번호가 일치하면 EditProfileActivity로 이동
                        Toast.makeText(VerifyIdentityActivity.this, "본인 확인되었습니다.", Toast.LENGTH_SHORT).show();
                        Intent editProfileIntent = new Intent(VerifyIdentityActivity.this, EditProfileActivity.class);
                        editProfileIntent.putExtra("login_id", receivedLoginId);
                        startActivity(editProfileIntent);
                        finish(); // 현재 액티비티 종료
                    } else {
                        Toast.makeText(VerifyIdentityActivity.this, "본인 확인에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    private boolean verifyPassword(final String loginId, final String enteredPassword) {
        String url = "http://3.209.169.0/verify.php";

        Log.d("TAG", "Login ID: " + loginId);
        Log.d("TAG", "Entered Password: " + enteredPassword);

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("login_id", loginId);
            requestData.put("entered_password", enteredPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    String status = response.getString("status");
                    if ("success".equals(status)) {
                        // 비밀번호 일치
                        showSuccessToast();
                        Intent editProfileIntent = new Intent(VerifyIdentityActivity.this, EditProfileActivity.class);
                        editProfileIntent.putExtra("login_id", loginId);
                        startActivity(editProfileIntent);
                        finish(); // 현재 액티비티 종료
                    } else if ("failure".equals(status)) {
                        // 비밀번호 불일치
                        showFailureToast();
                    } else if ("user_not_found".equals(status)) {
                        // 사용자 없음
                        showUserNotFoundToast();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // 에러 처리
                showNetworkErrorToast();
            }
        });

        // 서버 요청을 큐에 추가
        Volley.newRequestQueue(this).add(request);

        // 여기서는 비밀번호 일치 여부를 판단하므로 일단 false를 반환합니다.
        return false;
    }


    private void showSuccessToast() {
        Toast.makeText(VerifyIdentityActivity.this, "본인 확인되었습니다.", Toast.LENGTH_SHORT).show();
    }

    private void showFailureToast() {
        Toast.makeText(VerifyIdentityActivity.this, "본인 확인에 실패했습니다.", Toast.LENGTH_SHORT).show();
    }

    private void showUserNotFoundToast() {
        Toast.makeText(VerifyIdentityActivity.this, "사용자를 찾을 수 없습니다.", Toast.LENGTH_SHORT).show();
    }

    private void showNetworkErrorToast() {
        Toast.makeText(VerifyIdentityActivity.this, "네트워크 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
    }
}
