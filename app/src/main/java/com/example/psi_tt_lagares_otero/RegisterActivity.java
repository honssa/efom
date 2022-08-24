package com.example.psi_tt_lagares_otero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

public class RegisterActivity extends AppCompatActivity {

    TextInputEditText username, passwd, passwd2;
    Button btn_register;

    FirebaseAuth auth;
    DatabaseReference reference;
    String txt_username;

    Handler handler = GlobalApplication.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        username = findViewById(R.id.username);
        passwd = findViewById(R.id.passwd);
        passwd2 = findViewById(R.id.passwd2);
        btn_register = findViewById(R.id.btn_register);

        getSupportActionBar().setTitle("Create account");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        btn_register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txt_username = username.getText().toString();
                String txt_passwd = passwd.getText().toString();
                String txt_passwd2 = passwd2.getText().toString();

                if (TextUtils.isEmpty(txt_username) || TextUtils.isEmpty(txt_passwd) || TextUtils.isEmpty(txt_passwd2)){
                    Toast.makeText(RegisterActivity.this, "All fields are required", Toast.LENGTH_SHORT).show();
                } else if (txt_passwd.length() < 6) {
                    Toast.makeText(RegisterActivity.this, "password must have more than 6 characters", Toast.LENGTH_SHORT).show();
                } else if (!txt_passwd2.equals(txt_passwd)){
                    Toast.makeText(RegisterActivity.this, "passwords don't match", Toast.LENGTH_SHORT).show();
                } else{
                    handler.register(txt_username, txt_passwd,
                            new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterActivity.this, "Failed Registration", Toast.LENGTH_SHORT).show();
                                }
                            }
                            ,
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                        Toast.makeText(RegisterActivity.this, "Succesful Registration", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(RegisterActivity.this, com.example.psi_tt_lagares_otero.module.MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                }
                            }
                    );
                    //register(txt_username, txt_passwd);
                }
            }
        });
    }

    private void register(String username, String passwd){
        auth.createUserWithEmailAndPassword(txt_username+"@gmail.com", passwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                            reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",username);
                            hashMap.put("imageURL","default");

                            reference.setValue(hashMap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(RegisterActivity.this, com.example.psi_tt_lagares_otero.module.MainActivity.class);
                                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                        startActivity(intent);
                                        finish();
                                    }
                                }
                            });
                        } else {
                            Toast.makeText(RegisterActivity.this, "Failed Registration", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}