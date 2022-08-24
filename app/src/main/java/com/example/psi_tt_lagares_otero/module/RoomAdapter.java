package com.example.psi_tt_lagares_otero.module;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.psi_tt_lagares_otero.DataConnection.Handler;
import com.example.psi_tt_lagares_otero.Model.Room;
import com.example.psi_tt_lagares_otero.R;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class RoomAdapter extends BaseAdapter {
    List<Room> rooms = new ArrayList<Room>();
    Context context;


    public ItemClickListener onItemClickListener;

    DateFormat df = new SimpleDateFormat("HH:mm:ss");

    public RoomAdapter(Context context) {
        this.context = context;
    }

    public void add(Room room) {
        Log.d("TEST","TESTEO");
        this.rooms.add(room);
        notifyDataSetChanged();
    }

    public void clean() {
        rooms = new ArrayList<>();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return rooms.size();
    }

    @Override
    public Object getItem(int i) {
        return rooms.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    public void setItemClickListener(ItemClickListener clickListener) {
        onItemClickListener = clickListener;
    }



    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        RoomViewHolder holder = new RoomViewHolder();
        LayoutInflater messageInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        Room room = rooms.get(i);

        convertView = messageInflater.inflate(R.layout.room_entry, null);

        convertView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.onItemClick(view, room);
            }
        });

        holder.name = convertView.findViewById(R.id.room_name);
        holder.preview = convertView.findViewById(R.id.room_preview);
        convertView.setTag(holder);
        holder.name.setText(room.getName());
        holder.preview.setText(room.getPreview());

        return convertView;
    }

    public interface ItemClickListener {
        void onItemClick(View view, Room i);
    }

}



class RoomViewHolder {
    public TextView name;
    public TextView preview;
}
