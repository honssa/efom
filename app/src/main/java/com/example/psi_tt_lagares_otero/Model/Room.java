package com.example.psi_tt_lagares_otero.Model;

public class Room {
    private String name;
    private String id;
    private String preview;

    public Room(String name, String id,String preview) {
        this.name = name;
        this.id = id;
        this.preview = preview;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }
}
