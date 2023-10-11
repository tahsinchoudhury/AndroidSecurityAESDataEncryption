package com.example.dataencryptiontutorial;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private static String TAG = "MainActivity";
    private AESEncoder encoder;
    private String encodedMessage;
    private String decryptedMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Log.d(TAG, "onCreate: initializing");
        EditText editText = (EditText) findViewById(R.id.editText);
        EditText cipherEditText = (EditText) findViewById(R.id.editText2);

        Button encryptButton = (Button) findViewById(R.id.encryptButton);
        Button pasteButton = (Button) findViewById(R.id.pasteButton);
        Button decryptButton = (Button) findViewById(R.id.decryptButton);

        TextView encryptedTextView = (TextView) findViewById(R.id.encryptedText);
        TextView decryptedTextView = (TextView) findViewById(R.id.decryptedText);

        encryptButton.setOnClickListener(view -> {
            hideKeyboard(view);
            String message = editText.getText().toString();
            try {
                encoder = new AESEncoder();
                encoder.init();
                encodedMessage = encoder.encrypt(message);
            } catch (Exception e) {
                Log.d(TAG, "onCreate: Encryption error: " + e.getMessage());
                displayErrorMessage(encryptedTextView, "Sorry. Could not be encrypted.");
                return;
            }
            encryptedTextView.setText("Encrypted Text: " + encodedMessage);
        });

        decryptButton.setOnClickListener(view -> {
            hideKeyboard(view);
            String cipherText = cipherEditText.getText().toString();
            try {
                decryptedMessage = encoder.decrypt(cipherText);
            } catch (Exception e) {
                displayErrorMessage(decryptedTextView, "Sorry. Could not be decrypted.");
                Log.d(TAG, "onCreate: Decryption error: " + e.getMessage());
                return;
            }
            decryptedTextView.setText("Original Text: " + decryptedMessage);
        });

        pasteButton.setOnClickListener(view -> {
            cipherEditText.setText(encodedMessage);
        });
    }

    private void hideKeyboard(View view) {
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void displayErrorMessage(TextView textView, String message) {
        textView.setText(message);
        textView.setTextColor(Color.RED);
    }
}