package com.example.app15_2023_24;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference mDatabase;
    private TextView mTextView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!FirebaseApp.getApps(this).isEmpty()) {
            FirebaseApp.initializeApp(this);
        }
        mTextView = findViewById(R.id.textView);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mDatabase.child("mensaje").setValue("Hola, Firebase!");
        mDatabase.child("mensaje").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                // Obtiene el valor y lo muestra en un TextView
                String message = dataSnapshot.getValue(String.class);
                mTextView.setText(message);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                //ERROR
            }
        });
    }

    public void Seguent(View view){
        Intent next = new Intent(this, MainActivity2.class);
        startActivity(next);
    }
}