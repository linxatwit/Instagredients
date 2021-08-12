package edu.wit.mobileapp.instagredients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Locale;

public class LoginActivity extends AppCompatActivity {

    EditText email, password;
    Button login;
    TextView forgotPassword, register;
    FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        login = findViewById(R.id.btnLogin);
        forgotPassword = findViewById(R.id.forgotPassword);
        register = findViewById(R.id.gotoRegister);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(LoginActivity.this, R.style.MyAlertDialogStyle);
        pd.setMessage("Signing in...");
        pd.setCancelable(false);

        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, ForgotPassword.class);
                startActivity(it);
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent it = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(it);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }
                if (password.getText().toString().trim().isEmpty()){
                    password.setError("Required");
                    email.requestFocus();
                    return;
                }

                pd.show();
                mAuth.signInWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(LoginActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    pd.dismiss();
                                    Intent it = new Intent(LoginActivity.this, MainActivity.class);
                                    startActivity(it);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    pd.dismiss();
                                    Toast.makeText(LoginActivity.this, "Authentication failed\n" + task.getException().getMessage(),
                                            Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}