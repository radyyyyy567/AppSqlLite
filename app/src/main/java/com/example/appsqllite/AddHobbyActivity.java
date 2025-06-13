package com.example.appsqllite;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AddHobbyActivity extends AppCompatActivity {

    MyDatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_hobby);

        dbHelper = new MyDatabaseHelper(this);

        EditText hobby = findViewById(R.id.edit_hobby);
        Button btnSave = findViewById(R.id.btn_save_hobby);

        btnSave.setOnClickListener(v -> {
            dbHelper.getWritableDatabase().execSQL("INSERT INTO hobby (name) VALUES (?)",
                    new Object[]{hobby.getText().toString()});
            Toast.makeText(this, "Hobi ditambahkan", Toast.LENGTH_SHORT).show();
            finish();
        });
    }
}
