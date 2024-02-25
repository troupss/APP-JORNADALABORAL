package com.example.app15_2023_24;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;

public class MainActivity2 extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private File localFile;
    public boolean isPlay = true;
    private TextView tv1, textViewEmail;

    //FECHA I HORA
    private TextView tv2;
    private Handler handler;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);

        String userEmail = getIntent().getStringExtra("USER_EMAIL");
        TextView textViewEmail = findViewById(R.id.textViewEmail);
        textViewEmail.setText(userEmail);

        ImageButton imgButton = findViewById(R.id.btn1);

        tv1 = findViewById(R.id.txtFitxar);
        tv2 = findViewById(R.id.tv2);

        tv1.setText("INICI:");

        mAuth = FirebaseAuth.getInstance();

        textViewEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, LoginFitxar.class);
                Toast.makeText(MainActivity2.this, "Sesión cerrada!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                finish(); // Cierra la actividad actual
            }
        });
        imgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Alternar entre los recursos de imagen de reproducción y pausa
                if (isPlay) {
                    // Cambiar a la imagen de pausa
                    imgButton.setImageResource(android.R.drawable.ic_media_pause);
                    isPlay = false; // Actualizar el estado a pausa
                    tv1.setText("FINALITZAR:");
                } else {
                    // Cambiar a la imagen de reproducción
                    imgButton.setImageResource(android.R.drawable.ic_media_play);
                    isPlay = true; // Actualizar el estado a reproducción
                    tv1.setText("INICI:");
                }
            }
        });
        handler = new Handler();

        // Actualiza el TextView cada segundo
        updateDateTime();

        ImageView relojButton = findViewById(R.id.relojButton);
        relojButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity2.this, "¡Botón con forma de reloj clickeado!", Toast.LENGTH_SHORT).show();

                downloadAndOpenExcel();
            }
        });
    }
    private void updateDateTime() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // Obtén la hora actual
                Calendar calendar = Calendar.getInstance();
                calendar.setTimeInMillis(System.currentTimeMillis());

                // Suma una hora al tiempo actual
                calendar.add(Calendar.HOUR_OF_DAY, 1);

                // Formatea la nueva hora
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss - dd/MM/yyyy", Locale.getDefault());
                String currentDateAndTime = sdf.format(calendar.getTime());

                // Actualiza el texto del TextView
                tv2.setText(currentDateAndTime);

                // Vuelve a llamar a la función después de un segundo
                updateDateTime();
            }
        }, 1000); // 1000 milisegundos = 1 segundo
    }

    private void downloadAndOpenExcel() {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // Crear una referencia al archivo en Firebase Storage
        StorageReference excelRef = storageReference.child("user_excel/" + mAuth.getCurrentUser().getUid() + ".xls");

        // Crear un archivo local para descargar el Excel
        localFile = new File(getLocalFilePath(MainActivity2.this, "user_data.xls"));


        excelRef.getFile(localFile)
                .addOnSuccessListener(taskSnapshot -> {
                    // Archivo descargado exitosamente
                    Log.d("FirebaseStorage", "File successfully downloaded from Firebase Storage");
                    Toast.makeText(MainActivity2.this, "File successfully downloaded from Firebase Storage", Toast.LENGTH_SHORT).show();

                    // Abrir el archivo con una aplicación externa
                    openExcelFile(MainActivity2.this, localFile);

                })
                .addOnFailureListener(exception -> {
                    // Manejar descargas fallidas
                    Log.e("FirebaseStorageError", "Error downloading file from Firebase Storage: " + exception.getMessage());
                });
    }
    private static void openExcelFile(Context context, File file) {
        try {
            // Utilizar FileProvider para obtener una URI de contenido
            Uri uri = FileProvider.getUriForFile(context, context.getPackageName() + ".provider", file);

            // Crear una intención para abrir el archivo con Google Sheets
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(uri, "application/vnd.ms-excel");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            intent.setPackage("com.google.android.apps.docs"); // Paquete de la aplicación Google Sheets

            context.startActivity(intent);
        } catch (ActivityNotFoundException e) {
            Log.e("NoFurula", "No furula " + e.getMessage());
        }
    }


    private static String getLocalFilePath(Context context, String fileName) {
        // Crear un directorio para almacenar archivos descargados (puedes personalizar la ruta)
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "DownloadedFiles");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Crear una ruta de archivo local para el archivo descargado
        return new File(directory, fileName).getAbsolutePath();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Detén el handler cuando la actividad se destruye para evitar fugas de memoria
        handler.removeCallbacksAndMessages(null);
    }

}
