package com.example.contactsdemo;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class SelectUsersAdapter extends RecyclerView.Adapter<SelectUsersAdapter.SelectUsersViewHolder> {

    ArrayList<UserObject> selectedUsers;

    public SelectUsersAdapter(ArrayList<UserObject> selectedUsers) {
        this.selectedUsers = selectedUsers;
    }


    @NonNull
    @Override
    public SelectUsersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_selected_users, null, false);
        RecyclerView.LayoutParams lp = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        layoutView.setLayoutParams(lp);

        SelectUsersViewHolder rcv = new SelectUsersViewHolder(layoutView);
        return rcv;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectUsersViewHolder holder, int position) {
        holder.mSelectName.setText(selectedUsers.get(position).getName());
        holder.mSelectPhone.setText(selectedUsers.get(position).getPhone());

    }

    @Override
    public int getItemCount() {
        return selectedUsers.size();
    }

    public class SelectUsersViewHolder extends RecyclerView.ViewHolder {

        public TextView mSelectName, mSelectPhone;
        public SelectUsersViewHolder (View view) {
            super(view);
            mSelectName = view.findViewById(R.id.selected_name);
            mSelectPhone = view.findViewById(R.id.selected_phone);
        }

    }
}
