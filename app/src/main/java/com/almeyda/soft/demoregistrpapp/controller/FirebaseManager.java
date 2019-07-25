package com.almeyda.soft.demoregistrpapp.controller;

import android.app.Activity;
import android.util.Log;

import androidx.annotation.NonNull;

import com.almeyda.soft.demoregistrpapp.interfaces.OnAuthenticatePhoneNumber;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class FirebaseManager {

    private static int TIME_OUT = 60;
    private static String TAG = "FirebaseManager";
    private FirebaseAuth firebaseAuth;

    public FirebaseManager(){
        firebaseAuth = FirebaseAuth.getInstance();

    }

    public void validatePhoneNumberFirebase(String phoneNumber,
                                            PhoneAuthProvider.OnVerificationStateChangedCallbacks listener){
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNumber, TIME_OUT, TimeUnit.SECONDS,
                TaskExecutors.MAIN_THREAD, listener);
    }

    public void signInWithPhoneNumberCredential(Activity activity, PhoneAuthCredential credential,
                                                 final OnAuthenticatePhoneNumber onAuthenticatePhoneNumber){

        firebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(activity, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Log.i(TAG, "signInWithCredential:success");
                            FirebaseUser user = task.getResult().getUser();
                            onAuthenticatePhoneNumber.authenticateCompleted(user);
                        } else {
                            Log.i(TAG, "signInWithCredential:failure", task.getException());
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                onAuthenticatePhoneNumber.authenticateError();
                            }
                        }
                    }
                });
    }

    public void cerrarAuthenticate(){
        firebaseAuth.signOut();
    }

}
