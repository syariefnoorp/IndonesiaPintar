package com.example.noor.indonesiapintarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private Button Login;
    private EditText Email;
    private EditText Password;
    private TextView Daftar;
    private ProgressBar loading;

    public static final String USER_EMAIL = "user_email";

    DatabaseReference databaseRef;

    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        databaseRef = FirebaseDatabase.getInstance().getReference("user");

        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        Login = (Button)findViewById(R.id.btnLogin);
        Daftar = (TextView) findViewById(R.id.linkDaftar);
        loading = (ProgressBar)findViewById(R.id.loadingBar);

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                CekInput();
            }
        });

        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),RegistActivity.class);
                startActivity(intent);
            }
        });
    }

    private void CekInput(){
        String email = Email.getText().toString().trim();
        String pass = Password.getText().toString().trim();

        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&pass.length()>5){

            CekLogin();

        }else{
            if (TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Kolom email harus diisi",Toast.LENGTH_LONG).show();
            }else if (TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(email)){
                Toast.makeText(this, "Kolom password harus diisi",Toast.LENGTH_LONG).show();
            }else if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Kolom email dan password harus diisi",Toast.LENGTH_LONG).show();
            }else{
                CekLogin();
            }
            loading.setVisibility(View.GONE);
        }
    }

    private void CekLogin(){
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                String email = Email.getText().toString().trim();
                String password = Password.getText().toString().trim();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if (user.getEmail().equals(email) && user.getPassword().equals(password)){
                        userList.add(user);
                    }
                }

                if (userList.size()>0){
                    if (email.equalsIgnoreCase("admin@sip.com")){
                        SignIn("admin");
                    }else{
                        SignIn(email);
                    }
                }else{
                    SignIn("wrong");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void SignIn(String status){

        if (status.equalsIgnoreCase("admin")){
            Toast.makeText(this,"Login Sukses!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),HomeAdminActivity.class);
            startActivity(intent);
            finish();
        }else if (status.equalsIgnoreCase("wrong")){
            Toast.makeText(this,"Email atau Password Salah!",Toast.LENGTH_LONG).show();
        }else{
            Toast.makeText(this,"Login Sukses!",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(getApplicationContext(),HomeActivity.class);
            intent.putExtra(USER_EMAIL,status);
            startActivity(intent);
            finish();
        }

        loading.setVisibility(View.GONE);
    }


}
