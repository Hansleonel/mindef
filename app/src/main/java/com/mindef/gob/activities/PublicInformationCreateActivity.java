package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.mindef.gob.R;

import java.util.ArrayList;
import java.util.List;

public class PublicInformationCreateActivity extends AppCompatActivity {

    private Spinner spVTypePersonPublicInformation;
    private Spinner spVOfficePublicInformation;
    private Spinner spVMethodPublicInformation;
    private EditText edtSubjectPublicInformation;
    private TextInputEditText edtPublicInformationDescription;
    private Button btnVPublicInformation;

    private final List<String> typePersonList = new ArrayList<>();
    private final List<String> typeOfficeList = new ArrayList<>();
    private final List<String> typeMethodList = new ArrayList<>();

    private String itemSelected;

    private String typePerson;
    private String typeOffice;
    private String typeMethod;
    private String titleInformation;
    private String descriptionInformation;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_information_create);

        spVTypePersonPublicInformation = findViewById(R.id.spVTypePersonPublicInformation);
        spVOfficePublicInformation = findViewById(R.id.spVOfficePublicInformation);
        spVMethodPublicInformation = findViewById(R.id.spVMethodPublicInformation);
        edtSubjectPublicInformation = findViewById(R.id.edtSubjectPublicInformation);
        edtPublicInformationDescription = findViewById(R.id.edtPublicInformationDescription);
        btnVPublicInformation = findViewById(R.id.btnVPublicInformation);

        spinnerDataTypePerson();
        spinnerDataTypeInformation();
        spinnerDataTypeMethod();

        spinnerAction(spVTypePersonPublicInformation, typePersonList, 1);
        spinnerAction(spVOfficePublicInformation, typeOfficeList, 2);
        spinnerAction(spVMethodPublicInformation, typeMethodList, 3);

        btnVPublicInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSubjectPublicInformation.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PublicInformationCreateActivity.this, "Ingrese Asunto", Toast.LENGTH_SHORT).show();
                } else if (edtPublicInformationDescription.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PublicInformationCreateActivity.this, "Ingrese Descripción", Toast.LENGTH_SHORT).show();
                } else {
                    titleInformation = edtSubjectPublicInformation.getText().toString();
                    descriptionInformation = edtPublicInformationDescription.getText().toString();
                    sendInformation(typePerson, typeOffice, typeMethod, titleInformation, descriptionInformation);
                }
            }
        });

    }

    private void spinnerDataTypePerson() {
        typePersonList.add("Seleccione un tipo de Persona");
        typePersonList.add("Natural");
        typePersonList.add("Jurídica");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typePersonList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVTypePersonPublicInformation.setAdapter(spinnerAdapter);
    }

    private void spinnerDataTypeInformation() {
        typeOfficeList.add("Seleccione Oficina");
        typeOfficeList.add("Despacho Ministerial");
        typeOfficeList.add("Centro de Operaciones de Emergencia Nacional");
        typeOfficeList.add("Despacho Viceministerial de políticas para la defensa");
        typeOfficeList.add("Despacho Viceministerial de recursos para la defensa");
        typeOfficeList.add("Secretaría General");
        typeOfficeList.add("órgano de Control Institucional");
        typeOfficeList.add("Inspectoría General");
        typeOfficeList.add("Procuraduría Pública");
        typeOfficeList.add("Otro");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typeOfficeList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVOfficePublicInformation.setAdapter(spinnerAdapter);
    }

    private void spinnerDataTypeMethod() {
        typeMethodList.add("Seleccione Metodo Entrega");
        typeMethodList.add("Copia Simple");
        typeMethodList.add("Copia Certificada");
        typeMethodList.add("CD");
        typeMethodList.add("Email");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typeMethodList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVMethodPublicInformation.setAdapter(spinnerAdapter);
    }

    private void spinnerAction(Spinner spinner, final List<String> listSpinner, final int parameter) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = listSpinner.get(position);

                if (parameter == 1) {
                    typePerson = itemSelected;
                    Log.d("PARAMETER", "onItemSelected: " + typePerson);
                } else if (parameter == 2) {
                    typeOffice = itemSelected;
                    Log.d("PARAMETER", "onItemSelected: " + typeOffice);
                } else if (parameter == 3) {
                    typeMethod = itemSelected;
                    Log.d("PARAMETER", "onItemSelected: " + typeMethod);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sendInformation(String typePerson, String typeOffice, String typeMethod, String titleInformation, String descriptionInformation) {
        Log.d("PublicInformationCreate", "sendInformation: " + typePerson + " " + typeOffice + " " + typeMethod + " " + titleInformation + " " + descriptionInformation);
    }
}