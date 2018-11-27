package com.example.noor.indonesiapintarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.squareup.picasso.Picasso;

public class SekolahActivity extends AppCompatActivity {

    private TextView namaSekolah;
    private TextView deskripsiSekolah;
    private ImageView gambarSekolah;
    private Button join;

    public static final String SEKOLAH_NAME = "sekolahname";

    private BottomNavigationView.OnNavigationItemSelectedListener botnav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigasi_home:
                    Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigasi_logout:
                    LogOut();
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sekolah);

        namaSekolah = (TextView)findViewById(R.id.detail_namaSekolah);
        deskripsiSekolah = (TextView)findViewById(R.id.detail_deskripsiSekolah);
        gambarSekolah = (ImageView) findViewById(R.id.imgSekolah);
        join = (Button) findViewById(R.id.btnJoin);

        Intent intent = getIntent();

        String nama_sekolah = intent.getStringExtra(HomeActivity.SEKOLAH_NAME);
        String deskripsi_sekolah = intent.getStringExtra(HomeActivity.SEKOLAH_DESKRIPSI);
        String link_sekolah = intent.getStringExtra(HomeActivity.SEKOLAH_LINK);

        namaSekolah.setText(nama_sekolah);
        deskripsiSekolah.setText(deskripsi_sekolah);
        Picasso.get().load(link_sekolah).into(gambarSekolah);

        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RelawanActivity.class);
                intent.putExtra(SEKOLAH_NAME,namaSekolah.getText().toString());
                startActivity(intent);
            }
        });

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigasi);
        nav.setOnNavigationItemSelectedListener(botnav);
    }

    private void LogOut(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        return;
    }
}
