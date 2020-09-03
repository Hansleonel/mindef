package com.mindef.gob.Activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;

import com.mindef.gob.R;

public class DocumentReadActivity extends AppCompatActivity {

    private String idDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_read);

        idDocument = getIntent().getStringExtra("idDocument");
    }
}
