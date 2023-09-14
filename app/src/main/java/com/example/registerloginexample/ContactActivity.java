package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerType;
    private EditText editTextContent;
    private Button buttonSubmit;
    private String custid;
    private String login_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Intent intent = getIntent();
        if (intent != null) {
            login_id = intent.getStringExtra("login_id");
        }
        getCustid();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        spinnerType = findViewById(R.id.spinnerType);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.contact_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertAskData();
            }
        });

    }
    private void insertAskData() {
        // MySQL 데이터베이스에 데이터를 삽입하는 요청을 보냅니다.
        String baseUrl = "http://3.209.169.0/insert_ask.php";
        String url = baseUrl + "?custid=" + custid
                + "&a_type=" + spinnerType.getSelectedItem().toString()
                + "&a_content=" + editTextContent.getText().toString();

        // editTextContent가 비어 있는지 확인
        if (editTextContent.getText().toString().isEmpty()) {
            Toast.makeText(ContactActivity.this, "문의글을 작성하세요.", Toast.LENGTH_SHORT).show();
            return; // editTextContent가 비어 있으면 함수 종료
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 데이터베이스에 성공적으로 데이터가 삽입된 경우
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");
                            if (success) {
                                // 삽입 성공
                                Toast.makeText(ContactActivity.this, "문의가 성공적으로 제출되었습니다.", Toast.LENGTH_SHORT).show();
                                finish(); // 액티비티 종료
                            } else {
                                // 삽입 실패
                                Toast.makeText(ContactActivity.this, "문의 제출에 실패했습니다. " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 요청이 실패한 경우 에러 처리
                        Log.e("ContactActivity", "Error: " + error.getMessage());
                        Toast.makeText(ContactActivity.this, "문의 제출에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }

    private void getCustid() {
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
                            Log.d("ContactActivity", "Received custid: " + custid);
                            // 이제 custid를 사용하여 원하는 작업을 수행할 수 있습니다.
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 요청이 실패한 경우 에러 처리를 수행합니다.
                        Log.e("ContactActivity", "Error: " + error.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}