package com.example.appsqllite;

import android.content.Intent;
import android.os.Bundle;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private MyDatabaseHelper dbHelper;
    private SQLiteDatabase db;
    private ListView listView;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> userDataList;
    private int selectedUserDataId = -1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize DB
        dbHelper = new MyDatabaseHelper(this);
        db = dbHelper.getWritableDatabase();

        // Setup Views


        listView = findViewById(R.id.list_user_data);
        Button btnAddUser = findViewById(R.id.btn_add_user);
        Button btnAddKota = findViewById(R.id.btn_add_kota);
        Button btnAddHobby = findViewById(R.id.btn_add_hobby);
        Button btnUpdateData = findViewById(R.id.btn_update_data);
        Button btnDeleteData = findViewById(R.id.btn_delete_data);

        // Load Data into ListView
        loadUserData();

        // Button listeners (implement later)
        btnAddUser.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddUserActivity.class));
        });

        btnAddKota.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddKotaActivity.class));
        });

        btnAddHobby.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AddHobbyActivity.class));
        });

        btnUpdateData.setOnClickListener(v -> {
            if (selectedUserDataId == -1) {
                Toast.makeText(this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(MainActivity.this, UpdateUserDataActivity.class);
            intent.putExtra("userDataId", selectedUserDataId);
            startActivity(intent);
        });

        btnDeleteData.setOnClickListener(v -> {
            if (selectedUserDataId == -1) {
                Toast.makeText(this, "Pilih data terlebih dahulu", Toast.LENGTH_SHORT).show();
                return;
            }

            db.execSQL("DELETE FROM user_data WHERE id = ?", new Object[]{selectedUserDataId});
            Toast.makeText(this, "Data berhasil dihapus", Toast.LENGTH_SHORT).show();
            selectedUserDataId = -1;
            loadUserData();
        });

        listView.setOnItemClickListener((parent, view, position, id) -> {
            // Extract the ID from the first line of the item string
            String line = userDataList.get(position);
            String idLine = line.split("\n")[0]; // e.g., "ID: 4"
            selectedUserDataId = Integer.parseInt(idLine.replace("ID: ", "").trim());
            Toast.makeText(this, "Selected ID: " + selectedUserDataId, Toast.LENGTH_SHORT).show();
        });
    }

    private void loadUserData() {
        userDataList = new ArrayList<>();
        selectedUserDataId = -1;
        String query = "SELECT ud.id, u.name, u.nama_ayah, u.nama_ibu, u.tanggal_lahir, u.jurusan,   k1.name AS tempat_tinggal, k2.name AS tempat_lahir, h.name AS hobby " +
                "FROM user_data ud " +
                "JOIN user u ON ud.user_id = u.id " +
                "JOIN kota k1 ON ud.tempat_tinggal = k1.id " +
                "JOIN kota k2 ON ud.tempat_lahir = k2.id " +
                "JOIN hobby h ON ud.hobby_id = h.id";

        Cursor cursor = db.rawQuery(query, null);

        if (cursor.moveToFirst()) {
            do {
                String item = "ID: " + cursor.getInt(0)
                        + "\nNama: " + cursor.getString(1)
                        + "\nNama Ayah: " + cursor.getString(2)
                        + "\nNama Ibu: " + cursor.getString(3)
                        + "\nTanggal Lahir: " + cursor.getString(4)
                        + "\nJurusan: " + cursor.getString(5)
                        + "\nTinggal: " + cursor.getString(6)
                        + "\nLahir: " + cursor.getString(7)
                        + "\nHobi: " + cursor.getString(8);
                userDataList.add(item);
            } while (cursor.moveToNext());
        }

        cursor.close();

        adapter = new ArrayAdapter<>(this, R.layout.list_item_user, R.id.text1, userDataList);
        listView.setAdapter(adapter);
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadUserData(); // refresh list every time the activity is resumed
    }
}
