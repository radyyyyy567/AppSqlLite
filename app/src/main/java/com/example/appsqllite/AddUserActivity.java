package com.example.appsqllite;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class AddUserActivity extends AppCompatActivity {

    MyDatabaseHelper dbHelper;
    private void loadSpinnerData(SQLiteDatabase db, Spinner spinner, String tableName, List<SimpleItem> list) {
        list.clear();
        Cursor cursor = db.rawQuery("SELECT id, name FROM " + tableName, null);
        while (cursor.moveToNext()) {
            list.add(new SimpleItem(cursor.getInt(0), cursor.getString(1)));
        }
        cursor.close();
        ArrayAdapter<SimpleItem> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user);

        dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        EditText name = findViewById(R.id.edit_name);
        EditText jurusan = findViewById(R.id.edit_jurusan);
        EditText namaAyah = findViewById(R.id.edit_nama_ayah);
        EditText namaIbu = findViewById(R.id.edit_nama_ibu);
        EditText tanggalLahir = findViewById(R.id.edit_tanggal_lahir);

        Spinner spinnerTinggal = findViewById(R.id.spinner_tempat_tinggal);
        Spinner spinnerLahir = findViewById(R.id.spinner_tempat_lahir);
        Spinner spinnerHobby = findViewById(R.id.spinner_hobby);

        List<SimpleItem> kotaList = new ArrayList<>();
        List<SimpleItem> hobbyList = new ArrayList<>();

        loadSpinnerData(db, spinnerTinggal, "kota", kotaList);
        loadSpinnerData(db, spinnerLahir, "kota", kotaList);
        loadSpinnerData(db, spinnerHobby, "hobby", hobbyList);


        Button btnSave = findViewById(R.id.btn_save_user);

        btnSave.setOnClickListener(v -> {
            try {
                // 1. Insert user first
                db.execSQL("INSERT INTO user (name, jurusan, nama_ayah, nama_ibu, tanggal_lahir) VALUES (?, ?, ?, ?, ?)",
                        new Object[]{
                                name.getText().toString(),
                                jurusan.getText().toString(),
                                namaAyah.getText().toString(),
                                namaIbu.getText().toString(),
                                tanggalLahir.getText().toString()
                        });

                // 2. Get the last inserted user ID
                Cursor cursor = db.rawQuery("SELECT last_insert_rowid()", null);
                int userId = -1;
                if (cursor.moveToFirst()) {
                    userId = cursor.getInt(0);
                }
                cursor.close();

                SimpleItem selectedTinggal = (SimpleItem) spinnerTinggal.getSelectedItem();
                SimpleItem selectedLahir = (SimpleItem) spinnerLahir.getSelectedItem();
                SimpleItem selectedHobby = (SimpleItem) spinnerHobby.getSelectedItem();

                if (userId != -1) {
                    // 3. Insert into user_data
                    db.execSQL("INSERT INTO user_data (user_id, tempat_tinggal, tempat_lahir, hobby_id) VALUES (?, ?, ?, ?)",
                            new Object[]{
                                    userId,
                                    selectedTinggal.id,
                                    selectedLahir.id,
                                    selectedHobby.id
                            });

                    Toast.makeText(this, "User dan User Data ditambahkan", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(this, "Gagal mendapatkan user ID", Toast.LENGTH_SHORT).show();
                }

                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
