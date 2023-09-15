package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class WriteActivity extends AppCompatActivity {

    private EditText mWriteTitleText;
    private EditText mWriteContentsText;
    private String login_id;
    private String custid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_write);

        mWriteTitleText = findViewById(R.id.write_title_text);
        mWriteContentsText = findViewById(R.id.write_contents_text);

        Button writeUploadButton = findViewById(R.id.write_upload_button);

        Intent intent = getIntent();
        if (intent != null) {
            login_id = intent.getStringExtra("login_id");
        }

        writeUploadButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 사용자가 버튼을 클릭할 때 데이터를 DB에 넣도록 메서드 호출
                getCustidAndInsertData();
            }
        });
    }

    private void getCustidAndInsertData() {
        String baseUrl = "http://3.209.169.0/custid.php";
        String url = baseUrl + "?login_id=" + login_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            custid = response.getString("custid");
                            // 이제 custid를 사용하여 DB에 데이터를 넣을 수 있습니다.
                            // LOG로 login_id와 custid를 확인
                            Log.d("WriteActivity", "login_id: " + login_id + ", custid: " + custid);
                            // 데이터베이스에 데이터 삽입 메소드 호출
                            insertDataToDB();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 요청이 실패한 경우 에러 처리를 수행합니다.
                        Toast.makeText(WriteActivity.this, "custid 가져오기 실패", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void insertDataToDB() {
        String baseUrl = "http://3.209.169.0/insert_board.php";
        String url = baseUrl + "?custid=" + custid
                + "&b_title=" + mWriteTitleText.getText().toString()
                + "&b_content=" + mWriteContentsText.getText().toString();

        if (mWriteTitleText.getText().toString().isEmpty()) {
            Toast.makeText(WriteActivity.this, "제목을 작성하세요.", Toast.LENGTH_SHORT).show();
            return;
        }
        if (mWriteContentsText.getText().toString().isEmpty()) {
            Toast.makeText(WriteActivity.this, "내용을 작성하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean success = response.getBoolean("success");
                            if (success) {
                                Toast.makeText(WriteActivity.this, "게시가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(WriteActivity.this, "게시 실패", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(WriteActivity.this, "게시 실패", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}
