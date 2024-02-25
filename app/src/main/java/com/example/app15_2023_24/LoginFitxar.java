package com.example.app15_2023_24;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginFitxar extends AppCompatActivity {
    private EditText editTextLoginEmail, editTextLoginPassword;
    private Button buttonLogin;
    private FirebaseAuth firebaseAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_fitxar);

        firebaseAuth = FirebaseAuth.getInstance();
        editTextLoginEmail = findViewById(R.id.editTextLoginEmail);
        editTextLoginPassword = findViewById(R.id.editTextLoginPassword);
        buttonLogin = findViewById(R.id.buttonLogin);

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = editTextLoginEmail.getText().toString().trim();
                String password = editTextLoginPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginFitxar.this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show();
                } else if (!isValidEmail(email)) {
                    Toast.makeText(LoginFitxar.this, "Por favor, ingresa un correo electrónico válido", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    Toast.makeText(LoginFitxar.this, "La contraseña debe tener al menos 6 caracteres", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });
    }
    private void loginUser(String email, String password) {
        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(LoginFitxar.this, "Inicio de sesión exitoso", Toast.LENGTH_SHORT).show();

                            String userEmail = firebaseAuth.getCurrentUser().getEmail();

                            redirectToNewActivity(userEmail);
                        } else {
                            Toast.makeText(LoginFitxar.this, "Error en el inicio de sesión: " + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
    private boolean isValidEmail(CharSequence target) {
        return !TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches();
    }


    //REDIRIGIR A REGISTRE AMB EL TEXT
    public void redirectToRegister(View view) {
        Intent intent = new Intent(this, RegistreFitxar.class);
        startActivity(intent);
    }

    private void redirectToNewActivity(String userEmail) {
        Intent intent = new Intent(this, MainActivity2.class);
        intent.putExtra("USER_EMAIL", userEmail);
        startActivity(intent);
    }
}