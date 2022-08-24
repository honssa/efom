package com.example.psi_tt_lagares_otero.module.Fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.GlobalApplication;
import com.example.psi_tt_lagares_otero.Model.Room;
import com.example.psi_tt_lagares_otero.R;
import com.example.psi_tt_lagares_otero.module.AddConvActivity;
import com.example.psi_tt_lagares_otero.module.ChatRoom;
import com.example.psi_tt_lagares_otero.module.MainActivity;
import com.example.psi_tt_lagares_otero.module.RoomAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;


public class ChatListFragment extends Fragment {

    public class addRoom {
        public void add(Room r) {
            roomAdapter.add(r);
        }
    }

    public addRoom addRoomMethod = new addRoom();

    private ListView roomViews;
    private RoomAdapter roomAdapter;
    private Handler handler = GlobalApplication.getInstance().getHandler();
    public View view;

    public static final int CREATE_ROOM = 808;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        //return inflater.inflate(R.layout.fragment_chat_list, container, false);
        view = inflater.inflate(R.layout.fragment_chat_list, container, false);

        roomViews = (ListView) view.findViewById(R.id.room_view);


        FloatingActionButton myFab = (FloatingActionButton) view.findViewById(R.id.add_conv);
        myFab.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddConvActivity.class );
                startActivityForResult(intent,MainActivity.CREATE_ROOM);
            }
        });


        readChats();

        roomAdapter.setItemClickListener(new RoomAdapter.ItemClickListener() {
            @Override
            public void onItemClick(View view, Room r) {
                enterRoom(r);
            }
        });
        return view;
    }

    private void readChats(){
        roomAdapter = new RoomAdapter(this.getContext());
        roomViews.setAdapter(roomAdapter);
    }

    @Override
    public void onResume() {
        super.onResume();
        refresh();
    }

    private void refresh() {
        roomAdapter.clean();
        handler.getRooms(addRoomMethod);
    }

    private void enterRoom(Room r) {
        Intent intent = new Intent(getActivity(), ChatRoom.class);
        String id = r.getId();
        intent.putExtra("INTENT_EXTRA_ROOMID", id);
        startActivity(intent);
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
}