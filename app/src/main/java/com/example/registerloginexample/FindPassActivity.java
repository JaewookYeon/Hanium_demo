package com.example.registerloginexample;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class FindPassActivity extends AppCompatActivity {
    private TextView textView;
    private Spinner sp_passAnswer;
    private AlertDialog dialog;
    private Button okayButton1, okayButton2;
    private EditText et_passHintAnswer, et_idAnswer;
    private boolean validate = false;

    String[] items = {"당신의 별명은?", "당신이 나온 초등학교는?", "당신이 가장 좋아하는 음식은?", "당신의 핸드폰 기종은?"};

    public FindPassActivity() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_pass);

        et_idAnswer = findViewById(R.id.et_idAnswer);
        textView = findViewById(R.id.textView);
        okayButton1 = findViewById(R.id.okayButton1);
        okayButton2 = findViewById(R.id.okayButton2);
        sp_passAnswer = findViewById(R.id.sp_passAnswer);
        et_passHintAnswer = findViewById(R.id.et_passHintAnswer);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                this, android.R.layout.simple_spinner_item, items
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_passAnswer.setAdapter(adapter);
        sp_passAnswer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                //textView3.setText(items[position]);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                //textView3.setText("선택: ");
            }
        });

        // ID 중복 확인 버튼 클릭 리스너
        okayButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String login_id = et_idAnswer.getText().toString();
                if (validate) {
                    return;
                }
                if (login_id.equals("")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(FindPassActivity.this);
                    dialog = builder.setMessage("아이디를 입력해주세요.")
                            .setPositiveButton("확인", null)
                            .create();
                    dialog.show();
                    return;
                }
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");
                            if (success) {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindPassActivity.this);
                                dialog = builder.setMessage("없는 아이디입니다.")
                                        .setNegativeButton("확인", null)
                                        .create();
                                dialog.show();

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(FindPassActivity.this);
                                dialog = builder.setMessage("확인되었습니다.")
                                        .setPositiveButton("확인", null)
                                        .create();
                                dialog.show();
                                et_idAnswer.setEnabled(false);
                                validate = true;
                                okayButton1.setText("확인");
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };
                ValidateRequest validateRequest=new ValidateRequest(login_id,responseListener);
                RequestQueue queue= Volley.newRequestQueue(FindPassActivity.this);
                queue.add(validateRequest);
            }
        });

        // 비밀번호 확인 버튼 클릭 리스너
        okayButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 선택한 배열 번호
                //int selectedPosition = sp_passAnswer.getSelectedItemPosition();

                // 사용자가 입력한 답변
                String userAnswer = et_passHintAnswer.getText().toString();

                // 서버에서 비밀번호 관련 정보를 가져오는 요청을 보낼 URL
                String url = "http://3.209.169.0/get_password.php";

                JSONObject requestData = new JSONObject();
                try {
                    requestData.put("userAnswer", userAnswer); // 사용자가 입력한 값 전송
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, url, requestData, response -> {
                    try {
                        // 서버에서 받은 JSON 응답에서 필요한 정보 추출
                        String login_password = response.getString("login_password");
                        String password_hintNum = response.getString("password_hintNum");
                        String password_hint = response.getString("password_hint");

                        Log.d("MyApp", "login_password: " + login_password);
                        Log.d("MyApp", "password_hint: " + password_hint);
                        Log.d("MyApp", "userAnswer: " + userAnswer);
                        Log.d("MyApp", "requestData: " + requestData);
                        // 가져온 정보를 사용하여 일치 여부 확인
                        if (!TextUtils.isEmpty(password_hint) && password_hint.equals(userAnswer)) {
                            showPasswordDialog(login_password); // 비밀번호를 사용자에게 보여주는 다이얼로그 표시
                        } else showAlertDialog("일치하는 정보가 없습니다. 다시 시도하세요.");
                    } catch (JSONException e) {
                        e.printStackTrace();
                        showOtherErrorToast();
                    }
                }, error -> showNetworkErrorToast());

                Volley.newRequestQueue(FindPassActivity.this).add(request);
            }
        });

    }

    // 비밀번호를 사용자에게 보여주는 다이얼로그 표시
    private void showPasswordDialog(String password) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("비밀번호는 '" + password + "' 입니다.");
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 확인 버튼 클릭 시 다이얼로그 닫기
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    // 알림 다이얼로그 표시
    private void showAlertDialog(String message) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(message);
        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // 확인 버튼 클릭 시 다이얼로그 닫기
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    // 네트워크 오류 알림 토스트 표시
    private void showNetworkErrorToast() {
        // 네트워크 오류 메시지를 토스트로 표시
    }

    // 기타 오류 알림 토스트 표시
    private void showOtherErrorToast() {
        // 기타 오류 메시지를 토스트로 표시
    }
}