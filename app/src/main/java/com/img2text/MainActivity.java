package com.img2text;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.app.Activity;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.text.TextRecognition;
import com.google.mlkit.vision.text.TextRecognizer;
import com.google.mlkit.vision.text.latin.TextRecognizerOptions;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    ImageView clear, getImage, copy;
    EditText recognizedText;
    Uri imageUri;
    TextRecognizer textRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        clear = findViewById(R.id.clear);
        getImage = findViewById(R.id.getImage);
        copy = findViewById(R.id.copy);
        recognizedText = findViewById(R.id.recognizedText);

        textRecognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS);

        getImage.setOnClickListener(view -> ImagePicker.with(MainActivity.this)
                .crop()
                .compress(1024)
                .maxResultSize(1080, 1080)
                .start());

        copy.setOnClickListener(v -> {
            String text = recognizedText.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(MainActivity.this, "There is no text to copy", Toast.LENGTH_SHORT).show();
            } else {
                ClipboardManager clipboardManager = (ClipboardManager) getSystemService(MainActivity.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("Data", recognizedText.getText().toString());
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(MainActivity.this, "Text Copy To Clipboard", Toast.LENGTH_SHORT).show();
            }
        });

        clear.setOnClickListener(v -> {
            String text = recognizedText.getText().toString();

            if (text.isEmpty()) {
                Toast.makeText(MainActivity.this, "There Is No Text To Clear", Toast.LENGTH_SHORT).show();
            } else {
                recognizedText.setText("");
            }
        });

        // Add a click listener for the history button
        Button historyButton = findViewById(R.id.historyButton);
        historyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Open the history activity
                Intent intent = new Intent(MainActivity.this, HistoryActivity.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ImagePicker.REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data != null) {
                imageUri = data.getData();
                Toast.makeText(this, "image selected", Toast.LENGTH_SHORT).show();
                recognizeText();
            }
        } else {
            Toast.makeText(this, "image not selected", Toast.LENGTH_SHORT).show();
        }
    }

    private void recognizeText() {
        if (imageUri != null) {
            try {
                InputImage inputImage = InputImage.fromFilePath(MainActivity.this, imageUri);

                textRecognizer.process(inputImage)
                        .addOnSuccessListener(text -> {
                            String recognizeText = text.getText();
                            recognizedText.setText(recognizeText);
                            // Save the scanned text to history
                            ScanHistoryManager.saveScanResult(MainActivity.this, recognizeText);
                        })
                        .addOnFailureListener(e -> Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

