package com.almeyda.soft.demoregistrpapp;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
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
import com.almeyda.soft.demoregistrpapp.interfaces.OnSelectResult;
import com.almeyda.soft.demoregistrpapp.model.User;
import com.almeyda.soft.demoregistrpapp.util.MessageUtil;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.ArrayList;
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
    private List<User> lstClientes=null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initializeView();
        initializeData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadUsersRegistered();
    }

    private void initializeView(){
        setContentView(R.layout.activity_create_user_client);
        edtName = findViewById(R.id.edtName);
        edtLastName = findViewById(R.id.edtLastName);
        edtAge = findViewById(R.id.edtAge);
        btnRegister = findViewById(R.id.btnRegister);
        tvDateBirthday = findViewById(R.id.tvDateBirthday);
        rvUsers = findViewById(R.id.rvUsers);

        tvDateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
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
        lstClientes = new ArrayList<>();
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

    private void hideKeyboard(){
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private boolean validateFields(){

        return edtName.getText().toString().trim().length() > 0 &&
                edtLastName.getText().toString().trim().length() > 0 &&
                edtAge.getText().toString().trim().length() > 0 &&
                tvDateBirthday.getText().toString().trim().length() > 0 &&
                !tvDateBirthday.getText().toString().equals(getString(R.string.text_select));

    }

    private void loadUsersRegistered() {

        OnSelectResult onSelectResult = new OnSelectResult() {
            @Override
            public void onSelectResult(List<User> lstUsers) {
                lstClientes = lstUsers;
                if(lstClientes!=null) {
                    Log.i(TAG, "Hay " + lstClientes.size());
                    ClientAdapter adapter = new ClientAdapter(lstClientes);
                    rvUsers.setHasFixedSize(true);
                    rvUsers.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
                    rvUsers.setAdapter(adapter);
                }
            }
        };

        realTimeDBManager.getClients(onSelectResult);

    }


}
