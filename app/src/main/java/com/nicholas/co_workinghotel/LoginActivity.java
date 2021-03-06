package com.nicholas.co_workinghotel;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

public class LoginActivity extends AppCompatActivity
        implements View.OnClickListener {
    Button btnLogin, btnRegister;
    EditText etxUsername, etxPassword;
    TextInputLayout tilUsername, tilPassword;
    ActionBar ab;

    DBHelper dbHelper;
    ArrayList<ArrayList<String>> registered;
    String user_id;

    // Shared Preferences for
    // saving login credentials (auto login)
    SharedPreferences spref;
    SharedPreferences.Editor editor;

    private void initUI() {
        ab = getSupportActionBar();
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);
        etxUsername = findViewById(R.id.etxUsername);
        etxPassword = findViewById(R.id.etxPwd);
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPwd);
        dbHelper = new DBHelper(LoginActivity.this);

        ab.setTitle(R.string.login);
    }

    // Import all registered user IDs
    private void initDatas() {
        registered = new ArrayList<>();
        ArrayList<String> user;
        Cursor cUsers = dbHelper.allUsernameData();
        while (cUsers.moveToNext()) {
            if (registered.size() == 0) {
                cUsers.moveToFirst();
            }
            user = new ArrayList<>();
            user.add(cUsers.getString(2));
            user.add(cUsers.getString(0));
            user.add(cUsers.getString(1));
            registered.add(user);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        spref = getSharedPreferences("cwHotels", MODE_PRIVATE);
        user_id = spref.getString("loginkey", "");
        if (!user_id.equals("")) {
            Intent i = new Intent(LoginActivity.this, MainUserActivity.class);
            i.putExtra("user_id", user_id);
            startActivity(i);
            this.finish();
        } else {
            initUI();
            initDatas();

            btnLogin.setOnClickListener(this);
            btnRegister.setOnClickListener(this);

            etxUsername.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence,
                                              int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {
                    if (tilUsername.isErrorEnabled()) {
                        validateFilled(tilUsername, etxUsername);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
            etxPassword.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence,
                                              int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence,
                                          int i, int i1, int i2) {
                    if (tilPassword.isErrorEnabled()) {
                        validateFilled(tilPassword, etxPassword);
                    }
                }

                @Override
                public void afterTextChanged(Editable editable) {

                }
            });
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnLogin:
                boolean usrFilled = validateFilled(tilUsername, etxUsername);
                boolean pwdFilled = validateFilled(tilPassword, etxPassword);
                if (usrFilled  && pwdFilled) {
                    Intent i = new Intent(getApplicationContext(), MainUserActivity.class);
                    // Sekalian sharedpref untuk mengingat login
                    editor = spref.edit();
                    editor.putString("loginkey", user_id);
                    editor.apply();
                    i.putExtra("user_id", user_id);
                    startActivity(i);
                    this.finish();
                } else {
                    Toast.makeText(this, "Please check all the required fields!",
                            Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.btnRegister:
                Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(i);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        initDatas();
    }

    private boolean validateFilled(TextInputLayout til, EditText etx) {
        String username = etxUsername.getText().toString();
        String pwd = etxPassword.getText().toString();
        boolean valusername = false,
                valpassword = false;

        // Looping untuk mendapatkan data username dan password
        for (int i = 0; i < registered.size(); i++) {
            ArrayList<String> x = registered.get(i);
            // validate username
            if (username.equals(x.get(1))) {
                valusername = true;
                // validate password after username
                if (pwd.equals(x.get(2))) {
                    valpassword = true;
                    user_id = x.get(0);
                }
                break;
            }
        }

        if (etx.getText().toString().equals("")) {
            til.setError("must be filled");
            return false;
        } else if (etx.getId() == etxUsername.getId()) {
            if (!valusername) {
                til.setError("not registered");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else if (etx.getId() == etxPassword.getId()) {
            if (!valpassword) {
                til.setError("not valid");
                return false;
            } else {
                til.setError(null);
                return true;
            }
        } else {
            til.setError(null);
            return true;
        }
    }
}