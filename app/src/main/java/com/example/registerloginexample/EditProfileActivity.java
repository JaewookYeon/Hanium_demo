package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class EditProfileActivity extends AppCompatActivity {

    private String name;
    private String login_id;
    private String nickname;
    private String login_password;
    private String phone;
    private String address;

    private EditText editTextName;

    private EditText editTextPassword1;
    private EditText editTextPassword2;
    private EditText editTextNickname;
    private EditText editTextPhone;
    private EditText editTextAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        // 인텐트로부터 로그인 아이디 정보를 받아옴
        Intent intent = getIntent();
        if (intent != null) {
            login_id = intent.getStringExtra("login_id");
        }

        editTextName = findViewById(R.id.editTextName); // EditText 초기화
        Button applyNameButton = findViewById(R.id.apply_name); // 이름 변경 버튼
        // applyNameButton 클릭 이벤트 처리
        applyNameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // EditText에서 새로운 이름을 가져옴
                String newName = editTextName.getText().toString();

                // newName 값이 올바르게 가져와졌는지 확인하기 위해 Log로 출력
                Log.d("EditProfileActivity", "New Name: " + newName);

                // 서버로 새로운 이름을 전송하고 데이터베이스를 업데이트하는 함수 호출
                updateNameOnServer(newName, login_id);
                finish();
                startActivity(getIntent());
            }
        });
    // 비밀번호 변경 버튼 클릭 이벤트 처리
        Button applyPasswordButton = findViewById(R.id.apply_password);
        editTextPassword1 = findViewById(R.id.editTextPassword1);
        editTextPassword2 = findViewById(R.id.editTextPassword2);

        applyPasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // EditText에서 새로운 비밀번호를 가져옴
                String newPassword1 = editTextPassword1.getText().toString();
                String newPassword2 = editTextPassword2.getText().toString();

                // 두 개의 비밀번호 필드를 비교하여 일치하는지 확인
                if (newPassword1.equals(newPassword2)) {
                    // 일치하는 경우에만 서버로 비밀번호 업데이트 요청을 보냄
                    updatePasswordOnServer(newPassword1, login_id);
                    editTextPassword1.setText(""); // 비밀번호 필드 초기화
                    editTextPassword2.setText(""); // 비밀번호 확인 필드 초기화
                } else {
                    // 일치하지 않는 경우에 메시지 표시
                    Toast.makeText(EditProfileActivity.this, "비밀번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });

        editTextNickname = findViewById(R.id.editTextNickname);
        Button applyNicknameButton = findViewById(R.id.apply_nickname);
        applyNicknameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // EditText에서 새로운 이름을 가져옴
                String newNickname = editTextNickname.getText().toString();

                // newName 값이 올바르게 가져와졌는지 확인하기 위해 Log로 출력
                Log.d("EditProfileActivity", "New Nickname: " + newNickname);

                // 서버로 새로운 이름을 전송하고 데이터베이스를 업데이트하는 함수 호출
                updateNicknameOnServer(newNickname, login_id);
                finish();
                startActivity(getIntent());
            }
        });

        editTextPhone = findViewById(R.id.editTextPhone);
        Button applyPhoneButton = findViewById(R.id.apply_phone);
        applyPhoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // EditText에서 새로운 이름을 가져옴
                String newPhone = editTextPhone.getText().toString();

                // newName 값이 올바르게 가져와졌는지 확인하기 위해 Log로 출력
                Log.d("EditProfileActivity", "New Phone: " + newPhone);

                // 서버로 새로운 이름을 전송하고 데이터베이스를 업데이트하는 함수 호출
                updatePhoneOnServer(newPhone, login_id);
                finish();
                startActivity(getIntent());
            }
        });

        editTextAddress = findViewById(R.id.editTextAddress);
        Button applyAddressButton = findViewById(R.id.apply_address);
        applyAddressButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // EditText에서 새로운 주소을 가져옴
                String newAddress = editTextAddress.getText().toString();

                Log.d("EditProfileActivity", "NewAddress: " + newAddress);

                // 서버로 새로운 주소을 전송하고 데이터베이스를 업데이트하는 함수 호출
                updateAddressOnServer(newAddress, login_id);
                finish();
                startActivity(getIntent());
            }
        });


    }




    private void updatePasswordOnServer(final String newPassword, final String login_id) {
        String baseUrl = "http://3.209.169.0/password_update.php";
        String url = baseUrl + "?login_id=" + login_id + "&new_password=" + newPassword;

        // Volley 요청 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답 확인을 위해 로그로 출력
                        Log.d("RequestData", "login_id: " + login_id);
                        Log.d("RequestData", "new_password: " + newPassword);
                        Log.d("ServerResponse", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // 서버에서 성공적인 응답을 받았을 때 실행할 코드
                                Toast.makeText(EditProfileActivity.this, "비밀번호가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 서버에서 실패한 응답을 받았을 때 실행할 코드
                                String message = response.getString("message");
                                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 네트워크 오류 또는 서버 응답 오류 시 실행할 코드
                        Toast.makeText(EditProfileActivity.this, "서버 요청 오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );
        // Volley 요청 큐에 요청을 추가
        Volley.newRequestQueue(this).add(request);
    }
    private void updateNameOnServer(final String newName, final String login_id) {
        String baseUrl = "http://3.209.169.0/name_update.php";
        String url = baseUrl + "?login_id=" + login_id + "&new_name=" + newName;

        // Volley 요청 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답 확인을 위해 로그로 출력
                        Log.d("RequestData", "login_id: " + login_id);
                        Log.d("RequestData", "new_name: " + newName);
                        Log.d("ServerResponse", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // 서버에서 성공적인 응답을 받았을 때 실행할 코드
                                Toast.makeText(EditProfileActivity.this, "이름이 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 서버에서 실패한 응답을 받았을 때 실행할 코드
                                String message = response.getString("message");
                                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 네트워크 오류 또는 서버 응답 오류 시 실행할 코드
                        Toast.makeText(EditProfileActivity.this, "서버 요청 오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Volley 요청 큐에 요청을 추가
        Volley.newRequestQueue(this).add(request);
    }
    private void updateNicknameOnServer(final String newNickname, final String login_id) {
        String baseUrl = "http://3.209.169.0/nickname_update.php";
        String url = baseUrl + "?login_id=" + login_id + "&new_nickname=" + newNickname;

        // Volley 요청 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답 확인을 위해 로그로 출력
                        Log.d("RequestData", "login_id: " + login_id);
                        Log.d("RequestData", "new_nickname: " + newNickname);
                        Log.d("ServerResponse", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // 서버에서 성공적인 응답을 받았을 때 실행할 코드
                                Toast.makeText(EditProfileActivity.this, "닉네임이 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 서버에서 실패한 응답을 받았을 때 실행할 코드
                                String message = response.getString("message");
                                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 네트워크 오류 또는 서버 응답 오류 시 실행할 코드
                        Toast.makeText(EditProfileActivity.this, "서버 요청 오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Volley 요청 큐에 요청을 추가
        Volley.newRequestQueue(this).add(request);
    }
    private void updatePhoneOnServer(final String newPhone, final String login_id) {
        String baseUrl = "http://3.209.169.0/phone_update.php";
        String url = baseUrl + "?login_id=" + login_id + "&new_phone=" + newPhone;

        // Volley 요청 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답 확인을 위해 로그로 출력
                        Log.d("RequestData", "login_id: " + login_id);
                        Log.d("RequestData", "new_phone: " + newPhone);
                        Log.d("ServerResponse", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // 서버에서 성공적인 응답을 받았을 때 실행할 코드
                                Toast.makeText(EditProfileActivity.this, "전화번호가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 서버에서 실패한 응답을 받았을 때 실행할 코드
                                String message = response.getString("message");
                                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 네트워크 오류 또는 서버 응답 오류 시 실행할 코드
                        Toast.makeText(EditProfileActivity.this, "서버 요청 오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Volley 요청 큐에 요청을 추가
        Volley.newRequestQueue(this).add(request);
    }

    private void updateAddressOnServer(final String newAddress, final String login_id) {
        String baseUrl = "http://3.209.169.0/address_update.php";
        String url = baseUrl + "?login_id=" + login_id + "&new_address=" + newAddress;

        // Volley 요청 객체 생성
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답 확인을 위해 로그로 출력
                        Log.d("RequestData", "login_id: " + login_id);
                        Log.d("RequestData", "new_address: " + newAddress);
                        Log.d("ServerResponse", response.toString());

                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                // 서버에서 성공적인 응답을 받았을 때 실행할 코드
                                Toast.makeText(EditProfileActivity.this, "주소가 성공적으로 업데이트되었습니다.", Toast.LENGTH_SHORT).show();
                            } else {
                                // 서버에서 실패한 응답을 받았을 때 실행할 코드
                                String message = response.getString("message");
                                Toast.makeText(EditProfileActivity.this, message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 네트워크 오류 또는 서버 응답 오류 시 실행할 코드
                        Toast.makeText(EditProfileActivity.this, "서버 요청 오류", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        // Volley 요청 큐에 요청을 추가
        Volley.newRequestQueue(this).add(request);
    }


    @Override
    public void onResume() {
        super.onResume();

        // 서버에서 사용자 정보 가져오기
        String url = "http://3.209.169.0/edit_profile.php?login_id=" + login_id;

        // Volley를 사용하여 서버에서 데이터를 가져옴
        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            // 서버 응답에서 데이터 추출
                            name = response.getString("name");
                            login_id = response.getString("login_id");
                            login_password = response.getString("login_password");
                            nickname = response.getString("nickname");
                            phone = response.getString("phone");
                            address = response.getString("address");

                            Log.d("request", "name: "+name);
                            Log.d("request","login_id: "+login_id);
                            Log.d("request", "nickname: "+nickname);
                            Log.d("request", "phone: "+phone);
                            Log.d("request", "address: "+address);

                            TextView nameTextView = findViewById(R.id.nameTextView);
                            nameTextView.setText(name);
                            TextView idTextView = findViewById(R.id.idTextView);
                            idTextView.setText(login_id);
                            TextView nicknameTextView = findViewById(R.id.nicknameTextView);
                            nicknameTextView.setText(nickname);
                            TextView phoneTextView = findViewById(R.id.phoneTextView);
                            phoneTextView.setText(phone);
                            TextView addressTextView = findViewById(R.id.addressTextView);
                            addressTextView.setText(address);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("EditProfileActivity", "Error fetching data from server: " + error.getMessage());
                        // 오류 처리
                    }
                }
        );
        // 요청을 큐에 추가
        Volley.newRequestQueue(this).add(request);
    }
}