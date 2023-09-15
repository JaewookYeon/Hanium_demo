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

import java.util.ArrayList;
import java.util.List;

public class Frag4 extends Fragment implements View.OnClickListener {

    private RecyclerView mMainRecyclerView;
    private MainAdapter mAdapter;
    private List<Board> mBoardList;
    private FloatingActionButton mMainWriteButton; // FloatingActionButton 변수 추가

    private String login_id; // Frag4 내에서 사용하는 변수

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.frag4, container, false);

        mMainRecyclerView = view.findViewById(R.id.main_recycler_view);
        mMainWriteButton = view.findViewById(R.id.main_write_button); // FloatingActionButton 초기화
        mMainWriteButton.setOnClickListener(this);

        mBoardList = new ArrayList<>();
        mBoardList.add(new Board(null,"반갑습니다 여러분", null,"android"));
        mBoardList.add(new Board(null,"안녕", null,"Server"));
        mBoardList.add(new Board(null,"Hello", null,"Java"));
        mBoardList.add(new Board(null,"Hi", null,"php"));
        mBoardList.add(new Board(null,"ㅋㅋ", null,"Python"));

        mAdapter = new MainAdapter(mBoardList);
        mMainRecyclerView.setAdapter(mAdapter);

        // login_id를 받아오는 코드 (Frag4 내에서 필요한 경우)
        Bundle bundle = getArguments();
        if (bundle != null) {
            login_id = bundle.getString("login_id");
            Log.d("Frag4", "Received login_id: " + login_id);
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
