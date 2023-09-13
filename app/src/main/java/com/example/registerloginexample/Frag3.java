package com.example.registerloginexample;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toolbar;
import android.widget.ViewFlipper;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Frag3 extends Fragment {


    private TextView announcementText;
    private View view;
    ViewFlipper v_fllipper;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.frag3, container, false);

        announcementText = view.findViewById(R.id.announcementText);
        announcementText.postDelayed(new Runnable() {
            @Override
            public void run() {
                announcementText.setSelected(true);
            }
        }, 1000);
        announcementText.setHorizontallyScrolling(true);
        announcementText.setMovementMethod(new ScrollingMovementMethod());

        int images[] = {
                R.drawable.test_img01,
                R.drawable.test_img02,
                R.drawable.test_img03
        };

        v_fllipper = view.findViewById(R.id.image_slide);

        for (int image : images) {
            fllipperImages(image);
        }

        return view;
    }

    // 이미지 슬라이더 구현 메서드
    public void fllipperImages(int image) {
        ImageView imageView = new ImageView(getContext());
        imageView.setBackgroundResource(image);

        v_fllipper.addView(imageView);      // 이미지 추가
        v_fllipper.setFlipInterval(4000);       // 자동 이미지 슬라이드 딜레이시간(1000 당 1초)
        v_fllipper.setAutoStart(true);          // 자동 시작 유무 설정

        // animation
        v_fllipper.setInAnimation(getContext(), android.R.anim.slide_in_left);
        v_fllipper.setOutAnimation(getContext(), android.R.anim.slide_out_right);
    }
}
