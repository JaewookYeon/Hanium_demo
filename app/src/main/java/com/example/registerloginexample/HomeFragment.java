package com.example.registerloginexample;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

public class HomeFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedinstanceState){
        return inflater.inflate(R.layout.fragment_home,container,false);
    }
}