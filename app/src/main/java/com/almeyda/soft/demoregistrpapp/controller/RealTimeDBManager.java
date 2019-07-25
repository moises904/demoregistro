package com.almeyda.soft.demoregistrpapp.controller;

import android.util.Log;

import com.almeyda.soft.demoregistrpapp.model.User;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.almeyda.soft.demoregistrpapp.util.Constants.FIELD_AGE;
import static com.almeyda.soft.demoregistrpapp.util.Constants.FIELD_DATE_BIRTHDAY;
import static com.almeyda.soft.demoregistrpapp.util.Constants.FIELD_LAST_NAME;
import static com.almeyda.soft.demoregistrpapp.util.Constants.FIELD_NAME;
import static com.almeyda.soft.demoregistrpapp.util.Constants.USUARIOS;

public class RealTimeDBManager {

    private DatabaseReference mDatabase;

    public RealTimeDBManager(){
        mDatabase = FirebaseDatabase.getInstance().getReference(USUARIOS);

    }

    public void insertNewClient(User newUser, OnSuccessListener onSuccessListener, OnFailureListener onFailureListener){

        Map<String,String> hmUsers = new HashMap<>();
        hmUsers.put(FIELD_NAME, newUser.getFirstName());
        hmUsers.put(FIELD_LAST_NAME, newUser.getLastName());
        hmUsers.put(FIELD_AGE,newUser.getAge());
        hmUsers.put(FIELD_DATE_BIRTHDAY,newUser.getDateBirthday());

        mDatabase.child(USUARIOS+ new Date().getTime()).setValue(hmUsers).addOnSuccessListener(onSuccessListener)
                                                                         .addOnFailureListener(onFailureListener);

    }

    public List<User> getClients(){

        final List<User> listClients = new ArrayList<>();
        Query query = mDatabase.child(USUARIOS);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                List<User> list = new ArrayList<User>();

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    Log.i("DataBase",child.getKey());
                    list.add(child.getValue(User.class));
                }

                listClients.addAll(list);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        return listClients;
    }
}
