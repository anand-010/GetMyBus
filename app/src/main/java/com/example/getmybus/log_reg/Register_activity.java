package com.example.getmybus.log_reg;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.getmybus.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class Register_activity extends AppCompatActivity {
    EditText email,password;
    FirebaseAuth mAuth;
    LinearLayout goto_reg;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register3);
        goto_reg = findViewById(R.id.contiue_register);
        email = findViewById(R.id.email_regiter);
        password = findViewById(R.id.password_register);
        mAuth = FirebaseAuth.getInstance();
        goto_reg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email1= email.getText().toString();
                String passwor1 = password.getText().toString();
                mAuth.createUserWithEmailAndPassword(email1,passwor1)
                        .addOnSuccessListener(new OnSuccessListener<AuthResult>() {
                            @Override
                            public void onSuccess(AuthResult authResult) {
                                Toast.makeText(Register_activity.this,"user added",Toast.LENGTH_SHORT).show();
                                FirebaseUser user = mAuth.getCurrentUser();
                                user.sendEmailVerification()
                                        .addOnCompleteListener(Register_activity.this, new OnCompleteListener() {
                                            @Override
                                            public void onComplete(@NonNull Task task) {

                                                if (task.isSuccessful()) {
                                                    Toast.makeText(Register_activity.this,
                                                            "Verification email sent to " + user.getEmail() + user.isEmailVerified(),
                                                            Toast.LENGTH_SHORT).show();
                                                } else {
                                                    Log.e("tag", "sendEmailVerification", task.getException());
                                                    Toast.makeText(Register_activity.this,
                                                            "Failed to send verification email.",
                                                            Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(Register_activity.this,"user adding faild",Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });
    }
}
