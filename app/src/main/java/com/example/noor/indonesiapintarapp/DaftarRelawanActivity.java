package com.example.noor.indonesiapintarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class DaftarRelawanActivity extends AppCompatActivity {

    private ListView listViewRelawan;
    List<Relawan> relawanList;

    DatabaseReference databaseRef;

    private BottomNavigationView.OnNavigationItemSelectedListener botnav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigasi_home:
                    Intent intent = new Intent(getApplicationContext(),HomeAdminActivity.class);
                    startActivity(intent);
                    return true;
                case R.id.navigasi_notif:

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
        setContentView(R.layout.activity_daftar_relawan);

        listViewRelawan = (ListView)findViewById(R.id.listViewDaftarRelawan);
        databaseRef = FirebaseDatabase.getInstance().getReference("relawan");
        relawanList = new ArrayList<>();

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigasi);
        nav.setOnNavigationItemSelectedListener(botnav);
    }

    @Override
    protected void onStart() {
        super.onStart();

        tampilDaftarRelawan();
    }

    private void tampilDaftarRelawan(){
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                relawanList.clear();

                for (DataSnapshot relawanSnapshot:dataSnapshot.getChildren()){
                    Relawan relawan = relawanSnapshot.getValue(Relawan.class);

                    relawanList.add(relawan);
                }
                RelawanList adapter = new RelawanList(DaftarRelawanActivity.this,relawanList);
                listViewRelawan.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {
        return;
    }

    private void LogOut(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }
}
