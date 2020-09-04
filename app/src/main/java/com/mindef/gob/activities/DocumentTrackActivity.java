package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
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

public class DocumentTrackActivity extends AppCompatActivity {

    private EditText edtV_track_id;

    private String TOKENUSER;

    private String codeDoc;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_track);

        edtV_track_id = findViewById(R.id.edt_track_document);

        getUserTokenShared();

        findViewById(R.id.btn_search_document).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtV_track_id.getText().toString().trim().isEmpty()) {
                    Toast.makeText(DocumentTrackActivity.this, R.string.document_track_description, Toast.LENGTH_LONG).show();
                } else {
                    codeDoc = edtV_track_id.getText().toString().trim();
                    searchDocument();
                }
            }
        });
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void searchDocument() {
        JsonObjectRequest searchDocumentObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_BASE + "sistradoc/documento/detalle/" + codeDoc, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Response Detalle", "onResponse: " + response);
                        try {
                            if (response.getString("id").length() > 0) {
                                Intent i = new Intent(getApplicationContext(), DocumentReadActivity.class);
                                i.putExtra("codeDocument", codeDoc);
                                startActivity(i);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getApplicationContext(), R.string.warning_search, Toast.LENGTH_LONG).show();
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
