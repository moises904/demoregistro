package com.almeyda.soft.demoregistrpapp;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.almeyda.soft.demoregistrpapp.controller.FirebaseManager;
import com.almeyda.soft.demoregistrpapp.interfaces.OnAuthenticatePhoneNumber;
import com.almeyda.soft.demoregistrpapp.util.MessageUtil;
import com.almeyda.soft.demoregistrpapp.util.Validator;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

public class LoginActivity extends AppCompatActivity {
    private static String TAG = "FIREBASE";
    private Button btnLoguin;
    private EditText edtPhoneNumber;
    private FirebaseManager firebaseManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
        initializeData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        firebaseManager.cerrarAuthenticate();
    }

    private void initializeView(){
        setContentView(R.layout.activity_login);
        edtPhoneNumber = findViewById(R.id.edtPhoneNumber);
        btnLoguin = findViewById(R.id.btnLoguin);

        edtPhoneNumber.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {

                if(i == EditorInfo.IME_ACTION_DONE){
                    btnLoguin.performClick();
                }
                return false;
            }
        });

        btnLoguin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(validatePhoneNumber()){
                    initializationAuthentication();
                }else{
                    MessageUtil.showMessage(getApplicationContext(),getString(R.string.message_input_phonenumber));
                }
            }
        });
    }

    private void initializeData(){

        firebaseManager = new FirebaseManager();
    }


    private boolean validatePhoneNumber(){

        String phoneNumber = edtPhoneNumber.getText().toString().trim();

        return (Validator.validatePhoneNumber(phoneNumber));

    }

    private void goToRegister(){

        Intent intentRegister = new Intent(this, RegisterClientActivity.class);
        startActivity(intentRegister);
        finish();
    }

    private void initializationAuthentication(){

        String phoneNumber = "+51"+edtPhoneNumber.getText().toString().trim();

        PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks = new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onVerificationCompleted(PhoneAuthCredential credential) {
                Log.i(TAG, "onVerificationCompleted:" + credential.getSignInMethod());
                Log.i(TAG, "onVerificationCompleted:" + credential.getProvider());
                Log.i(TAG, "onVerificationCompleted:" + credential.getSmsCode());

                signInWithPhoneAuthCredential(credential);
            }

            @Override
            public void onVerificationFailed(FirebaseException e) {
                Log.i(TAG, "onVerificationFailed", e);
                MessageUtil.showMessage(getApplicationContext(),getString(R.string.message_phone_number_invalid));
            }

            @Override
            public void onCodeSent(String verificationId,
                                   PhoneAuthProvider.ForceResendingToken token) {

                Log.i(TAG, "onCodeSent:" + verificationId);
                Log.i(TAG, "token:" + token);

            }
        };

        firebaseManager.validatePhoneNumberFirebase(phoneNumber, mCallbacks);

    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        OnAuthenticatePhoneNumber onAuthenticatePhoneNumber = new OnAuthenticatePhoneNumber() {

            @Override
            public void authenticateCompleted(FirebaseUser user) {
                    goToRegister();
            }

            @Override
            public void authenticateError() {
                   MessageUtil.showMessage(getApplicationContext(),getString(R.string.message_authenticate_failed));
            }
        };

        firebaseManager.signInWithPhoneNumberCredential(this, credential, onAuthenticatePhoneNumber);
    }

}
