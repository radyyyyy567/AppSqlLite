package com.example.appsqllite;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddKotaActivity extends AppCompatActivity {

    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_kota);

        dbHelper = new MyDatabaseHelper(this);

        EditText kota = findViewById(R.id.edit_kota);
        Button btnSave = findViewById(R.id.btn_save_kota);

        btnSave.setOnClickListener(v -> {
            dbHelper.getWritableDatabase().execSQL("INSERT INTO kota (name) VALUES (?)",
                    new Object[]{kota.getText().toString()});
            Toast.makeText(this, "Kota ditambahkan", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
