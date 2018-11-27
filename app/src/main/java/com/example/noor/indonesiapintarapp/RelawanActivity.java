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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class RelawanActivity extends AppCompatActivity {

    private EditText namaRelawan;
    private EditText email;
    private EditText noTelp;
    private Button submit;
    private ProgressBar loading;

    DatabaseReference databaseRef;
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
        setContentView(R.layout.activity_relawan);

        databaseRef = FirebaseDatabase.getInstance().getReference("relawan");

        namaRelawan = (EditText)findViewById(R.id.input_namaRelawan);
        email = (EditText)findViewById(R.id.input_emailRelawan);
        noTelp = (EditText)findViewById(R.id.input_noTelp);
        submit = (Button) findViewById(R.id.btnSubmit);
        loading = (ProgressBar)findViewById(R.id.loadingBar);

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                Relawan();
            }
        });

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigasi);
        nav.setOnNavigationItemSelectedListener(botnav);
    }

    private void Relawan(){
        Intent intent = getIntent();

        final String nama_relawan = namaRelawan.getText().toString().trim();
        final String email_relawan = email.getText().toString().trim();
        final String no_telp = noTelp.getText().toString().trim();
        final String nama_sekolah = intent.getStringExtra(SekolahActivity.SEKOLAH_NAME);

        if (!TextUtils.isEmpty(nama_relawan)&&!TextUtils.isEmpty(email_relawan)&&!TextUtils.isEmpty((no_telp))){
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String idRelawan = databaseRef.push().getKey();

                    Relawan relawan = new Relawan(idRelawan,nama_relawan,nama_sekolah,email_relawan,no_telp);

                    databaseRef.child(idRelawan).setValue(relawan);

                    loading.setVisibility(View.GONE);
                    DialogDaftarRelawan dialog = new DialogDaftarRelawan();
                    dialog.show(getSupportFragmentManager(),"dialog relawan");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }else{
            Toast.makeText(this,"Daftar relawan gagal! periksa kembali data inputan!",Toast.LENGTH_LONG).show();
            loading.setVisibility(View.GONE);
        }
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
