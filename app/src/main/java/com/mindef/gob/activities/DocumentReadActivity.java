package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindef.gob.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class DocumentReadActivity extends AppCompatActivity {

    private TextView txtV_document_read_title, txtV_document_read_subject, txtV_document_read_date;

    private String codeDocument;
    private String linkDocument;

    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_read);

        txtV_document_read_title = findViewById(R.id.txtV_document_read_title);
        txtV_document_read_subject = findViewById(R.id.txtV_document_read_subject);
        txtV_document_read_date = findViewById(R.id.txtV_document_read_date);

        codeDocument = getIntent().getStringExtra("codeDocument");
        Log.d("DocumentReadActivity", "onCreate: " + codeDocument);

        findViewById(R.id.lV_pdfViewer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (linkDocument != null) {
                    Intent i = new Intent(DocumentReadActivity.this, DocumentViewActivity.class);
                    i.putExtra("linkDocument", linkDocument);
                    startActivity(i);
                } else {
                    Toast.makeText(getApplicationContext(), R.string.warning_pdf, Toast.LENGTH_LONG).show();
                }
            }
        });

        getUserTokenShared();
        searchDocument();

    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void searchDocument() {
        JsonObjectRequest searchDocumentObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_BASE + "sistradoc/documento/detalle/" + codeDocument, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            int id = response.getInt("id");
                            String serie = response.getString("serie");
                            String asunto = response.getString("asunto");
                            String fecha = response.getString("fecha");
                            // TODO recordar corregir el link devuelto por el servidor
                            linkDocument = response.getString("link");

                            txtV_document_read_title.setText(serie);
                            txtV_document_read_subject.setText(asunto);
                            txtV_document_read_date.setText(fecha);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError" + " documento detalle:" + error.toString());
                Toast.makeText(getApplicationContext(), R.string.warning_search, Toast.LENGTH_LONG).show();
            }
        }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + TOKENUSER);
                return headers;
            }
        };
        // Adding request
        Volley.newRequestQueue(getApplicationContext()).add(searchDocumentObjectRequest);
    }

}
