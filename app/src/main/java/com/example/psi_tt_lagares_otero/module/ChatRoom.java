package com.example.psi_tt_lagares_otero.module;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.Model.Message;
import com.example.psi_tt_lagares_otero.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class ChatRoom extends AppCompatActivity {
    private EditText editText;
    private MessageAdapter messageAdapter;
    private ListView messagesView;

    private Handler handler = GlobalApplication.getInstance().getHandler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        Intent intent = getIntent();
        String roomID = intent.getStringExtra("INTENT_EXTRA_ROOMID");
        if (roomID == null) {
            finish();
        }

        handler.connectToRoom(roomID, new Handler.callback() {
            @Override
            public void callback(Object o) {
                update();
            }
        });
        handler.setMessageListener(new Handler.messageListener() {
            @Override
            public void onMessageClick(Message message) {
                messageReceived(message);
            }
        });

        editText = (EditText) findViewById(R.id.editText);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
        update();
    }


    public void update() {
        if (handler.currentRoom != null) {
            setTitle(handler.currentRoom.getName());
        }
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        System.out.println("message: " + message);
        if (message.length() > 0) {
            handler.sendMessage(message);
            editText.getText().clear();
        }
    }

    public void messageReceived(Message message) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                messageAdapter.add(message);
                // scroll the ListView to the last added element
                messagesView.setSelection(messagesView.getCount() - 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.chat_options,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.change_chat_name:
                Intent i = new Intent(this, Change_chat_name.class);
                startActivity(i);
                return true;
            case R.id.exit_chat:
                leaveChatPopUp();
                return true;
            case R.id.add_user:
                Intent i2 = new Intent(this, Add_user2chat.class);
                startActivity(i2);
                return true;
            case R.id.remove_user:
                Intent i3 = new Intent(this, Remove_user_chat.class);
                startActivity(i3);
                return true;
            case android.R.id.home:
                    finish();
                    return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void leaveChatPopUp() {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Sair do chat?");
        alert.setMessage("Solo poderas volver se o owner te añade de novo");

        alert.setPositiveButton("Si", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                handler.removeSelfFromRoom(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        finish();
                    }
                });
            }
        });

        alert.setNegativeButton("Non", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}