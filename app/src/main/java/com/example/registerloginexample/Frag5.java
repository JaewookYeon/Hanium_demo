package com.example.registerloginexample;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;

import de.hdodenhof.circleimageview.CircleImageView;

public class Frag5 extends Fragment {

    private Button buttonEditProfile;
    private Button buttonContact;
    private Button buttonLogout;
    private Button buttonEditProfile_right;
    private Button buttonContact_right;
    private Button buttonLogout_right;

    private String login_id;

    private static final int PICK_IMAGE_REQUEST_CODE = 123; // 임의의 숫자로 설정
    private CircleImageView profileImageView;
    private Uri selectedImageUri; // 선택한 이미지의 Uri 저장
    private ImageView icBoy;
    private ImageView icGirl;
    private ImageView selectedImageView = null; // 현재 선택된 이미지 뷰를 추적


    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.frag5, container, false);


        LinearLayout layoutEditUser = view.findViewById(R.id.layout_editUser);
        buttonEditProfile = layoutEditUser.findViewById(R.id.my_editUser);
        buttonEditProfile_right = layoutEditUser.findViewById(R.id.editUser_right);
        LinearLayout layoutContact = view.findViewById(R.id.layout_contact);
        buttonContact = layoutContact.findViewById(R.id.my_contact);
        buttonContact_right = layoutContact.findViewById(R.id.my_contact_right);
        LinearLayout layoutLogout = view.findViewById(R.id.layout_logout);
        buttonLogout = layoutLogout.findViewById(R.id.my_logout);
        buttonLogout_right = layoutLogout.findViewById(R.id.my_logout_right);


        // 프로필 이미지 아이콘 클릭 시 동작
        profileImageView = view.findViewById(R.id.profileImageView);


        // 프로필 이미지 아이콘 클릭 시 동작
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("프로필 사진 변경");
                CharSequence[] options = new CharSequence[]{"앨범에서 선택", "기본이미지"};
                builder.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                openGallery();
                                break;
                            case 1:
                                setDefaultProfileImage(); // 기본 이미지 설정을 여기에 구현
                                break;
                        }
                    }
                });

                // 취소 버튼 추가
                builder.setNegativeButton("취소", null);

                builder.show();
            }
        });

        Bundle bundle = getArguments();
        if (bundle != null) {
            login_id = bundle.getString("login_id");
            Log.d("Frag5", "Received login_id: " + login_id);
        }


        buttonEditProfile_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerifyIdentityActivity.class);
                intent.putExtra("login_id", login_id); // 여기서 login_id를 추가
                startActivity(intent);
            }
        });
        buttonEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), VerifyIdentityActivity.class);
                intent.putExtra("login_id", login_id); // 여기서 login_id를 추가
                startActivity(intent);
            }
        });
        buttonContact_right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.putExtra("login_id", login_id); // 여기서 login_id를 추가
                startActivity(intent);
            }
        });
        buttonContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getActivity(), ContactActivity.class);
                intent.putExtra("login_id", login_id); // 여기서 login_id를 추가
                startActivity(intent);
            }
        });
        buttonLogout_right.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LogoutActivity.class);
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

        // 서버에서 사용자 정보 가져오기
        String url = "http://3.209.169.0/frag5.php?login_id=" + login_id;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, url, null,
                response -> {
                    try {
                        String userName = response.getString("name");
                        TextView nameTextView = getView().findViewById(R.id.nameTextView);
                        nameTextView.setText(userName);

                        // 사용자의 프로필 이미지 정보 가져오기
                        String userPhoto = response.getString("photo");
                        Log.d("Frag5", "User photo: " + userPhoto);

                        if (userPhoto == null || userPhoto.equalsIgnoreCase("null") || userPhoto.isEmpty()) {
                            profileImageView.setImageResource(R.drawable.ic_boy);
                        } else {
                            // userPhoto 값이 존재하거나 빈 문자열이 아닌 경우에 다른 이미지를 설정하거나 처리할 수 있음
                        }

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
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST_CODE && resultCode == Activity.RESULT_OK && data != null) {
            selectedImageUri = data.getData();

            // AlertDialog의 커스텀 레이아웃을 inflate하여 사용
            LayoutInflater inflater = requireActivity().getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.custom_alert_dialog, null);

            // 커스텀 레이아웃 내부의 ImageView를 찾아서 이미지 설정
            ImageView imageView = dialogView.findViewById(R.id.alertImageView);
            imageView.setImageURI(selectedImageUri);

            // AlertDialog 생성 및 설정
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setView(dialogView);

            // "확인" 버튼에 클릭 리스너 설정
            Button selectButton = dialogView.findViewById(R.id.selectButton);
            AlertDialog dialog = builder.create(); // AlertDialog 생성
            selectButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 설정 버튼 클릭 시 동작 구현
                    applySelectedImage();
                    dialog.dismiss();
                }
            });

            // "취소" 버튼에 클릭 리스너 설정
            Button cancelButton = dialogView.findViewById(R.id.cancelButton);
            cancelButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 취소 버튼 클릭 시 사진 선택창으로 돌아감
                    openGallery();
                    dialog.dismiss();
                }
            });

            dialog.show(); // AlertDialog 보이기
        }
    }

    // 앨범에서 이미지 선택
    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryIntent.setType("image/*"); // 이미지 파일만 필터링
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE);
    }
    private void applySelectedImage() {
        // 선택한 이미지를 프로필 이미지뷰에 표시
        profileImageView.setImageURI(selectedImageUri);
    }
    private void setDefaultProfileImage() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("기본 이미지 선택");

        LayoutInflater inflater = requireActivity().getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_image_dialog, null);
        builder.setView(dialogView);

        icBoy = dialogView.findViewById(R.id.icBoy);
        icGirl = dialogView.findViewById(R.id.icGirl);
        icBoy.setTag(icBoy.getBackground());
        icGirl.setTag(icGirl.getBackground());

        // AlertDialog 인스턴스 생성
        AlertDialog dialog = builder.create();

        // "선택" 및 "취소" 버튼에 클릭 리스너 설정
        Button selectButton = dialogView.findViewById(R.id.selectButton);
        Button cancelButton = dialogView.findViewById(R.id.cancelButton);

        // 이미지뷰 클릭 리스너 설정
        icBoy.setOnClickListener(v -> handleImageSelection(icBoy, R.drawable.ic_boy));
        icGirl.setOnClickListener(v -> handleImageSelection(icGirl, R.drawable.ic_girl));

        selectButton.setOnClickListener(v -> {
            // "선택" 버튼 클릭 시 선택한 이미지를 프로필 이미지로 설정하고 다이얼로그 닫기
            if (selectedImageView != null) {
                applySelectedImageWithBorder(selectedImageView);
            }
            dialog.dismiss();
        });

        cancelButton.setOnClickListener(v -> {
            // "취소" 버튼 클릭 시 다이얼로그 닫기
            dialog.dismiss();
        });

        // AlertDialog 보이기
        dialog.show();
    }
    private void handleImageSelection(ImageView imageView, int imageResource) {
        if (selectedImageView != null) {
            Drawable prevBackground = (Drawable) selectedImageView.getTag();
            selectedImageView.setBackground(prevBackground); // 이전 이미지의 테두리 재설정
        }

        if (imageView != selectedImageView) {
            if (selectedImageView != null) {
                selectedImageView.setBackgroundResource(0); // 선택 해제되는 경우 테두리 제거
            }
            selectedImageView = imageView;
            selectedImageView.setBackgroundResource(R.drawable.selected_image_border); // 선택한 이미지에 테두리 추가
        } else {
            selectedImageView = null;
        }

        if (imageView == icBoy) {
            icGirl.setBackgroundResource(R.drawable.gray_border); // 이미지2의 테두리를 원래가진 테두리로 변경
        } else if (imageView == icGirl) {
            icBoy.setBackgroundResource(R.drawable.gray_border); // 이미지1의 테두리를 원래가진 테두리로 변경
        }
    }
    private void applySelectedImageWithBorder(ImageView imageView) {
        profileImageView.setImageDrawable(imageView.getDrawable()); // 선택한 이미지를 프로필 이미지뷰에 표시

        // 선택한 이미지뷰의 테두리 제거
        imageView.setBackgroundResource(0);
    }
}