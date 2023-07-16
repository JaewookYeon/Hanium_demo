package com.example.registerloginexample;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Add extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;

    private ImageView photoImageView;
    private EditText productNameEditText;
    private EditText quantityEditText;
    private EditText expiryDateEditText;
    private Button takePhotoButton;
    private Button saveButton;

    private Uri photoUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        photoImageView = findViewById(R.id.photoImageView);
        productNameEditText = findViewById(R.id.productNameEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        expiryDateEditText = findViewById(R.id.expiryDateEditText);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        saveButton = findViewById(R.id.saveButton);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });
    }


    private void requestCameraPermission() {
        PermissionListener permissionListener = new PermissionListener() {
            @Override
            public void onPermissionGranted() {
                dispatchTakePictureIntent();
            }

            @Override
            public void onPermissionDenied(List<String> deniedPermissions) {
                Toast.makeText(Add.this, "카메라 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
            }
        };

        TedPermission.with(Add.this)
                .setPermissionListener(permissionListener)
                .setPermissions(Manifest.permission.CAMERA)
                .check();
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            try {
                photoUri = createImageUri();
                if (photoUri != null) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(this, "이미지 파일을 생성하는 동안 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private Uri createImageUri() {
        Uri uri = null;
        try {
            String fileName = "temp_photo.jpg";
            File imagePath = new File(getExternalCacheDir(), fileName);
            uri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imagePath);
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "이미지 파일을 생성하는 동안 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
        }
        return uri;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            if (photoUri != null) {
                setProductImage();
            } else {
                Toast.makeText(this, "이미지 파일이 없습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setProductImage() {
        if (photoUri != null) {
            try {
                Bitmap sourceBitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(photoUri));
                int targetWidth = photoImageView.getWidth();
                int targetHeight = photoImageView.getHeight();

                int sourceWidth = sourceBitmap.getWidth();
                int sourceHeight = sourceBitmap.getHeight();

                float scaleWidth = (float) targetWidth / sourceWidth;
                float scaleHeight = (float) targetHeight / sourceHeight;

                // 이미지의 가로 세로 비율을 유지하면서 크기 조절
                float scaleFactor = Math.min(scaleWidth, scaleHeight);
                int finalWidth = (int) (sourceWidth * scaleFactor);
                int finalHeight = (int) (sourceHeight * scaleFactor);

                Bitmap resizedBitmap = Bitmap.createScaledBitmap(sourceBitmap, finalWidth, finalHeight, true);
                photoImageView.setImageBitmap(resizedBitmap);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "이미지를 처리하는 동안 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void saveProduct() {
        String productName = productNameEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String expiryDate = expiryDateEditText.getText().toString();

        Bundle bundle = new Bundle();
        bundle.putString("productName", productName);
        bundle.putString("quantity", quantity);
        bundle.putString("expiryDate", expiryDate);
        bundle.putParcelable("photoUri", photoUri);

        Intent intent = new Intent();
        intent.putExtras(bundle);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

}
