package com.example.psi_tt_lagares_otero.module;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class AddConvActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    UserInputAdapter mAdapter;
    String[] mDataSet = new String[1];

    Button bt;

    private Handler handler = GlobalApplication.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mAdapter = new UserInputAdapter();
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setHasFixedSize(true);

        bt = findViewById(R.id.btn_register);

        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mAdapter.mDataset.contains("")) {
                    for(int i = 0;i < mAdapter.mDataset.size();i++) {
                        if (mAdapter.mDataset.get(i) == "") {
                            mAdapter.mDataset.set(i,null);
                            mAdapter.notifyDataSetChanged();
                        }
                    }
                }
                else {
                    handler.getUids(mAdapter.mDataset, new Handler.callback() {

                        @Override
                        public void callback(Object o) {
                            ArrayList<String> list = (ArrayList<String>) o;
                            Intent resultIntent = new Intent();
                            if (list.contains(null)) {
                                for (int i = 0; i < list.size(); i++) {
                                    if (list.get(i) == null) {
                                        mAdapter.mDataset.set(i, null);
                                        mAdapter.notifyDataSetChanged();
                                    }
                                }
                            } else {
                                resultIntent.putExtra("result", list);
                                setNameAndFinish(resultIntent);
                            }
                        }
                    });
                }
            }
        });

        FloatingActionButton myFab = (FloatingActionButton) findViewById(R.id.add_user);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mAdapter.mDataset.add("");
                mAdapter.notifyDataSetChanged();
            }
        });

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }
    }


    public void setNameAndFinish(Intent i) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Nome do chat:");

        final EditText input = new EditText(this);
        input.setText("Chat 1");
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                i.putExtra("chatName",input.getText().toString());
                setResult(Activity.RESULT_OK, i);
                finish();
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}