package com.example.app15_2023_24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegistreFitxar extends AppCompatActivity {
    private EditText editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button buttonRegister;

    private FirebaseAuth mAuth;
    private DatabaseReference databaseReference;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registre_fitxar);

        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonRegister = findViewById(R.id.buttonRegister);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference();
        buttonRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password, confirmpassword;
                email = String.valueOf(editTextEmail.getText());
                password = String.valueOf(editTextPassword.getText());
                confirmpassword = String.valueOf(editTextConfirmPassword.getText());

                if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                    Toast.makeText(RegistreFitxar.this, "Formato de correo electrónico incorrecto", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (password.length() < 6) {
                    Toast.makeText(RegistreFitxar.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!password.equals(confirmpassword)) {
                    Toast.makeText(RegistreFitxar.this, "Las contraseñas no coinciden", Toast.LENGTH_SHORT).show();
                    return;
                }

                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(RegistreFitxar.this, "Cuenta creada.", Toast.LENGTH_SHORT).show();

                                    ExcelHelper.createExcelFile(getApplicationContext(), email);

                                    Intent intent = new Intent(RegistreFitxar.this, LoginFitxar.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    if (task.getException() instanceof FirebaseAuthUserCollisionException) {
                                        // If the email is already registered
                                        Toast.makeText(RegistreFitxar.this, "El correo electrónico ya está registrado", Toast.LENGTH_SHORT).show();
                                    } else {
                                        // If any other registration error occurs
                                        Log.e("RegistrationError", "Authentication failed: " + task.getException().getMessage());
                                    }
                                }
                            }
                        });
            }
        });
    }
    public void redirectToLogin(View view) {
        Intent intent = new Intent(this, LoginFitxar.class);
        startActivity(intent);
    }
}