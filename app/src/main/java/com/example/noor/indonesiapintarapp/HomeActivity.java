package com.example.noor.indonesiapintarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    Button TambahSekolah;

    Spinner filterSekolah;

    DatabaseReference databaseRef;

    List<Sekolah> sekolahList;
    ListView listViewSekolah;

    public static final String SEKOLAH_NAME = "sekolahname";
    public static final String SEKOLAH_LINK = "sekolahlink";
    public static final String SEKOLAH_DESKRIPSI = "sekolahdeskripsi";

    private BottomNavigationView.OnNavigationItemSelectedListener botnav = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()){
                case R.id.navigasi_home:

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
        setContentView(R.layout.activity_home);

        databaseRef = FirebaseDatabase.getInstance().getReference("sekolah");

        TambahSekolah = (Button)findViewById(R.id.btnTambahSekolah);
        listViewSekolah = (ListView)findViewById(R.id.listViewSekolah);
        sekolahList = new ArrayList<>();

        listViewSekolah.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Sekolah sekolah = sekolahList.get(position);

                Intent intent = new Intent(getApplicationContext(),SekolahActivity.class);
                intent.putExtra(SEKOLAH_NAME,sekolah.getNamaSekolah());
                intent.putExtra(SEKOLAH_DESKRIPSI,sekolah.getDeskripsi());
                intent.putExtra(SEKOLAH_LINK,sekolah.getGambar());
                startActivity(intent);
            }
        });

        TambahSekolah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),InputSekolahActivity.class);
                startActivity(intent);
            }
        });

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigasi);
        nav.setOnNavigationItemSelectedListener(botnav);
    }

    @Override
    protected void onStart() {
        super.onStart();

        filterSekolah = (Spinner) findViewById(R.id.spinnerJenjang);

        Sekolah();

        filterSekolah.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Filter();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void LogOut(){
        Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
    }

    private void Sekolah(){
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                sekolahList.clear();

                for (DataSnapshot sekolahSnapshot:dataSnapshot.getChildren()){
                    Sekolah sekolah = sekolahSnapshot.getValue(Sekolah.class);

                    sekolahList.add(sekolah);
                }
                SekolahList adapter = new SekolahList(HomeActivity.this,sekolahList);
                listViewSekolah.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void Filter(){
        String filter = filterSekolah.getSelectedItem().toString().trim();

        if (!filter.equalsIgnoreCase("Semua Jenjang")){

            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    sekolahList.clear();
                    String jenjang;
                    for (DataSnapshot sekolahSnapshot : dataSnapshot.getChildren()){

                        Sekolah sekolah = sekolahSnapshot.getValue(Sekolah.class);


                        if (filterSekolah.getSelectedItem().toString().equals("SD")){
                            jenjang = sekolah.getNamaSekolah().substring(0,2);
                        }else{
                            jenjang = sekolah.getNamaSekolah().substring(0,3);
                        }

                        if (jenjang.equalsIgnoreCase(filterSekolah.getSelectedItem().toString())){
                            sekolahList.add(sekolah);
                        }
                    }

                    if (sekolahList.size()>0){
                        SekolahList adapter = new SekolahList(HomeActivity.this,sekolahList);
                        listViewSekolah.setAdapter(adapter);
                    }else if(sekolahList.size()==0){
                        handleFilter();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }if (filter.equalsIgnoreCase("Semua Jenjang")){
            Sekolah();
        }
    }

    private void handleFilter(){
        Toast.makeText(this, "Jenjang tidak ada dalam data sekolah",Toast.LENGTH_LONG).show();
    }


}
