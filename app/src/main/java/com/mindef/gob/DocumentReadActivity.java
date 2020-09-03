package com.mindef.gob;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

public class DocumentReadActivity extends AppCompatActivity {

    private String idDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_read);

        idDocument = getIntent().getStringExtra("idDocument");
    }
}
