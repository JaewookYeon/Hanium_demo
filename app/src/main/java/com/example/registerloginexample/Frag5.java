package com.example.registerloginexample;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class Frag5 extends Fragment {

    private Button buttonEditProfile;
    private Button buttonContact;
    private Button buttonLogout;
    private String login_id;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag5, container, false);

        buttonEditProfile = view.findViewById(R.id.my_editUser);
        buttonContact = view.findViewById(R.id.my_contact);
        buttonLogout = view.findViewById(R.id.my_logout);

        // 프로필 이미지 아이콘 클릭 시 동작
        View profileImageView = view.findViewById(R.id.profileImageView);
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 다이얼로그 빌더 생성
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("프로필 사진 변경");
                // 옵션 목록 설정
                CharSequence[] options = new CharSequence[]{"카메라", "앨범", "기본"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                // 카메라 옵션 선택 시 동작
                                //openCamera();
                                break;
                            case 1:
                                // 앨범 옵션 선택 시 동작
                                //openGallery();
                                break;
                            case 2:
                                // 기본 사진 옵션 선택 시 동작
                                //setDefaultProfileImage();
                                break;
                        }
                    }
                });
                builder.show();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            login_id = bundle.getString("login_id");
            Log.d("Frag5", "Received login_id: " + login_id);
        }

        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                startActivity(intent);
            }
        });

        buttonLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogoutActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        // 서버에서 사용자 이름 가져오기
        String url = "http://ruddk658.dothome.co.kr/frag5.php?login_id=" + login_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String userName = response.getString("name");
                        TextView nameTextView = getView().findViewById(R.id.nameTextView);
                        nameTextView.setText(userName);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("ServerResponse", "Error: " + error.toString());
                        error.printStackTrace();
                    }
                });

        Volley.newRequestQueue(getContext()).add(request);
    }
}
