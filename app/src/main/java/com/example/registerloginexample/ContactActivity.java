package com.example.registerloginexample;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.registerloginexample.R;

public class ContactActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner spinnerType;
    private EditText editTextContent;
    private Button buttonSubmit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);

        spinnerType = findViewById(R.id.spinnerType);
        editTextContent = findViewById(R.id.editTextContent);
        buttonSubmit = findViewById(R.id.buttonSubmit);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.contact_types, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerType.setAdapter(adapter);
        spinnerType.setOnItemSelectedListener(this);

        buttonSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedType = spinnerType.getSelectedItem().toString();
                String content = editTextContent.getText().toString();

                Toast.makeText(ContactActivity.this, "문의가 제출되었습니다.", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
}
