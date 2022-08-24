package com.example.psi_tt_lagares_otero.module;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.LoginActivity;
import com.example.psi_tt_lagares_otero.R;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;

public class Add_user2chat extends AppCompatActivity {

    Button btn_ok;
    TextInputEditText user_name;
    private Handler handler = GlobalApplication.getInstance().getHandler();
    UserInputAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_user2chat);


        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_ok = findViewById(R.id.btn_ok);
        user_name = findViewById(R.id.user_name);
        mAdapter = new UserInputAdapter();

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user_name.getText().toString().equals("")) {
                    Toast.makeText(Add_user2chat.this, "Nome inválido", Toast.LENGTH_SHORT).show();
                }
                else {
                    ArrayList<String> nome = new ArrayList<String>();
                    nome.add(user_name.getText().toString());
                    handler.getUids(nome, new Handler.callback() {

                        @Override
                        public void callback(Object o) {
                            ArrayList<String> list = (ArrayList<String>) o;
                            Intent resultIntent = new Intent();
                            if (list.get(0) == null) {
                                Toast.makeText(Add_user2chat.this, "User not found", Toast.LENGTH_SHORT).show();
                            } else {
                                handler.addUserToRoom(list.get(0));
                                finish();
                            }

                        }
                    });
                }
            }
        });
    }

    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}