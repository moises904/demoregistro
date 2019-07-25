package com.almeyda.soft.demoregistrpapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.almeyda.soft.demoregistrpapp.R;
import com.almeyda.soft.demoregistrpapp.model.User;

import java.util.List;

public class ClientAdapter extends RecyclerView.Adapter<ClientAdapter.ViewHolder> {


    private List<User> lstUsers;
    public ClientAdapter(List<User> users){
        lstUsers = users;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.item_client, parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final User user = lstUsers.get(position);
        holder.tvClient.setText(user.getFirstName() + " "+user.getLastName());
    }

    @Override
    public int getItemCount() {
        return lstUsers.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvClient;
        public ViewHolder(View itemView) {
            super(itemView);
            this.tvClient = (TextView) itemView.findViewById(R.id.tvClient);
        }
    }
}
