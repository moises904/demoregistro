package com.almeyda.soft.demoregistrpapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.almeyda.soft.demoregistrpapp.adapter.ClientAdapter;
import com.almeyda.soft.demoregistrpapp.controller.RealTimeDBManager;
import com.almeyda.soft.demoregistrpapp.model.User;
import com.almeyda.soft.demoregistrpapp.util.MessageUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Calendar;
import java.util.List;

public class RegisterClientActivity extends AppCompatActivity {

    private static String TAG = "FIREBASE";

    private EditText edtName;
    private EditText edtLastName;
    private EditText edtAge;
    private Button btnRegister;
    private RecyclerView rvUsers;
    private TextView tvDateBirthday;
    private int mYear, mMonth, mDay;
    private RealTimeDBManager realTimeDBManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
        initializeData();
    }

    private void initializeView(){
        setContentView(R.layout.activity_create_user_client);
        edtName = findViewById(R.id.edtName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAge = findViewById(R.id.edtAge);
        btnRegister = findViewById(R.id.btnRegister);
        tvDateBirthday = findViewById(R.id.tvDateBirthday);
        rvUsers = findViewById(R.id.rvUsers);

        edtAge.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if(i == EditorInfo.IME_ACTION_DONE){
                    btnRegister.performClick();
                }
                return false;
            }
        });
        tvDateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDatePickers();
            }
        });

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validateFields()) {
                    saveData();
                }else {
                    MessageUtil.showMessage(getApplicationContext(),
                                            getString(R.string.message_register_incompleted));
                }
            }
        });
    }

    private void initializeData(){
        realTimeDBManager = new RealTimeDBManager();

    }

    private void showDatePickers(){

        final Calendar calendar = Calendar.getInstance();
        mYear = calendar.get(Calendar.YEAR);
        mMonth = calendar.get(Calendar.MONTH);
        mDay = calendar.get(Calendar.DAY_OF_MONTH);


        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {

                    @SuppressLint("DefaultLocale")
                    @Override
                    public void onDateSet(DatePicker view, int year,
                                          int monthOfYear, int dayOfMonth) {

                        tvDateBirthday.setText(String.format("%d %s %d %s %d",dayOfMonth ,"-", (monthOfYear + 1) , "-", year));

                    }
                }, mYear, mMonth, mDay);
        datePickerDialog.show();

    }

    private void saveData(){

        User user = new User();
        user.setFirstName(edtName.getText().toString().trim());
        user.setLastName(edtLastName.getText().toString().trim());
        user.setAge(edtAge.getText().toString().trim());
        user.setDateBirthday(tvDateBirthday.getText().toString().trim());

        OnSuccessListener onSuccessListener = new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                clearFields();
                loadUsersRegistered();
            }
        };

        OnFailureListener onFailureListener = new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        };
        realTimeDBManager.insertNewClient(user,onSuccessListener, onFailureListener);

    }

    private void clearFields(){

        edtName.setText("");
        edtLastName.setText("");
        edtAge.setText("");
        tvDateBirthday.setText(getString(R.string.text_select));
    }

    private boolean validateFields(){

        return edtName.getText().toString().trim().length() > 0 &&
                edtLastName.getText().toString().trim().length() > 0 &&
                edtAge.getText().toString().trim().length() > 0 &&
                tvDateBirthday.getText().toString().trim().length() > 0;

    }

    private void loadUsersRegistered() {

        List<User> lstClientes = realTimeDBManager.getClients();
        Log.i(TAG, "Hay "+lstClientes.size());
        ClientAdapter adapter = new ClientAdapter(lstClientes);
        rvUsers.setHasFixedSize(true);
        rvUsers.setLayoutManager(new LinearLayoutManager(this));
        rvUsers.setAdapter(adapter);
    }


}
