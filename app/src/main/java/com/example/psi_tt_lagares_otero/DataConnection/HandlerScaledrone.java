package com.example.psi_tt_lagares_otero.DataConnection;

import android.util.Log;

import com.example.psi_tt_lagares_otero.Model.MemberData;
import com.example.psi_tt_lagares_otero.Model.Message;
import com.example.psi_tt_lagares_otero.module.Fragments.ChatListFragment;
import com.example.psi_tt_lagares_otero.module.MainActivity;
import com.example.psi_tt_lagares_otero.module.RoomAdapter;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.ArrayList;
import java.util.Date;
import java.util.Set;

public class HandlerScaledrone extends Handler implements RoomListener {
    private String channelID = "fY2lld8R2yZxcRHX";
    private String roomName = "observable-room";
    private Scaledrone scaledrone;



    public void init() {
        MemberData data = null;

        scaledrone = new Scaledrone(channelID, data);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                // Since the MainActivity itself already implement RoomListener we can pass it as a target
                scaledrone.subscribe(roomName, HandlerScaledrone.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);
            }
        });

    }

    @Override
    public void register(String txt_username, String txt_passwd, OnFailureListener register_fail, OnCompleteListener listener) {

    }

    @Override
    public void auth(String txt_username, String txt_passwd, OnCompleteListener listener) {

    }

    @Override
    public void connectToRoom(String id, callback callback) {

    }


    public void sendMessage(String message) {
        scaledrone.publish("observable-room", message);
    }

    @Override
    public String createNewRoom(String name, ArrayList<String> users) {
        return null;
    }

    @Override
    public void changeRoomName(String new_name) {

    }

    @Override
    public void getRooms(ChatListFragment.addRoom addRoom) {

    }

    @Override
    public void getUids(ArrayList<String> name, callback callback) {

    }

    @Override
    public void addUserToRoom(String user) {

    }

    @Override
    public void removeUserFromRoom(String user) {

    }

    @Override
    public void removeSelfFromRoom(OnCompleteListener listener) {

    }


    @Override
    public void log_out() {

    }

    @Override
    public void delete_account() {

    }

    @Override
    public Set<String> block_user(String uid) {
        return null;
    }


    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
    }

    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    @Override
    public void onMessage(Room room, com.scaledrone.lib.Message receivedMessage) {
        // To transform the raw JsonNode into a POJO we can use an ObjectMapper
        final ObjectMapper mapper = new ObjectMapper();
        try {
            // member.clientData is a MemberData object, let's parse it as such
            final MemberData data = mapper.treeToValue(receivedMessage.getMember().getClientData(), MemberData.class);
            // if the clientID of the message sender is the same as our's it was sent by us
            boolean belongsToCurrentUser = receivedMessage.getClientID().equals(scaledrone.getClientID());
            // since the message body is a simple string in our case we can use json.asText() to parse it as such
            // if it was instead an object we could use a similar pattern to data parsing

            final Message message = new Message(receivedMessage.getData().asText(), data, belongsToCurrentUser,new Date());

            messageCallback.onMessageClick(message);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }


}
