package com.guritchistudios.buudiesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.WindowDecorActionBar;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {
    private EditText name, email, password;
    private Button mLogin;
    private TextView needNewAccount, recoveryPass;
    FirebaseUser currentUser;
    private FirebaseAuth mAuth;
    private ProgressDialog loadingBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle("Create account.");
        actionBar.setDisplayShowHomeEnabled(true);
        actionBar.setDisplayHomeAsUpEnabled(true);

        email = findViewById(R.id.login_email);
        password = findViewById(R.id.login_pass);
        needNewAccount = findViewById(R.id.new_account);
        recoveryPass = findViewById(R.id.forget_pass);
        mLogin = findViewById(R.id.login_button);
        mAuth = FirebaseAuth.getInstance();
        loadingBar = new ProgressDialog(this);

        if (mAuth != null) {
            currentUser = mAuth.getCurrentUser();
        }

        mLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userEmail = email.getText().toString().trim();
                String userPass = password.getText().toString().trim();

                if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                    email.setError("Invalid email address.");
                    email.setFocusable(true);
                }else {
                    loginUser(userEmail, userPass);
                }
            }
        });

        needNewAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegistrationActivity.class));
            }
        });

        recoveryPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showRecoveryPasswordDialog();
            }
        });
    }

    private void showRecoveryPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover password.");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText recoveryEmail = new EditText(this);
        recoveryEmail.setText("Email");
        recoveryEmail.setMinEms(16);
        recoveryEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(recoveryEmail);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String userEmail = email.getText().toString().trim();
                startRecovery(userEmail);
            }
        });
    }
}