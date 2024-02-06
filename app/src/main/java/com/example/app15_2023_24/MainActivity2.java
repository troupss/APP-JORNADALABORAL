package com.example.app15_2023_24;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

public class MainActivity2 extends AppCompatActivity {

    public boolean isPlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        ImageButton imgButton = findViewById(R.id.Btn1);



        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alternar entre los recursos de imagen de reproducción y pausa
                if (isPlay) {
                    // Cambiar a la imagen de pausa
                    imgButton.setImageResource(android.R.drawable.ic_media_pause);
                    isPlay = false; // Actualizar el estado a pausa
                } else {
                    // Cambiar a la imagen de reproducción
                    imgButton.setImageResource(android.R.drawable.ic_media_play);
                    isPlay = true; // Actualizar el estado a reproducción
                }
            }
        });
    }


}
