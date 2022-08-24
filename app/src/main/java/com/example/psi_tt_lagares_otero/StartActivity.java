package com.example.psi_tt_lagares_otero;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;

public class StartActivity extends AppCompatActivity {
    Button login, register;


    Handler handler = GlobalApplication.getInstance().getHandler();

    SharedPreferences preferences = GlobalApplication.getInstance().getLogin_preferences();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        String txt_username = preferences.getString("username", null);
        String txt_password = preferences.getString("password", null);
        /*if (txt_username != null && txt_password != null && txt_username != "" && txt_password != "") {
            logging(txt_username,txt_password);
        }
        /*SharedPreferences.Editor edit = preferences.edit();
        edit.putString("username", txt_username);
        edit.putString("password", txt_passwd);
        edit.apply();*/
        if (txt_username != null && txt_password != null && txt_username != "" && txt_password != "") {
            Toast.makeText(StartActivity.this, "Logging in..", Toast.LENGTH_SHORT).show();
            handler.auth(txt_username,txt_password,new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()){
                        Intent intent = new Intent(StartActivity.this, com.example.psi_tt_lagares_otero.module.MainActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(StartActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            setContentView(R.layout.activity_start);


            login = findViewById(R.id.login);
            register = findViewById(R.id.register);

            login.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(StartActivity.this, LoginActivity.class));

                }

            });


            register.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(new Intent(StartActivity.this, RegisterActivity.class));

                }

            });
        }
    }
}