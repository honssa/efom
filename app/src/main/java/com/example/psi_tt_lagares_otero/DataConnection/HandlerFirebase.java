package com.example.psi_tt_lagares_otero.DataConnection;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.psi_tt_lagares_otero.Model.MemberData;
import com.example.psi_tt_lagares_otero.Model.Message;
import com.example.psi_tt_lagares_otero.Model.Room;
import com.example.psi_tt_lagares_otero.module.Fragments.ChatListFragment;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.Timestamp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

public class HandlerFirebase extends Handler {
    private String channelID;
    private String roomName = "observable-room";

    DatabaseReference reference;


    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();;
    private FirebaseAuth auth = FirebaseAuth.getInstance();;
    private String UID;

    private ListenerRegistration listenerRegistration;

    private Set<String> blockedUsers = new HashSet<String>();

    @Override
    public void register(String txt_username, String txt_passwd, OnFailureListener register_fail,OnCompleteListener listener) {
        auth.createUserWithEmailAndPassword(txt_username+"@gmail.com", txt_passwd)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            FirebaseUser firebaseUser = auth.getCurrentUser();
                            assert firebaseUser != null;
                            String userid = firebaseUser.getUid();

                           // reference = FirebaseDatabase.getInstance().getReference("Users").child(userid);
                            HashMap<String, String> hashMap = new HashMap<>();
                            hashMap.put("id",userid);
                            hashMap.put("username",txt_username);
                            hashMap.put("imageURL","default");
                            hashMap.put("color",getRandomColor());

                            //reference.setValue(hashMap);

                            auth(txt_username,txt_passwd,listener);


                            firestore.collection("users").document(txt_username).set(hashMap);


                        }
                    }
                }).addOnFailureListener(register_fail);
    }

    @Override
    public void auth(String txt_username,String txt_passwd,OnCompleteListener listener) {
        data = new MemberData(txt_username,getRandomColor());

        auth.signInWithEmailAndPassword(txt_username+"@gmail.com", txt_passwd).addOnCompleteListener(listener
        ).addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                UID = null;
                while (UID == null) {
                    UID = auth.getUid();
                    DocumentReference docRef = firestore.collection("users").document(txt_username);
                    Task<DocumentSnapshot> a = docRef.get();
                    while (!a.isComplete()) {

                    }
                    String color = (String) a.getResult().get("color");
                    data = new MemberData(txt_username,color);
                }
            }
        });

    }

    @Override
    public void connectToRoom(String id,callback c) {
        channelID = id;

        if (listenerRegistration != null) {
            listenerRegistration.remove();
        }

        firestore.collection("rooms").document(channelID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    List<String> list = new ArrayList<>();
                    DocumentSnapshot document = task.getResult();
                        currentRoom = new Room(
                                (String) document.getData().get("name"),
                                document.getId(),
                                (String) document.getData().get("preview")
                        );
                        c.callback(null);

                }
            }
        });

        listenerRegistration = firestore.collection("rooms").document(channelID).collection("messages")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot snapshot, @Nullable FirebaseFirestoreException e) {
                        if (e != null) {
                            Log.w("TAG", "Listen failed.", e);
                            listenerRegistration.remove();
                            return;
                        }
                        if (snapshot != null) {
                            for (DocumentChange dc : snapshot.getDocumentChanges()) {
                                QueryDocumentSnapshot qd = dc.getDocument();
                                String strMessage = (String) qd.getData().get("message");
                                Map<String, Object> hashData = (Map<String, Object>) qd.getData().get("data");
                                MemberData mData = new MemberData((String)hashData.get("name"),(String)hashData.get("color"));
                                String userID = (String) qd.getData().get("clientID");
                                Timestamp ts = (Timestamp) qd.getData().get("timestamp");

                                if (!blockedUsers.contains(UID)) {
                                    boolean belongsToCurrentUser = userID.equals(UID);
                                    final Message message = new Message(strMessage, mData, belongsToCurrentUser, ts.toDate());
                                    messageCallback.onMessageClick(message);
                                }
                            }

                        }
                    }

                });
    }

    @Override
    public void sendMessage(String message) {
        Map<String, Object> msg = new HashMap<>();
        msg.put("message", message);
        msg.put("data", data);
        msg.put("clientID", UID);
        msg.put("timestamp",Timestamp.now());

        firestore.collection("rooms").document(channelID).collection("messages").add(msg);

        Map<String, Object> pr = new HashMap<>();
        pr.put("preview",data.getName()+": "+message);
        firestore.collection("rooms").document(channelID).update(pr);
    }

    @Override
    public String createNewRoom(String name,ArrayList<String> users) {
        Log.d("TAG","adding room");
        channelID = getRandomId();
        List<String> list = new ArrayList<String>();

        list.add(UID);

        for (String u:users) {
            list.add(u);
        }

        Map<String,Object> hs = new HashMap<>();
        hs.put("name",name);
        hs.put("owner",UID);
        hs.put("users",list);

        firestore.collection("rooms").document(channelID).set(hs);
        return channelID;
    }

    @Override
    public void changeRoomName(String new_name ){
        Map<String,Object> hs = new HashMap<>();
        hs.put("name",new_name);
        firestore.collection("rooms").document(channelID).update(hs);

    }

    private String getUidFromName(String name) {
        DocumentReference docRef = firestore.collection("users").document(name);
        Task<DocumentSnapshot> a = docRef.get();
        while (!a.isComplete()) {

        }
        return (String) a.getResult().get("id");
    }

    @Override
    public void getUids(ArrayList<String> name,Handler.callback callback) {
        ArrayList<String> ids = new ArrayList<>();
        for (String n:name) {
            ids.add(getUidFromName(n));
        }
        callback.callback(ids);
    }

    @Override
    public void addUserToRoom(String user) {
        firestore.collection("rooms").document(channelID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> list = (ArrayList<String>) task.getResult().get("users");
                list.add(user);
                Map<String,Object> hs = new HashMap<>();
                hs.put("users",list);
                firestore.collection("rooms").document(channelID).update(hs);
            }
        });
    }

    @Override
    public void removeUserFromRoom(String user) {
        firestore.collection("rooms").document(channelID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> list = (ArrayList<String>) task.getResult().get("users");
                list.remove(user);
                Map<String,Object> hs = new HashMap<>();
                hs.put("users",list);
                firestore.collection("rooms").document(channelID).update(hs);
            }
        });
    }

    @Override
    public void removeSelfFromRoom(OnCompleteListener listener) {
        listenerRegistration.remove();
        firestore.collection("rooms").document(channelID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                ArrayList<String> list = (ArrayList<String>) task.getResult().get("users");
                list.remove(UID);
                Map<String,Object> hs = new HashMap<>();
                hs.put("users",list);
                firestore.collection("rooms").document(channelID).update(hs).addOnCompleteListener(listener);
            }
        });
    }

    @Override
    public void log_out() {
        auth.signOut();
    }

    @Override
    public void delete_account() {
        firestore.collection("users").document(data.getName())
                .delete()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.getException() != null) {
                            Log.d("TAG", task.getException().toString());
                        }
                        auth.getCurrentUser().delete();
                    }
                });
    }

    @Override
    public Set<String> block_user(String uid) {
        blockedUsers.add(uid);
        return blockedUsers;
    }


    @Override
    public void getRooms(ChatListFragment.addRoom addRoom) {
        //Se non usas o whereArrayContains, a query e denegada e a app crashea
        firestore.collection("rooms").whereArrayContains("users",UID)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            List<String> list = new ArrayList<>();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                addRoom.add(new Room(
                                        (String) document.getData().get("name"),
                                        document.getId(),
                                        (String) document.getData().get("preview")
                                ));
                            }
                        }
                    }
                });
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#");
        while(sb.length() < 7){
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, 7);
    }

    private String getRandomId() {
        int n = 20;
        byte[] array = new byte[256];
        new Random().nextBytes(array);
        String randomString= new String(array, Charset.forName("UTF-8"));
        StringBuffer r = new StringBuffer();
        String  AlphaNumericString = randomString.replaceAll("[^A-Za-z0-9]", "");
        for (int k = 0; k < AlphaNumericString.length(); k++) {
            if (Character.isLetter(AlphaNumericString.charAt(k)) && (n > 0) || Character.isDigit(AlphaNumericString.charAt(k))  && (n > 0)) {
                r.append(AlphaNumericString.charAt(k)); n--;
            }
        }
        return (r.toString());
    }


}
