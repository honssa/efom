package com.example.psi_tt_lagares_otero.module;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.psi_tt_lagares_otero.R;

import org.w3c.dom.Text;

import java.util.ArrayList;

public class UserInputAdapter extends RecyclerView.Adapter<UserInputAdapter.ViewHolder> {
    protected ArrayList<String> mDataset;

    public UserInputAdapter() {
        mDataset = new ArrayList<String>();
    }

    @Override
    public UserInputAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,
                                                   int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_view_entry, parent, false);
        ViewHolder vh = new ViewHolder(v, new MyCustomEditTextListener());

        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.myCustomEditTextListener.updatePosition(holder.getAdapterPosition());

        if (mDataset.get(holder.getAdapterPosition()) == null) {
            holder.mErrorText.setVisibility(View.VISIBLE);
        }
        else {
            holder.mErrorText.setVisibility(View.INVISIBLE);
        }
        holder.mEditText.setText(
                mDataset.get(holder.getAdapterPosition())
        );
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    @Override
    public void onViewAttachedToWindow(ViewHolder holder) {
        ((ViewHolder) holder).enableTextWatcher();
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull ViewHolder holder) {
        ((ViewHolder) holder).disableTextWatcher();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        // each data item is just a string in this case
        public EditText mEditText;
        public MyCustomEditTextListener myCustomEditTextListener;

        public TextView mErrorText;

        public ViewHolder(View v, MyCustomEditTextListener myCustomEditTextListener) {
            super(v);

            this.mEditText = (EditText) v.findViewById(R.id.rview_field);
            this.mErrorText = (TextView) v.findViewById(R.id.user_error);
            this.myCustomEditTextListener = myCustomEditTextListener;
        }

        void enableTextWatcher() {
            mEditText.addTextChangedListener(myCustomEditTextListener);
        }

        void disableTextWatcher() {
            mEditText.removeTextChangedListener(myCustomEditTextListener);
        }
    }

    private class MyCustomEditTextListener implements TextWatcher {
        private int position;

        public void updatePosition(int position) {
            this.position = position;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            // no op
        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
            mDataset.set(position,charSequence.toString());
        }

        @Override
        public void afterTextChanged(Editable editable) {
            // no op
        }
    }
}
