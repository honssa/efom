package com.example.psi_tt_lagares_otero.Model;

import com.google.firebase.Timestamp;
import java.util.Date;

public class Message implements Comparable<Message> {
    private String text; // message body
    private MemberData memberData; // data of the user that sent this message
    private Date timestamp;
    private boolean belongsToCurrentUser; // is this message sent by us?

    public Message(String text, MemberData memberData, boolean belongsToCurrentUser, Date timestamp) {
        this.text = text;
        this.memberData = memberData;
        this.belongsToCurrentUser = belongsToCurrentUser;
        this.timestamp = timestamp;
    }

    public String getText() {
        return text;
    }

    public MemberData getMemberData() {
        return memberData;
    }

    public Date getTimestamp() {return timestamp;}

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }


    @Override
    public int compareTo(Message o) {
        return getTimestamp().compareTo(o.getTimestamp());
    }

}
