package com.example.registerloginexample;

import androidx.annotation.Nullable;
import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {

    //서버 URL 설정 (php 파일 연동)
    final static private String URL="http://3.209.169.0/Register.php";
    private Map<String,String> map;

    public RegisterRequest(String login_id, String login_password, String name, String nickname, String phone, String address, String password_hintNum, String password_hint, Response.Listener <String> listener){
        super(Method.POST, URL, listener, null);

        map=new HashMap<>();
        map.put("login_id",login_id);
        map.put("login_password",login_password);
        map.put("name",name);
        map.put("nickname",nickname);
        map.put("phone",phone);
        map.put("address",address);
        map.put("password_hintNum", String.valueOf(password_hintNum));
        map.put("password_hint",password_hint);
    }

    @Nullable
    @Override
    protected Map<String,String> getParams() throws AuthFailureError{
        return map;
    }
}
