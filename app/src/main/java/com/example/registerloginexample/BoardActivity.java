package com.example.registerloginexample;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class BoardActivity extends AppCompatActivity implements View.OnClickListener {

    private RecyclerView mMainRecyclerView;

    private MainAdapter mAdapter;
    private List<Board> mBoardList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_board);

        mMainRecyclerView = findViewById(R.id.main_recycler_view);

        findViewById(R.id.main_write_button).setOnClickListener(this);

        mBoardList = new ArrayList<>();
        mBoardList.add(new Board(null,"반갑습니다 여러분", null,"android"));
        mBoardList.add(new Board(null,"안녕", null,"Server"));
        mBoardList.add(new Board(null,"Hello", null,"Java"));
        mBoardList.add(new Board(null,"Hi", null,"php"));
        mBoardList.add(new Board(null,"ㅋㅋ", null,"Python"));

        mAdapter= new MainAdapter(mBoardList);
        mMainRecyclerView.setAdapter(mAdapter);

    }

    @Override
    public void onClick(View v){
        startActivity(new Intent(this, WriteActivity.class));
    }

    private class MainAdapter extends RecyclerView.Adapter<MainAdapter.MainViewHolder>{

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

            private TextView mTitleTextView;
            private TextView mNameTextView;
            public MainViewHolder(View itemView) {
                super(itemView);

                mTitleTextView = itemView.findViewById(R.id.item_title_text);
                mNameTextView = itemView.findViewById(R.id.item_name_text);
            }
        }
    }
}