package com.tony_justin.mobile_app.assassins;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.tony_justin.mobile_app.assassin.R;


/**
 * Created by Tony Nguyen on 9/29/2017.
 */

public class RegisterActivity extends AppCompatActivity {

    private EditText name;
    private EditText email;
    private EditText password;
    private EditText confirmPassword;
    private Button registerButton;
    private TextView alreadyRegistered;
    private RegisterHelper mRegisterHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        mRegisterHelper = new RegisterHelper(this);

        name = (EditText) findViewById(R.id.name);
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        confirmPassword = (EditText) findViewById(R.id.confirmpassword);

        registerButton = (Button) findViewById(R.id.email_register_button);
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean attemptSuccess = attemptRegister(name, email, password, confirmPassword);
                if(attemptSuccess) {
                    ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this);
                    //progressDialog.setIndeterminate(true);
                    progressDialog.setMessage("Creating account...");
                    progressDialog.show();
                    Intent loginIntent = new Intent(RegisterActivity.this, LoginActivity.class);
                    RegisterActivity.this.startActivity(loginIntent);
                }
            }
        });
        alreadyRegistered = (TextView) findViewById(R.id.already_register_login);
        alreadyRegistered.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent goBack = new Intent(RegisterActivity.this, LoginActivity.class);
                RegisterActivity.this.startActivity(goBack);
            }
        });

    }

    private boolean attemptRegister(EditText mname, EditText memail, EditText mpassword, EditText mconfirmPassword) {
        mname.setError(null);
        memail.setError(null);
        mpassword.setError(null);
        mconfirmPassword.setError(null);

        String name = mname.getText().toString();
        String email = memail.getText().toString();
        String password = mpassword.getText().toString();
        String confirmpassword = mconfirmPassword.getText().toString();

        boolean cancel = false;
        View focusView = null;

        if(TextUtils.isEmpty(name)) {
            mname.setError(getString(R.string.error_field_required));
            focusView = mname;
            cancel = true;
        } else if(TextUtils.isEmpty(email)) {
            memail.setError(getString(R.string.error_field_required));
            focusView = memail;
            cancel = true;
        } else if(TextUtils.isEmpty(password)) {
            mpassword.setError(getString(R.string.error_field_required));
            focusView = mpassword;
            cancel = true;
        } else if(!TextUtils.isEmpty(password) && !this.isPasswordValid(password)) {
            mpassword.setError(getString(R.string.error_invalid_password));
            focusView = mpassword;
            cancel = true;
        } else if(!password.equalsIgnoreCase(confirmpassword)) {
            mpassword.setError(getString(R.string.error_mismatch_password));
            focusView = mpassword;
            cancel = true;
        }
        Cursor data = mRegisterHelper.getData();
        while(data.moveToNext()) {
            if(email.equals(data.getString(2))) {
                memail.setError(getString(R.string.error_same_email));
                focusView = memail;
                cancel = true;
            }
        }
        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
            return false;
        } else {
            mRegisterHelper.addData(name, email, password);
            Toast.makeText(this, "Register Successful", Toast.LENGTH_SHORT).show();
            return true;

        }
    }


    private boolean isPasswordValid(String password) {
        //TODO: Replace this with your own logic
        return password.length() > 4;
    }

}

