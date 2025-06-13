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

public class UpdateUserDataActivity extends AppCompatActivity {

    MyDatabaseHelper dbHelper;
    int userDataId;
    int userId;
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

    private void setSpinnerSelectionById(Spinner spinner, List<SimpleItem> list, int id) {
        for (int i = 0; i < list.size(); i++) {
            if (list.get(i).id == id) {
                spinner.setSelection(i);
                return;
            }
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user_data);

        dbHelper = new MyDatabaseHelper(this);
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        // Get UI components
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

        Button btnSave = findViewById(R.id.btn_save_update);

        userDataId = getIntent().getIntExtra("userDataId", -1);
        if (userDataId == -1) {
            Toast.makeText(this, "ID tidak valid", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        // Load existing data from DB
        Cursor cursor = db.rawQuery("SELECT ud.user_id, u.name, u.jurusan, u.nama_ayah, u.nama_ibu, u.tanggal_lahir, ud.tempat_tinggal, ud.tempat_lahir, ud.hobby_id " +
                "FROM user_data ud JOIN user u ON ud.user_id = u.id WHERE ud.id = ?", new String[]{String.valueOf(userDataId)});

        if (cursor.moveToFirst()) {
            userId = cursor.getInt(0);

            name.setText(cursor.getString(1));
            jurusan.setText(cursor.getString(2));
            namaAyah.setText(cursor.getString(3));
            namaIbu.setText(cursor.getString(4));
            tanggalLahir.setText(cursor.getString(5));

            int tinggalId = cursor.getInt(6);
            int lahirId = cursor.getInt(7);
            int hobbyIdValue = cursor.getInt(8);

            // Set spinners
            setSpinnerSelectionById(spinnerTinggal, kotaList, tinggalId);
            setSpinnerSelectionById(spinnerLahir, kotaList, lahirId);
            setSpinnerSelectionById(spinnerHobby, hobbyList, hobbyIdValue);

        } else {
            Toast.makeText(this, "Data tidak ditemukan", Toast.LENGTH_SHORT).show();
            cursor.close();
            finish();
            return;
        }
        cursor.close();

        // Save button action
        btnSave.setOnClickListener(v -> {
            try {
                SQLiteDatabase writableDb = dbHelper.getWritableDatabase();

                // Update user table
                writableDb.execSQL("UPDATE user SET name = ?, jurusan = ?, nama_ayah = ?, nama_ibu = ?, tanggal_lahir = ? WHERE id = ?",
                        new Object[]{
                                name.getText().toString(),
                                jurusan.getText().toString(),
                                namaAyah.getText().toString(),
                                namaIbu.getText().toString(),
                                tanggalLahir.getText().toString(),
                                userId
                        });


                SimpleItem selectedTinggal = (SimpleItem) spinnerTinggal.getSelectedItem();
                SimpleItem selectedLahir = (SimpleItem) spinnerLahir.getSelectedItem();
                SimpleItem selectedHobby = (SimpleItem) spinnerHobby.getSelectedItem();

                // Update user_data table
                writableDb.execSQL("UPDATE user_data SET tempat_tinggal = ?, tempat_lahir = ?, hobby_id = ? WHERE id = ?",
                        new Object[]{
                                selectedTinggal.id,
                                selectedLahir.id,
                                selectedHobby.id,
                                userDataId
                        });

                Toast.makeText(this, "Data berhasil diperbarui", Toast.LENGTH_SHORT).show();
                finish();

            } catch (Exception e) {
                Toast.makeText(this, "Gagal: " + e.getMessage(), Toast.LENGTH_LONG).show();
            }
        });
    }
}
