package com.example.noor.indonesiapintarapp;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.Image;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class InputSekolahActivity extends AppCompatActivity {

    private EditText namaSekolah;
    private EditText deskripsi;
    private Button Tambah;
    private ProgressBar loading;

    private ImageView uploadImg;
    private TextView file_path;
    private Uri filepath;

    private String link = "link gambar";

    private final int PICK_IMG_REQUEST = 71;

    DatabaseReference databaseRef;
    FirebaseStorage firebaseStorage;
    StorageReference storageRef;

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
        setContentView(R.layout.activity_input_sekolah);

        firebaseStorage = FirebaseStorage.getInstance();
        storageRef = firebaseStorage.getReference().child("images/"+ UUID.randomUUID().toString());
        databaseRef = FirebaseDatabase.getInstance().getReference("sekolah_ajuan");

        namaSekolah = (EditText)findViewById(R.id.editTextNamaSekolah);
        deskripsi = (EditText)findViewById(R.id.editTextDeskripsi);
        loading = (ProgressBar)findViewById(R.id.loadingBar);

        uploadImg = (ImageView)findViewById(R.id.addImg);
        file_path = (TextView)findViewById(R.id.file_path);
        Tambah = (Button)findViewById(R.id.btnTambah);

        Tambah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                tambahSekolah();
            }
        });

        uploadImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pilihGambar();
            }
        });

        BottomNavigationView nav = (BottomNavigationView)findViewById(R.id.navigasi);
        nav.setOnNavigationItemSelectedListener(botnav);
    }

    private void uploadGambar(){
        if (filepath!=null){
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            //StorageReference ref = storageRef.child("images/"+ UUID.randomUUID().toString());
            storageRef.putFile(filepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    progressDialog.dismiss();
                    Toast.makeText(InputSekolahActivity.this, "Upload Sukses!",Toast.LENGTH_SHORT).show();
                    setLink();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(InputSekolahActivity.this, "Upload Gagal! "+e.getMessage(),Toast.LENGTH_SHORT).show();
                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                    double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                    progressDialog.setMessage("Uploaded "+(int)progress+"%");
                }
            });


        }
    }

    private void setLink(){
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                link = uri.toString();
            }
        });
    }

    private void pilihGambar(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent,"select picture"),PICK_IMG_REQUEST);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==PICK_IMG_REQUEST&&resultCode==RESULT_OK&&data!=null&&data.getData()!=null){
            filepath = data.getData();
            if (filepath!=null){
                file_path.setText("Gambar sukses di upload! klik submit!");
                uploadGambar();
            }
        }
    }

    private void tambahSekolah(){
        final String nama_sekolah = namaSekolah.getText().toString().trim();
        final String deskripsi_sekolah = deskripsi.getText().toString().trim();

        if (!TextUtils.isEmpty(nama_sekolah)&&!TextUtils.isEmpty(deskripsi_sekolah)&&!link.equals("link gambar")){
            databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String idSekolah = databaseRef.push().getKey();

                    Sekolah sekolah = new Sekolah(idSekolah,nama_sekolah,link,deskripsi_sekolah);

                    databaseRef.child(idSekolah).setValue(sekolah);

                    loading.setVisibility(View.GONE);

                    DialogTambahSekolah dialog = new DialogTambahSekolah();
                    dialog.show(getSupportFragmentManager(),"dialog sekolah");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });



        }else{
            if (TextUtils.isEmpty(nama_sekolah)){
                Toast.makeText(this, "Kolom nama sekolah harus diisi",Toast.LENGTH_LONG).show();
            }else if (TextUtils.isEmpty(deskripsi_sekolah)){
                Toast.makeText(this, "Kolom deskripsi harus diisi",Toast.LENGTH_LONG).show();
            }else if (link.equals("link gambar")){
                Toast.makeText(this, "Harap upload gambar sekolah",Toast.LENGTH_LONG).show();
            }
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
