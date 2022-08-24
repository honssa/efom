package com.example.psi_tt_lagares_otero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    TextInputEditText username, passwd;
    Button btn_login;

    FirebaseAuth auth;

    SharedPreferences preferences = GlobalApplication.getInstance().getLogin_preferences();

    Handler handler = GlobalApplication.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getSupportActionBar().setTitle("Login");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        auth = FirebaseAuth.getInstance();
        passwd = findViewById(R.id.passwd);
        username = findViewById(R.id.username);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                String txt_passwd = passwd.getText().toString();
                String txt_username = username.getText().toString();
                if (TextUtils.isEmpty(txt_passwd) || TextUtils.isEmpty(txt_username)){
                    Toast.makeText(LoginActivity.this, "All fields required", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(LoginActivity.this, "Logging in..", Toast.LENGTH_SHORT).show();
                    handler.auth(txt_username,txt_passwd,new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()){

                                SharedPreferences.Editor edit = preferences.edit();
                                edit.putString("username", txt_username);
                                edit.putString("password", txt_passwd);
                                edit.apply();

                                Intent intent = new Intent(LoginActivity.this, com.example.psi_tt_lagares_otero.module.MainActivity.class);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(LoginActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }
            }
        });
    }
}