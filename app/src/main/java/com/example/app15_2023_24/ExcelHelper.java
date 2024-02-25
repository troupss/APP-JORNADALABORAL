package com.example.app15_2023_24;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;

import jxl.Workbook;
import jxl.write.Label;
import jxl.write.WritableSheet;
import jxl.write.WritableWorkbook;

public class ExcelHelper {
    public static void createExcelFile(Context context, String userEmail) {
        try {
            // Create a workbook and add a sheet to it
            WritableWorkbook workbook = Workbook.createWorkbook(new File(getFilePath(context)));
            WritableSheet sheet = workbook.createSheet("Sheet1", 0);

            // Add content to the sheet (for example, user email)
            sheet.addCell(new Label(0, 0, "User Email"));
            sheet.addCell(new Label(1, 0, userEmail));

            // Write the workbook content
            workbook.write();
            workbook.close();

            // Upload the Excel file to Firebase Storage
            uploadToFirebaseStorage(context, userEmail);

        } catch (Exception e) {
            Log.e("ExcelCreationError", "Error creating Excel file: " + e.getMessage());
        }
    }

    private static String getFilePath(Context context) {
        // Create a directory for Excel files (you may want to customize the path)
        File directory = new File(context.getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS), "ExcelFiles");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        // Create a file path for the new Excel file
        String fileName = "user_data.xls";
        return new File(directory, fileName).getAbsolutePath();
    }

    private static void uploadToFirebaseStorage(Context context, String userEmail) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageReference = storage.getReference();

        // Create a reference to the file in Firebase Storage
        StorageReference excelRef = storageReference.child("user_excel/" + mAuth.getCurrentUser().getUid() + ".xls");

        // Upload the file to Firebase Storage
        File file = new File(getFilePath(context));
        excelRef.putFile(android.net.Uri.fromFile(file))
                .addOnSuccessListener(taskSnapshot -> {
                    // File uploaded successfully
                    Log.d("FirebaseStorage", "File successfully uploaded to Firebase Storage");
                    file.delete(); // Delete the local file after successful upload
                })
                .addOnFailureListener(exception -> {
                    // Handle unsuccessful uploads
                    Log.e("FirebaseStorageError", "Error uploading file to Firebase Storage: " + exception.getMessage());
                });
    }
}