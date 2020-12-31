package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mindef.gob.R;

import java.util.ArrayList;
import java.util.List;

public class ImpeachmentPersonActivity extends AppCompatActivity {

    private Spinner spVPersonDependencyImpeachment;
    private EditText edtPersonPositionImpeachment;
    private EditText edtPersonNameImpeachment;
    private EditText edtPersonNameSecondImpeachment;
    private Button btnVCancelPersonImpeachment;
    private Button btnVAddPersonImpeachment;

    private final List<String> personDependencyList = new ArrayList<>();

    private String itemSelected;

    private String personDependency;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impeachment_person);

        spVPersonDependencyImpeachment = findViewById(R.id.spVPersonDependencyImpeachment);
        edtPersonPositionImpeachment = findViewById(R.id.edtPersonPositionImpeachment);
        edtPersonNameImpeachment = findViewById(R.id.edtPersonNameImpeachment);
        edtPersonNameSecondImpeachment = findViewById(R.id.edtPersonNameSecondImpeachment);
        btnVCancelPersonImpeachment = findViewById(R.id.btnVCancelPersonImpeachment);
        btnVAddPersonImpeachment = findViewById(R.id.btnVAddPersonImpeachment);

        spinnerDataPersonDependency();

        spinnerAction(spVPersonDependencyImpeachment, personDependencyList, 1);


        btnVCancelPersonImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                impeachmentCancelResponse();
            }
        });

        btnVAddPersonImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtPersonPositionImpeachment.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ImpeachmentPersonActivity.this, R.string.sending_person_impeachment_position_empty, Toast.LENGTH_SHORT).show();
                } else if (edtPersonNameImpeachment.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ImpeachmentPersonActivity.this, R.string.sending_person_impeachment_name_empty, Toast.LENGTH_SHORT).show();
                } else if (edtPersonNameSecondImpeachment.getText().toString().trim().isEmpty()) {
                    Toast.makeText(ImpeachmentPersonActivity.this, R.string.sending_person_impeachment_second_name_empty, Toast.LENGTH_SHORT).show();
                } else {
                    impeachmentAddResponse();
                }
            }
        });
    }

    private void spinnerDataPersonDependency() {
        personDependencyList.add("Seleccione Dependencia");
        personDependencyList.add("Dependencia 1");
        personDependencyList.add("Dependencia 2");
        personDependencyList.add("Dependencia 3");
        personDependencyList.add("Dependencia 4");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(ImpeachmentPersonActivity.this, android.R.layout.simple_list_item_1, personDependencyList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVPersonDependencyImpeachment.setAdapter(spinnerAdapter);
    }

    private void spinnerAction(Spinner spinner, final List<String> listSpinner, final int parameter) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = listSpinner.get(position);

                if (parameter == 1) {
                    personDependency = itemSelected;
                    Log.d("PARAMETER", "onItemSelected: " + personDependency);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    private void impeachmentCancelResponse() {
        setResult(RESULT_CANCELED);
        finish();
    }

    private void impeachmentAddResponse() {
        Intent intentR = getIntent();
        intentR.putExtra("namePersonImpeachment", edtPersonNameSecondImpeachment.getText().toString());
        setResult(RESULT_OK, intentR);
        finish();
    }
}