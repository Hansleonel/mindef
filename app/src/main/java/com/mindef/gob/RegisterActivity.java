package com.mindef.gob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextDni;
    private TextView textViewDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.edt_email_register);
        editTextPassword = findViewById(R.id.edt_password_register);
        editTextDni = findViewById(R.id.edt_dni_register);
        textViewDate = findViewById(R.id.txtV_dni_date_register);

        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);

        textViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_NoActionBar_MinWidth, dateSetListener, year, month, day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                String monthString = String.valueOf(month + 1);
                month = month + 1;
                if (month < 10) {
                    monthString = "0" + month;
                }
                String dateString = day + "-" + monthString + "-" + year;
                textViewDate.setText(dateString);
            }
        };


        findViewById(R.id.btn_accept_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, R.string.register_email, Toast.LENGTH_LONG).show();
                } else if (!Patterns.EMAIL_ADDRESS.matcher(editTextEmail.getText().toString()).matches()) {
                    Toast.makeText(RegisterActivity.this, R.string.register_mail_validation, Toast.LENGTH_LONG).show();
                }
                register();
            }
        });
    }

    private void register() {

    }
}
