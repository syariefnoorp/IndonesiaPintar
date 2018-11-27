package com.example.noor.indonesiapintarapp;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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

public class RegistActivity extends AppCompatActivity {

    private Button Daftar;
    private EditText Email;
    private EditText Password;
    private TextView Login;
    private ProgressBar loading;

    DatabaseReference databaseRef;

    List<User> userList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regist);

        databaseRef = FirebaseDatabase.getInstance().getReference("user");

        Email = (EditText)findViewById(R.id.email);
        Password = (EditText)findViewById(R.id.password);
        Login = (TextView)findViewById(R.id.linkLogin);
        Daftar = (Button)findViewById(R.id.btnRegist);
        loading = (ProgressBar)findViewById(R.id.loadingBar);

        Daftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loading.setVisibility(View.VISIBLE);
                SignUp();
            }
        });

        Login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });
    }

    private void SignUp(){
        String email = Email.getText().toString().trim();
        String pass = Password.getText().toString().trim();

        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (!TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)&&pass.length()>5&&email.matches(emailPattern)){
            cekEmail();
        }else{
            if (TextUtils.isEmpty(email)&&!TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Kolom email harus diisi",Toast.LENGTH_SHORT).show();
            }else if (TextUtils.isEmpty(pass)&&!TextUtils.isEmpty(email)){
                Toast.makeText(this, "Kolom password harus diisi",Toast.LENGTH_SHORT).show();
            }else if(TextUtils.isEmpty(email)&&TextUtils.isEmpty(pass)){
                Toast.makeText(this, "Kolom email dan password harus diisi",Toast.LENGTH_SHORT).show();
            }else if (pass.length()<6){
                Toast.makeText(this, "Password minimal 6 karakter",Toast.LENGTH_SHORT).show();
            }else if(!email.matches(emailPattern)){
                Toast.makeText(this, "Format emaail : example@domain.com",Toast.LENGTH_SHORT).show();
            }

            loading.setVisibility(View.GONE);
        }
    }

    private void cekEmail(){
        databaseRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                userList.clear();
                String email = Email.getText().toString().trim();
                for (DataSnapshot userSnapshot : dataSnapshot.getChildren()){
                    User user = userSnapshot.getValue(User.class);
                    if (user.getEmail().equals(email)){
                        userList.add(user);
                    }
                }
                if (userList.size()>0){
                    addUser(false);
                }else{
                    addUser(true);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void addUser(boolean status){
        String email = Email.getText().toString().trim();
        String pass = Password.getText().toString().trim();

        if (status==true){
            String id = databaseRef.push().getKey();

            User user = new User(id,email,pass);

            databaseRef.child(id).setValue(user);

            Email.getText().clear();
            Password.getText().clear();

            closeKeyboard();
            Toast.makeText(this,"Regist Sukses!",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(this,"Email telah terdaftar! Gunakan email lain",Toast.LENGTH_SHORT).show();
        }

        loading.setVisibility(View.GONE);
    }

    private void closeKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null){
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(),0);
        }
    }
}
