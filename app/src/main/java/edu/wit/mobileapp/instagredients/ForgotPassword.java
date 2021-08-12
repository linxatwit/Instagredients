package edu.wit.mobileapp.instagredients;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    EditText email;
    Button reset;
    FirebaseAuth mAuth;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }

        email = findViewById(R.id.inputEmail);
        reset = findViewById(R.id.btnReset);
        mAuth = FirebaseAuth.getInstance();
        pd = new ProgressDialog(ForgotPassword.this, R.style.MyAlertDialogStyle);
        pd.setMessage("Processing...");

        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (email.getText().toString().trim().isEmpty()){
                    email.setError("Required");
                    email.requestFocus();
                    return;
                }

                pd.show();

                mAuth.sendPasswordResetEmail(email.getText().toString()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        pd.dismiss();
                        if (task.isSuccessful()){
                            new AlertDialog.Builder(ForgotPassword.this)
                                    .setMessage("We have sent you an email with password reset link. Please check you mail and follow the procedure.")
                                    .setPositiveButton("Ok", null)
                                    .show();
                        }else{
                            Toast.makeText(ForgotPassword.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });
    }
}