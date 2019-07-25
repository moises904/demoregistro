package com.almeyda.soft.demoregistrpapp.controller;


import com.almeyda.soft.demoregistrpapp.interfaces.OnSelectResult;
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

    public void getClients(final OnSelectResult onSelectResult){

        final List<User> listClients = new ArrayList<>();
        Query query = mDatabase.limitToFirst(50);

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot child: dataSnapshot.getChildren()) {
                    HashMap<String,String> hm = (HashMap<String, String>) child.getValue();
                    User user = new User();
                    user.setFirstName(hm.get(FIELD_NAME));
                    user.setLastName(hm.get(FIELD_LAST_NAME));
                    user.setAge(hm.get(FIELD_AGE));
                    user.setDateBirthday(hm.get(FIELD_DATE_BIRTHDAY));
                    listClients.add(user);
                }
                onSelectResult.onSelectResult(listClients);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
