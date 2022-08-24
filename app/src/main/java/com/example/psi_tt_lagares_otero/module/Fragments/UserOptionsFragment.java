package com.example.psi_tt_lagares_otero.module.Fragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.R;
import com.example.psi_tt_lagares_otero.module.AddConvActivity;
import com.example.psi_tt_lagares_otero.module.MainActivity;

import java.util.ArrayList;
import java.util.Set;


public class UserOptionsFragment extends Fragment {
    Button del_account, block_user, log_out;

    public View view;

    private Handler handler = GlobalApplication.getInstance().getHandler();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_user_options, container, false);
        del_account = view.findViewById(R.id.del_account);
        block_user = view.findViewById(R.id.block_user);
        log_out = view.findViewById(R.id.log_out_account);

        del_account.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SharedPreferences preferences = GlobalApplication.getInstance().getLogin_preferences();

                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("username", "");
                edit.putString("password", "");
                edit.apply();

                handler.delete_account();

                Intent intent = new Intent(getActivity(), com.example.psi_tt_lagares_otero.StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        block_user.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                blockPopup();
            }
        });

        log_out.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                SharedPreferences preferences = GlobalApplication.getInstance().getLogin_preferences();

                SharedPreferences.Editor edit = preferences.edit();
                edit.putString("username", "");
                edit.putString("password", "");
                edit.apply();

                handler.log_out();

                Intent intent = new Intent(getActivity(), com.example.psi_tt_lagares_otero.StartActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });
        return view;
    }

    private void blockPopup() {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());

        alert.setTitle("Nome do usuario a bloquear:");
        alert.setMessage("Non poderas leer as suas mesaxes!");

        final EditText input = new EditText(getActivity());
        input.setText("");
        alert.setView(input);

        alert.setPositiveButton("Bloquear", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String user_name = input.getText().toString();
                ArrayList<String> nome = new ArrayList<String>();
                nome.add(user_name);
                handler.getUids(nome, new Handler.callback() {

                    @Override
                    public void callback(Object o) {
                        ArrayList<String> list = (ArrayList<String>) o;
                        Set<String> set = handler.block_user(list.get(0));
                        //TODO: Gardar o set nunha sharedpreference
                    }
                });
            }
        });

        alert.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }
}