package com.example.psi_tt_lagares_otero.DataConnection;

import com.example.psi_tt_lagares_otero.Model.MemberData;
import com.example.psi_tt_lagares_otero.Model.Message;
import com.example.psi_tt_lagares_otero.Model.Room;
import com.example.psi_tt_lagares_otero.module.Fragments.ChatListFragment;
import com.example.psi_tt_lagares_otero.module.MainActivity;
import com.example.psi_tt_lagares_otero.module.RoomAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public abstract class Handler {

    MemberData data;
    public Room currentRoom;

    public messageListener messageCallback;
    public interface messageListener {
        void onMessageClick(Message message);
    }
    public interface callback {
        void callback(Object o);
    }

    public void setMessageListener(messageListener onMessage) {
        messageCallback = onMessage;
    }

    public abstract void register(String txt_username, String txt_passwd, OnFailureListener register_fail,OnCompleteListener listener);

    public abstract void auth(String txt_username, String txt_passwd, OnCompleteListener listener);

    public abstract void connectToRoom(String id,callback callback);

    public abstract void sendMessage(String message);

    public abstract String createNewRoom(String name, ArrayList<String> users);

    public abstract void changeRoomName(String new_name );

    public abstract void getRooms(ChatListFragment.addRoom addRoom);

    public abstract void getUids(ArrayList<String> name,Handler.callback callback);

    public abstract void addUserToRoom(String user);

    public abstract void removeUserFromRoom(String user);

    public abstract void removeSelfFromRoom(OnCompleteListener listener);

    public abstract void log_out();

    public abstract void delete_account();

    public abstract Set<String> block_user(String uid);

}
