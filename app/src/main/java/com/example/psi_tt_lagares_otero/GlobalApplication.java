package com.example.psi_tt_lagares_otero;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.DataConnection.HandlerFirebase;

public class GlobalApplication extends Application {

    private static GlobalApplication instance;

    private Handler handler;

    private SharedPreferences login_preferences;

    @Override
    public void onCreate() {
        super.onCreate();
        login_preferences = getSharedPreferences("eofm-logging", Context.MODE_PRIVATE);
        handler = new HandlerFirebase();
        instance = this;
    }

    public static GlobalApplication getInstance() {
        return instance;
    }

    public Handler getHandler() {
        return handler;
    }

    public SharedPreferences getLogin_preferences() { return login_preferences; }

}
