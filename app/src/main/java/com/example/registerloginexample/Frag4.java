package com.example.registerloginexample;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Frag4 extends Fragment implements View.OnClickListener {

    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private List<Board> mBoardList = new ArrayList<>();
    private FloatingActionButton mMainWriteButton; // FloatingActionButton 변수 추가
    private String login_id; // Frag4 내에서 사용하는 변수

    public interface VolleyCallback {
        void onSuccess(String result);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // AsyncTask를 실행하여 데이터를 가져옵니다.
        new GetDataFromServer().execute("http://3.209.169.0/getJson.php");
    }

    // AsyncTask로 서버에서 데이터 가져오기
    // AsyncTask로 서버에서 데이터 가져오기
    private class GetDataFromServer extends AsyncTask<String, Void, List<Board>> {
        @Override
        protected List<Board> doInBackground(String... params) {
            List<Board> boardList = new ArrayList<>();
            try {
                String url = params[0];
                String jsonData = fetchDataFromUrl(url);

                Log.d("Frag4", "Received JSON Data: " + jsonData); // JSON 데이터를 로그에 출력

                JSONArray jsonArray = new JSONArray(jsonData);
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String id = jsonObject.getString("b_id");
                    String title = jsonObject.getString("b_title");
                    String content = jsonObject.getString("b_content");
                    String custid = jsonObject.getString("custid");

                    getNicknameFromCustid(custid, new VolleyCallback() {
                        @SuppressLint("NotifyDataSetChanged")
                        @Override
                        public void onSuccess(String name) {
                            Log.d("Frag4", "Received name: " + name);
                            // 닉네임을 받아온 후에 Board 객체 생성
                            Board board = new Board(id, title, content, name);
                            boardList.add(board);
                            mAdapter.notifyDataSetChanged(); // 어댑터에 변경 알림
                        }
                    });
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("MyApp", "Error fetching data: " + e.getMessage());
            }
            return boardList;
        }

        @Override
        protected void onPostExecute(List<Board> boardList) {
            super.onPostExecute(boardList);

            // 가져온 데이터를 어댑터에 설정
            mAdapter = new MainAdapter(boardList); // 어댑터 초기화
            mMainRecyclerView.setAdapter(mAdapter); // 어댑터 설정
        }



    private void getNicknameFromCustid(String custid, final VolleyCallback callback) {
            String baseUrl = "http://3.209.169.0/nickname.php";
            String url = baseUrl + "?custid=" + custid;

            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                    Request.Method.GET,
                    url,
                    null,
                    new Response.Listener<JSONObject>() {
                        @Override
                        public void onResponse(JSONObject response) {
                            try {
                                String nickname = response.getString("nickname");
                                callback.onSuccess(nickname); // 닉네임을 콜백으로 전달
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError error) {
                            // 요청이 실패한 경우 에러 처리를 수행합니다.
                            Log.e("Frag4", "Error: " + error.getMessage());
                        }
                    }
            );

            if (getContext() != null) {
                RequestQueue requestQueue = Volley.newRequestQueue(getContext());
                // 나머지 코드
            } else {
                // 컨텍스트가 null인 경우 처리
                Log.e("Frag4", "Context is null");
            }

            // 요청을 큐에 추가
            // 다른 곳에서 정의된 RequestQueue 객체가 있다고 가정합니다.
            RequestQueue requestQueue = Volley.newRequestQueue(getContext()); // RequestQueue 초기화
            requestQueue.add(jsonObjectRequest);
        }
    }


    // fetchDataFromUrl 메서드도 그대로 사용합니다.
    private String fetchDataFromUrl(String urlString) throws IOException {
        HttpURLConnection urlConnection = null;
        BufferedReader reader = null;
        String jsonData = null;

        try {
            URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            InputStream inputStream = urlConnection.getInputStream();
            StringBuilder buffer = new StringBuilder();

            if (inputStream == null) {
                // No data to read
                return null;
            }

            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                buffer.append(line).append("\n");
            }

            if (buffer.length() == 0) {
                // Empty stream
                return null;
            }

            jsonData = buffer.toString();
            Log.d("MyApp", "JSON Data: " + jsonData); // JSON 데이터를 로그에 출력
        } catch (IOException e) {
            Log.e("MyApp", "Error fetching data: " + e.getMessage());
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    Log.e("MyApp", "Error closing stream: " + e.getMessage());
                }
            }
        }
        return jsonData;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag4, container, false);

        mMainRecyclerView = view.findViewById(R.id.main_recycler_view);
        mBoardList = new ArrayList<>(); // 어댑터 초기화 이전에 리스트 초기화

        mAdapter = new MainAdapter(mBoardList); // 어댑터 초기화
        mMainRecyclerView.setAdapter(mAdapter); // 어댑터 설정

        mMainWriteButton = view.findViewById(R.id.main_write_button); // FloatingActionButton 초기화
        mMainWriteButton.setOnClickListener(this);

        // 데이터베이스에서 게시물 데이터를 가져옵니다.
        // 데이터는 AsyncTask에서 가져오므로 여기서 호출할 필요 없음

        // login_id를 받아오는 코드 (Frag4 내에서 필요한 경우)
        Bundle bundle4 = getArguments();
        if (bundle4 != null) {
            login_id = bundle4.getString("login_id");
            Log.d("Frag4", "Received login_id: " + login_id);
        }

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        // AsyncTask를 실행하여 데이터를 다시 가져옵니다.
        new GetDataFromServer().execute("http://3.209.169.0/getJson.php");
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_write_button) {
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            intent.putExtra("login_id", login_id); // 여기서 login_id를 추가
            startActivity(intent);
        }
    }

    private static class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder> {

        private List<Board> mBoardList;

        public MainAdapter(List<Board> mBoardList) {
            this.mBoardList = mBoardList;
        }

        @NonNull
        @Override
        public MainViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new MainViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull MainViewHolder holder, int position) {
            Board data = mBoardList.get(position);
            holder.mTitleTextView.setText(data.getTitle());
            holder.mNameTextView.setText(data.getName());
        }

        @Override
        public int getItemCount() {
            return mBoardList.size();
        }

        class MainViewHolder extends RecyclerView.ViewHolder {

            private final TextView mTitleTextView;
            private final TextView mNameTextView;

            public MainViewHolder(View itemView) {
                super(itemView);

                mTitleTextView = itemView.findViewById(R.id.item_title_text);
                mNameTextView = itemView.findViewById(R.id.item_name_text);
            }
        }
    }
}
