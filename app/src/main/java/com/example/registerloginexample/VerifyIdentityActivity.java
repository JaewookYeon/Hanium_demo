package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log; // Log 추가
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.registerloginexample.EditProfileActivity;
import com.example.registerloginexample.R;
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
                handleVerification();
            }
        });
    }

    private void handleVerification() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }

        String receivedLoginId = intent.getStringExtra("login_id");
        String enteredPassword = editTextPassword.getText().toString();

        if (TextUtils.isEmpty(enteredPassword)) {
            showToast("비밀번호를 입력하세요.");
            return;
        }

        String url = "http://3.209.169.0/verify.php";

        JSONObject requestData = new JSONObject();
        try {
            requestData.put("login_id", receivedLoginId);
            requestData.put("entered_password", enteredPassword);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData, response -> {
            try {
                String login_password = response.getString("login_password");
                Log.d("VerifyIdentity", "DB Password: " + login_password); // Log로 DB에서 받아온 비밀번호 출력

                if (!TextUtils.isEmpty(login_password) && login_password.equals(enteredPassword)) {
                    showSuccessToast();
                    navigateToEditProfile(receivedLoginId);
                } else {
                    showPasswordMismatchToast();
                }
            } catch (JSONException e) {
                e.printStackTrace();
                showOtherErrorToast();
            }
        }, error -> showNetworkErrorToast());


        Volley.newRequestQueue(this).add(request);
    }

    private void navigateToEditProfile(String loginId) {
        Intent editProfileIntent = new Intent(VerifyIdentityActivity.this, EditProfileActivity.class);
        editProfileIntent.putExtra("login_id", loginId);
        startActivity(editProfileIntent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(VerifyIdentityActivity.this, message, Toast.LENGTH_SHORT).show();
    }

    private void showSuccessToast() {
        showToast("본인 확인되었습니다.");
    }

    private void showPasswordMismatchToast() {
        showToast("비밀번호가 일치하지 않습니다.");
    }

    private void showUserNotFoundToast() {
        showToast("사용자를 찾을 수 없습니다.");
    }

    private void showDataMissingToast() {
        showToast("POST 데이터가 누락되었습니다.");
    }

    private void showOtherErrorToast() {
        showToast("오류가 발생했습니다.");
    }

    private void showNetworkErrorToast() {
        showToast("네트워크 오류가 발생했습니다.");
    }
}
