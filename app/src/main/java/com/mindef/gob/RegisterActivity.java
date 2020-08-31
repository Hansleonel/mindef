package com.mindef.gob;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.Utilities.Constants.URL_BASE;

public class RegisterActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword, editTextDni;
    private TextView textViewDate;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editTextEmail = findViewById(R.id.edt_email_register);
        editTextPassword = findViewById(R.id.edt_password_register);
        editTextDni = findViewById(R.id.edt_dni_register);
        textViewDate = findViewById(R.id.txtV_dni_date_register);
        progressBar = findViewById(R.id.progressBarRegister);

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
                } else if (editTextPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, R.string.register_password, Toast.LENGTH_LONG).show();
                } else if (editTextDni.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, R.string.register_dni, Toast.LENGTH_LONG).show();
                } else if (textViewDate.getText().toString().trim().isEmpty()) {
                    Toast.makeText(RegisterActivity.this, R.string.register_date, Toast.LENGTH_LONG).show();
                } else {
                    register();
                }
            }
        });
    }

    private void register() {
        // String[] i = editTextEmail.getText().toString().split("@");
        // String user = i[0];
        progressBar.setVisibility(View.VISIBLE);

        JSONObject jsonRegister = new JSONObject();
        try {
            jsonRegister.put("login", editTextDni.getText().toString());
            jsonRegister.put("password", editTextPassword.getText().toString());
            jsonRegister.put("firstName", "Alfredo");
            jsonRegister.put("lastName", "ccapa");
            jsonRegister.put("email", editTextEmail.getText().toString());
            jsonRegister.put("langKey", "es");
            jsonRegister.put("fechaEmision", "2017-12-22");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("REGISTER", "register: " + jsonRegister);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_BASE + "register", jsonRegister,

                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Request response", "onResponse: " + response);

                        Intent intent = new Intent(RegisterActivity.this, NavigationActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                progressBar.setVisibility(View.GONE);
                VolleyLog.d("Request error", "onRegisterError" + error.getMessage());
                Log.d("ERROR", "onErrorResponse: " + error.getMessage());
                Toast.makeText(RegisterActivity.this, R.string.warning, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };

        // Adding request to request queue
        Volley.newRequestQueue(getApplicationContext()).add(jsonObjectRequest);

    }
}
