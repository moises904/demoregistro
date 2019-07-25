package com.almeyda.soft.demoregistrpapp.interfaces;

import com.google.firebase.auth.FirebaseUser;

public interface OnAuthenticatePhoneNumber {

    void authenticateCompleted(FirebaseUser user);
    void authenticateError();

}
