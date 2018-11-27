package com.example.noor.indonesiapintarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeAdminActivity extends AppCompatActivity {


    private ListView listViewSekolahAjuan;

    DatabaseReference databaseRef;
    List<Sekolah> sekolahAjuanList;

    private BottomNavigationView.OnNavigationItemSelectedListener botnav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigasi_home:

                    return true;
                case R.id.navigasi_notif:
                    Intent intent = new Intent(getApplicationContext(),DaftarRelawanActivity.class);
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
        setContentView(R.layout.activity_home_admin);

        listViewSekolahAjuan = (ListView)findViewById(R.id.listViewSekolahAjuan);
        sekolahAjuanList = new ArrayList<>();

        databaseRef = FirebaseDatabase.getInstance().getReference("sekolah_ajuan");

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigasi);
        nav.setOnNavigationItemSelectedListener(botnav);
    }

    @Override
    protected void onStart() {
        super.onStart();

        tampilSekolahAjuan();
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void tampilSekolahAjuan(){
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sekolahAjuanList.clear();

                for (DataSnapshot sekolahAjuanSnapshot:dataSnapshot.getChildren()){
                    Sekolah sekolahAjuan = sekolahAjuanSnapshot.getValue(Sekolah.class);

                    sekolahAjuanList.add(sekolahAjuan);
                }
                SekolahAjuanList adapter = new SekolahAjuanList(HomeAdminActivity.this,sekolahAjuanList);
                listViewSekolahAjuan.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void LogOut(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
