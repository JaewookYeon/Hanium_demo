package com.example.registerloginexample;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
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
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
    private String imagePath;
    private Uri photoUri;
    private Calendar selectedDate = Calendar.getInstance();
    private int custId;
    private String login_id = "";

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

        // custId를 받아오고 AddActivity를 시작합니다.
        getCustidAndStartAddActivity();

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
    }

    // getCustidAndStartAddActivity 메서드 추가
    private void getCustidAndStartAddActivity() {
        String baseUrl = "http://3.209.169.0/custid.php";
        String url = baseUrl + "?login_id=" + login_id;

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            custId = response.getInt("custid");

                            // 이제 custId를 사용하여 원하는 작업을 수행할 수 있습니다.
                            // AddActivity를 시작합니다.
                            Intent intent = new Intent(Add.this, AddRequest.class);
                            intent.putExtra("custId", custId);
                            startActivity(intent);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // 요청이 실패한 경우 에러 처리를 수행합니다.
                        Log.e("AddActivity", "Error: " + error.getMessage());
                    }
                }
        );

        Volley.newRequestQueue(this).add(jsonObjectRequest);
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
                String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
                String imageFileName = "JPEG_" + timeStamp + ".jpg";
                imagePath = imageFileName;

                File imagePathFile = new File(getExternalCacheDir(), imageFileName);
                photoUri = FileProvider.getUriForFile(this, getPackageName() + ".fileprovider", imagePathFile);

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
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String endDate = sdf.format(selectedDate.getTime());
        expiryDateText.setText(endDate);
    }

    private void saveProduct() {
        String refIdString = refIdEditText.getText().toString();

        if (refIdString.isEmpty()) {
            // refId가 입력되지 않은 경우 사용자에게 메시지를 표시하거나 다른 처리를 할 수 있음
            Toast.makeText(Add.this, "refId를 입력하세요.", Toast.LENGTH_SHORT).show();
            return;
        }

        int refId = Integer.parseInt(refIdString);
        String productName = productNameEditText.getText().toString();
        String quantity = quantityEditText.getText().toString();
        String end_date = expiryDateText.getText().toString();

        if (productName.isEmpty() || end_date.isEmpty() || quantity.isEmpty() || !isValidImagePath(imagePath)) {
            Toast.makeText(Add.this, "모든 필수 항목을 입력해주세요", Toast.LENGTH_SHORT).show();
            return;
        }

        // GET 요청을 보내는 코드 추가
        sendGetRequest(custId, refId, productName, quantity, end_date, new File(imagePath));
    }

    private boolean isValidImagePath(String path) {
        String[] validExtensions = {".jpg", ".jpeg", ".png"};
        for (String extension : validExtensions) {
            if (path.toLowerCase().endsWith(extension)) {
                return true;
            }
        }
        return false;
    }

    private void showLoginRequiredDialog(final String productName, final String quantity, final String expiryDate, final String imagePath) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("로그인이 필요합니다")
                .setMessage("제품을 저장하려면 로그인이 필요합니다. 로그인 페이지로 이동하시겠습니까?")
                .setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(Add.this, LoginActivity.class);
                        startActivity(intent);
                    }
                })
                .setNegativeButton("취소", null)
                .show();
    }

    private String convertImageFileToBase64(File imageFile) {
        Bitmap bitmap = BitmapFactory.decodeFile(imageFile.getAbsolutePath());
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 70, byteArrayOutputStream);
        byte[] imageBytes = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(imageBytes, Base64.DEFAULT);
    }

    private void sendGetRequest(int custId, int refId, String productName, String quantity, String expiryDate, File imagePath) {
        // URL과 파라미터를 조합하여 완전한 GET 요청 URL을 생성합니다.
        String baseUrl = "http://3.209.169.0/Add.php";

        // URL에 파라미터를 붙여서 GET 요청을 보냅니다.
        String url = baseUrl
                + "?fk_food_custid=" + custId
                + "&f_name=" + productName
                + "&end_date=" + expiryDate
                + "&ref_id=" + refId
                + "&f_count=" + quantity
                + "&f_image=" + imagePath;

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.GET,
                url,
                null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        // 서버 응답을 처리합니다.
                        try {
                            boolean success = response.getBoolean("success");
                            String message = response.getString("message");

                            if (success) {
                                Toast.makeText(Add.this, "상품이 저장되었습니다.", Toast.LENGTH_SHORT).show();
                                finish();
                            } else {
                                Toast.makeText(Add.this, "상품 저장에 실패하였습니다. " + message, Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Add.this, "서버 응답을 처리하는 동안 오류가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Add.this, "서버 응답을 처리하는 동안 에러가 발생했습니다.", Toast.LENGTH_SHORT).show();
                        error.printStackTrace();
                    }
                }
        );

        Volley.newRequestQueue(this).add(request);
    }
}
