package com.example.registerloginexample;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Frag1 extends Fragment {

    private View view;
    private ImageButton lockerButton;
    private FloatingActionButton DescripButton;

    private String login_id;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        Bundle bundle1 = getArguments();
        if (bundle1 != null) {
            login_id = bundle1.getString("login_id");
            Log.d("Frag1", "Received login_id: " + login_id);
        }

        view = inflater.inflate(R.layout.frag1, container, false);
        lockerButton = view.findViewById(R.id.lockerButton);
        DescripButton = view.findViewById(R.id.DescripButton);

        lockerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), lockerActivity.class);
                startActivity(intent);
            }
        });

        DescripButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), lockerDesActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }
}
