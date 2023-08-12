package com.example.registerloginexample;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Surface;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Add extends AppCompatActivity {

    private static final int REQUEST_IMAGE_CAPTURE = 672;

    private EditText refIdEditText;
    private ImageView photoImageView;
    private EditText productNameEditText;
    private EditText quantityEditText;
    private TextView expiryDateText;
    private Button takePhotoButton;
    private Button saveButton;
    private Button expiryDateButton;

    private Uri photoUri;
    private Calendar selectedDate = Calendar.getInstance();
    private int custId; // 로그인 시 받아온 custid 값을 저장

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        refIdEditText = findViewById(R.id.refEditText);
        photoImageView = findViewById(R.id.photoImageView);
        productNameEditText = findViewById(R.id.productNameEditText);
        quantityEditText = findViewById(R.id.quantityEditText);
        expiryDateText = findViewById(R.id.expiryDateText);
        takePhotoButton = findViewById(R.id.takePhotoButton);
        saveButton = findViewById(R.id.saveButton);
        expiryDateButton = findViewById(R.id.expiryDateButton);

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestCameraPermission();
            }
        });

        expiryDateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickerDialog();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveProduct();
            }
        });

        // 로그인 정보 가져오기
        SharedPreferences sharedPreferences = getSharedPreferences("user_info", MODE_PRIVATE);
        custId = sharedPreferences.getInt("cust_id", -1);
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

                    int rotation = getWindowManager().getDefaultDisplay().getRotation();
                    int orientation;
                    switch (rotation) {
                        case Surface.ROTATION_0:
                            orientation = 0;
                            break;
                        case Surface.ROTATION_90:
                            orientation = 90;
                            break;
                        case Surface.ROTATION_180:
                            orientation = 180;
                            break;
                        case Surface.ROTATION_270:
                            orientation = 270;
                            break;
                        default:
                            orientation = 0;
                    }
                    takePictureIntent.putExtra(MediaStore.Images.Media.ORIENTATION, orientation);
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

    private void showDatePickerDialog() {
        DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                selectedDate.set(Calendar.YEAR, year);
                selectedDate.set(Calendar.MONTH, monthOfYear);
                selectedDate.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                // 선택한 날짜를 텍스트 형식으로 표시
                updateExpiryDateEditText();
            }
        };

        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                dateSetListener,
                selectedDate.get(Calendar.YEAR),
                selectedDate.get(Calendar.MONTH),
                selectedDate.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void updateExpiryDateEditText() {
        String myFormat = "yyyy-MM-dd";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.KOREA);
        expiryDateText.setText(sdf.format(selectedDate.getTime()));
    }

    private void saveProduct() {
        String productName = productNameEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String expiryDate = expiryDateText.getText().toString();
        String imagePath = "uploads/temp_photo.jpg";

        // 데이터 유효성 검사
        if (productName.isEmpty() || expiryDate.isEmpty() || quantity.isEmpty()) {
            Toast.makeText(Add.this, "모든 필수 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // 로그인 정보가 없는 경우
        if (custId == -1) {
            showConfirmationDialog(productName, quantity, expiryDate, imagePath);
        } else {
            saveProductToDatabase(custId, productName, quantity, expiryDate, imagePath);
        }
    }

    private void showConfirmationDialog(final String productName, final String quantity, final String expiryDate, final String imagePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인 정보 없음")
                .setMessage("로그인 정보가 없습니다. 제품을 저장하시겠습니까?")
                .setPositiveButton("저장", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        saveProductToDatabase(-1, productName, quantity, expiryDate, imagePath);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private void saveProductToDatabase(int custId, String productName, String quantity, String expiryDate, String imagePath) {
        String url = "http://ruddk658.dothome.co.kr/Add.php";

        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonResponse = new JSONObject(response);
                    boolean success = jsonResponse.getBoolean("success");
                    String message = jsonResponse.getString("message");

                    if (success) {
                        Toast.makeText(Add.this, message, Toast.LENGTH_SHORT).show();
                        finish(); // 액티비티 종료
                    } else {
                        Toast.makeText(Add.this, "제품 저장에 실패했습니다.", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        };

        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(Add.this, "서버와 통신 중 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                error.printStackTrace();
            }
        };

        // 서버로 데이터 전송을 위한 요청 객체 생성
        AddRequest addRequest = new AddRequest(custId, productName, quantity, expiryDate, imagePath, responseListener, errorListener);

        // Volley 요청 큐에 요청 추가
        RequestQueue requestQueue = Volley.newRequestQueue(Add.this);
        requestQueue.add(addRequest);
    }
}
