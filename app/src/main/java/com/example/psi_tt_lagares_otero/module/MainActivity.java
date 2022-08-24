package com.example.psi_tt_lagares_otero.module;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.Model.Room;
import com.example.psi_tt_lagares_otero.R;
import com.example.psi_tt_lagares_otero.module.ChatRoom;
import com.example.psi_tt_lagares_otero.module.Fragments.ChatListFragment;
import com.example.psi_tt_lagares_otero.module.Fragments.UserOptionsFragment;
import com.example.psi_tt_lagares_otero.module.RoomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
//import com.scaledrone.lib.Message;


public class MainActivity extends AppCompatActivity {

    public class addRoom {
        public void add(Room r) {
            roomAdapter.add(r);
        }
    }

    public addRoom addRoomMethod = new addRoom();

    private RoomAdapter roomAdapter;
    private ListView roomViews;

    public static final int CREATE_ROOM = 808;

    private Handler handler = GlobalApplication.getInstance().getHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d("TESTEO","AAAAA");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lobby);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(false);
        }


        roomAdapter = new RoomAdapter(this);
        roomViews = (ListView) findViewById(R.id.room_view);
        //roomViews.setAdapter(roomAdapter);




        /*
        roomAdapter.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClick(View view, Room r) {
                enterRoom(r);
            }
        });*/


        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager viewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPageAdapter = new ViewPagerAdapter(getSupportFragmentManager());
        ChatListFragment chatListFragment = new ChatListFragment();
        viewPageAdapter.addFragment(chatListFragment, "Chats");
        //FragmentManager fragmentManager = getSupportFragmentManager();
        //FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //fragmentTransaction.replace(R.id.lobby_content, chatListFragment);
        //fragmentTransaction.commit();
        // exp
        viewPageAdapter.addFragment(new UserOptionsFragment(), "Settings");

        viewPager.setAdapter(viewPageAdapter);
        tabLayout.setupWithViewPager(viewPager);





    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.d("TAG","test");
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (CREATE_ROOM) : {
                if (resultCode == Activity.RESULT_OK) {
                    ArrayList<String> userList = data.getStringArrayListExtra("result");
                    String chatName = data.getStringExtra("chatName");
                    handler.createNewRoom(chatName,userList);
                }
                break;
            }
        }
    }

    private void refresh() {
        roomAdapter.clean();
        //handler.getRooms(addRoomMethod);
    }

    private void enterRoom(Room r) {
        Intent intent = new Intent(this, ChatRoom.class);
        String id = r.getId();
        intent.putExtra("INTENT_EXTRA_ROOMID", id);
        startActivity(intent);
    }
    
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.lobby_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.log_out){
            SharedPreferences preferences = GlobalApplication.getInstance().getLogin_preferences();

            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("username", "");
            edit.putString("password", "");
            edit.apply();

            handler.log_out();

            Intent intent = new Intent(MainActivity.this, com.example.psi_tt_lagares_otero.StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        else if (id == R.id.delete) {
            SharedPreferences preferences = GlobalApplication.getInstance().getLogin_preferences();

            SharedPreferences.Editor edit = preferences.edit();
            edit.putString("username", "");
            edit.putString("password", "");
            edit.apply();

            handler.delete_account();

            Intent intent = new Intent(MainActivity.this, com.example.psi_tt_lagares_otero.StartActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }


    class ViewPagerAdapter extends FragmentPagerAdapter{

        private  ArrayList<Fragment> fragments;
        private  ArrayList<String> titles;

        ViewPagerAdapter(FragmentManager fm){
            super(fm, FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
            this.fragments = new ArrayList<>();
            this.titles = new ArrayList<>();
        }
        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }

        public void addFragment(Fragment fragment, String title){
            fragments.add(fragment);
            titles.add(title);
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position){
            return titles.get(position);
        }

    }
}