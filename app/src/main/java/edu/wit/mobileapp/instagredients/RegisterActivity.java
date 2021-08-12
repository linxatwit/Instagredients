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

public class RegisterActivity extends AppCompatActivity {


    EditText email, password;
    Button register;
    FirebaseAuth mAuth;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }


        email = findViewById(R.id.inputEmail);
        password = findViewById(R.id.inputPassword);
        register = findViewById(R.id.btnRegister);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(RegisterActivity.this, R.style.MyAlertDialogStyle);
        pd.setMessage("Signing up...");
        pd.setCancelable(false);


        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }
                if (password.getText().toString().trim().isEmpty()){
                    password.setError("Required");
                    password.requestFocus();
                    return;
                }

                pd.show();
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                pd.dismiss();
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegisterActivity.this, "Registered Successfully", Toast.LENGTH_SHORT).show();
                                    FirebaseAuth.getInstance().signOut();
                                    Intent it = new Intent(RegisterActivity.this, LoginActivity.class);
                                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                                    startActivity(it);
                                } else {
                                    Toast.makeText(RegisterActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });
    }
}