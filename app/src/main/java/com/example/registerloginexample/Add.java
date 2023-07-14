package com.example.registerloginexample;

import android.Manifest;
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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import java.io.File;
import java.io.IOException;
import java.util.List;

public class Add extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 1;

    private ImageView photoImageView;
    private EditText productNameEditText;
    private EditText quantityEditText;
    private EditText expiryDateEditText;
    private Button takePhotoButton;
    private Button btn_save;

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
        btn_save = findViewById(R.id.saveButton);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String productName = productNameEditText.getText().toString();
                String quantity = quantityEditText.getText().toString();
                String expiryDate = expiryDateEditText.getText().toString();

                Bundle bundle = new Bundle();
                bundle.putString("productName", productName);
                bundle.putString("quantity", quantity);
                bundle.putString("expiryDate", expiryDate);
                bundle.putParcelable("photoUri", photoUri);

                Frag2 frag2 = new Frag2();
                frag2.setArguments(bundle);

                FragmentManager fragmentManager = getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.fragment_container, frag2);
                fragmentTransaction.commit();
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
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            if (photoUri != null) {
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri);
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
            }
        }
    }

    private Uri createImageUri() throws IOException {
        String fileName = "temp_photo.jpg";
        return Uri.parse(getExternalCacheDir().getPath() + "/" + fileName);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            // 이미지 파일의 경로를 가져옵니다.
            String imagePath = photoUri.getPath();

            // 이미지 파일을 가져와서 photoImageView에 설정합니다.
            setProductImage(new File(imagePath));
        }
    }

    private void setProductImage(File imageFile) {
        if (imageFile != null && imageFile.exists()) {
            Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
            photoImageView.setImageBitmap(bitmap);
        }
    }
}
