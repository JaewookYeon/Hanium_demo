package com.example.registerloginexample;

import android.content.Intent;
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

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class Frag4 extends Fragment implements View.OnClickListener {

    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private List<Board> mBoardList;
    private FloatingActionButton mMainWriteButton; // FloatingActionButton 변수 추가

    private String login_id; // Frag4 내에서 사용하는 변수
    // 데이터베이스에서 게시물 데이터를 가져오는 메서드
    private void loadBoardDataFromDatabase() {
        Log.d("MyApp", "Attempting to load data from the database."); // 로그 추가
        // 데이터베이스에서 게시물 데이터를 가져옵니다.
        List<Board> boardData = getBoardDataFromDatabase();

        // 가져온 데이터를 mBoardList에 추가합니다.
        mBoardList.clear(); // 기존 데이터를 모두 제거합니다.
        mBoardList.addAll(boardData); // 새로운 데이터를 mBoardList에 추가합니다.
        mAdapter.notifyDataSetChanged(); // 어댑터에 데이터 변경 알림
    }


    private List<Board> getBoardDataFromDatabase() {
        Connection connection = null;
        List<Board> boardList = new ArrayList<>();
        try {
            // 데이터베이스 연결 설정
            String url = "jdbc:mysql://localhost:3307/hanium_api"; // 데이터베이스 URL
            String username = "hanium";
            String password = "1234";

            // JDBC 드라이버 로드
            Class.forName("com.mysql.cj.jdbc.Driver");

            // 데이터베이스 연결
            connection = DriverManager.getConnection(url, username, password);

            // SQL 쿼리 실행
            String sql = "SELECT * FROM BOARD";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(sql);

            // 결과를 Board 객체로 변환하여 리스트에 추가
            while (resultSet.next()) {
                String id = resultSet.getString("id");
                String title = resultSet.getString("b_title");
                String content = resultSet.getString("b_content");
                String name = resultSet.getString("custid");

                Board board = new Board(id, title, content, name);
                boardList.add(board);
            }

            // 리소스 해제
            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Log.e("MyApp", "SQL Exception1: " + e.getMessage()); // SQL 예외 메시지 출력
            return null;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            Log.e("MyApp", "Class Not Found Exception: " + e.getMessage()); // 클래스를 찾을 수 없는 예외 메시지 출력
        } finally {
            // 데이터베이스 연결 닫기
            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return boardList;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag4, container, false);

        mMainRecyclerView = view.findViewById(R.id.main_recycler_view);
        mMainWriteButton = view.findViewById(R.id.main_write_button); // FloatingActionButton 초기화
        mMainWriteButton.setOnClickListener(this);

        mBoardList = new ArrayList<>();

        // 데이터베이스에서 게시물 데이터를 가져옵니다.
        loadBoardDataFromDatabase();

        mAdapter = new MainAdapter(mBoardList);
        mMainRecyclerView.setAdapter(mAdapter);

        // login_id를 받아오는 코드 (Frag4 내에서 필요한 경우)
        Bundle bundle4 = getArguments();
        if (bundle4 != null) {
            Connection connection = null;
            try{
                String url = "jdbc:mysql://localhost:3306/hanium_api"; // 데이터베이스 URL
                String username = "hanium";
                String password = "1234";

                // JDBC 드라이버 로드
                Class.forName("com.mysql.cj.jdbc.Driver");

                // 데이터베이스 연결
                connection = DriverManager.getConnection(url, username, password);
            }catch(SQLException e){
                Log.e("MyApp", "SQL Exception2: " + e.getMessage());
                return null;
            } catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            login_id = bundle4.getString("login_id");
            Log.d("Frag4", "Received login_id: " + login_id);

            if (connection != null) {
                try {
                    connection.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }

        return view;
    }


    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.main_write_button) {
            Intent intent = new Intent(getActivity(), WriteActivity.class);
            intent.putExtra("login_id", login_id); // 여기서 login_id를 추가
            startActivity(intent);
        }
    }

    private static class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

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

        class MainViewHolder extends RecyclerView.ViewHolder{

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
