package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.mindef.gob.R;

import java.util.ArrayList;
import java.util.List;

public class DocumentCreateActivity extends AppCompatActivity {

    private Spinner spV_typeDocument;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_create);

        spV_typeDocument = findViewById(R.id.spV_typeDocument);

        List<String> tipoDoc = new ArrayList<>();
        tipoDoc.add("Seleccione el tipo de Documento");
        tipoDoc.add("Carta");
        tipoDoc.add("Solicitud");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, tipoDoc);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spV_typeDocument.setAdapter(spinnerAdapter);
    }
}
