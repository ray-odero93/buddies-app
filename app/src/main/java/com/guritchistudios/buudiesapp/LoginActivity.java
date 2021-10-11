package com.guritchistudios.buudiesapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;

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

        mLogin.setOnClickListener(view -> {
            String userEmail = email.getText().toString().trim();
            String userPass = password.getText().toString().trim();

            if (!Patterns.EMAIL_ADDRESS.matcher(userEmail).matches()) {
                email.setError("Invalid email address.");
                email.setFocusable(true);
            }else {
                loginUser(userEmail, userPass);
            }
        });

        needNewAccount.setOnClickListener(view -> startActivity(new Intent(LoginActivity.this, RegistrationActivity.class)));

        recoveryPass.setOnClickListener(view -> showRecoveryPasswordDialog());
    }

    @SuppressLint("SetTextI18n")
    private void showRecoveryPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Recover password.");
        LinearLayout linearLayout = new LinearLayout(this);
        final EditText recoveryEmail = new EditText(this);
        recoveryEmail.setText("@string/email_text");
        recoveryEmail.setMinEms(16);
        recoveryEmail.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        linearLayout.addView(recoveryEmail);
        linearLayout.setPadding(10, 10, 10, 10);
        builder.setView(linearLayout);

        builder.setPositiveButton("Recover", (dialogInterface, i) -> {
            String userEmail = email.getText().toString().trim();
            startRecovery(userEmail);
        });

        builder.setNegativeButton("Cancel", (dialogInterface, i) -> dialogInterface.dismiss());

        builder.create().show();
    }

    private void startRecovery(String userEmail) {
        loadingBar.setMessage("Sending e-mail...");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        mAuth.sendPasswordResetEmail(userEmail).addOnCompleteListener(task -> {
            loadingBar.dismiss();
            if (task.isSuccessful()) {
                Toast.makeText(LoginActivity.this, "E-mail sent.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(LoginActivity.this, "Sending failed.", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(e -> {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                });
    }

    private void loginUser(String userEmail, String userPass) {
        loadingBar.setMessage("Logging in...");
        loadingBar.show();

        mAuth.signInWithEmailAndPassword(userEmail, userPass).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                loadingBar.dismiss();
                FirebaseUser user = mAuth.getCurrentUser();

                if (task.getResult().getAdditionalUserInfo().isNewUser()) {
                    String userEmail1 = user.getEmail();
                    String userId = user.getUid();
                    HashMap<Object, String> hashMap = new HashMap<>();
                    hashMap.put("email", userEmail1);
                    hashMap.put("uid", userId);
                    hashMap.put("name", "");
                    hashMap.put("onLineStatus", "online");
                    hashMap.put("typingTo", "noOne");
                    hashMap.put("phone", "");
                    hashMap.put("image", "");
                    hashMap.put("cover", "");
                    FirebaseDatabase database = FirebaseDatabase.getInstance();
                    DatabaseReference reference = database.getReference("Users");
                    reference.child(userId).setValue(hashMap);
                }
                Toast.makeText(LoginActivity.this, "Registered user" + user.getEmail(), Toast.LENGTH_LONG).show();
                Intent mainIntent = new Intent(LoginActivity.this, RegistrationActivity.class);
                mainIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(mainIntent);
                finish();
            } else {
                loadingBar.dismiss();
                Toast.makeText(LoginActivity.this, "Login failed.", Toast.LENGTH_LONG).show();
            }
        })
                .addOnFailureListener(e -> {
                    loadingBar.dismiss();
                    Toast.makeText(LoginActivity.this, "Error occurred.", Toast.LENGTH_SHORT).show();
                });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }
}