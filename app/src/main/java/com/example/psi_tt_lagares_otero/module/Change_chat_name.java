package com.example.psi_tt_lagares_otero.module;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.R;
import com.google.android.material.textfield.TextInputEditText;

public class Change_chat_name extends AppCompatActivity {

    Button btn_ok;
    TextInputEditText chat_name;
    private Handler handler = GlobalApplication.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_chat_name);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        btn_ok = findViewById(R.id.btn_ok);
        chat_name = findViewById(R.id.chat_name);

        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (chat_name.getText().toString().equals("")) {
                    Toast.makeText(Change_chat_name.this, "Nome inválido", Toast.LENGTH_SHORT).show();
                }
                else {
                    handler.changeRoomName(chat_name.getText().toString());
                    finish();
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