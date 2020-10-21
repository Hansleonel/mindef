package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.mindef.gob.R;
import com.mindef.gob.adapters.AnswerAdapter;
import com.mindef.gob.adapters.TrackingAdapter;
import com.mindef.gob.models.Answer;
import com.mindef.gob.models.Tracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class DocumentReadActivity extends AppCompatActivity {

    private TextView txtV_document_read_title, txtV_document_read_subject, txtV_document_read_date, txtV_document_read_answer, txtV_document_read_type;
    private RecyclerView recyclerViewTracking, recyclerViewAnswers;
    private ImageView imgV_type_read_document;
    private Button btn_read_document_answer, btn_read_document_answer_source;

    private String idDocument;
    private String fromDocument;
    private String codeDocument;
    private String linkDocument = "445102";
    private String sourceDocument;
    private JSONArray answersDocument;

    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_read);

        txtV_document_read_title = findViewById(R.id.txtV_document_read_title);
        txtV_document_read_subject = findViewById(R.id.txtV_document_read_subject);
        txtV_document_read_date = findViewById(R.id.txtV_document_read_date);
        txtV_document_read_type = findViewById(R.id.txtV_document_read_type);
        txtV_document_read_answer = findViewById(R.id.txtV_document_read_answer);
        recyclerViewTracking = findViewById(R.id.rV_tracking);
        imgV_type_read_document = findViewById(R.id.imgV_type_read_document);
        btn_read_document_answer = findViewById(R.id.btn_read_document_answer);
        btn_read_document_answer_source = findViewById(R.id.btn_read_document_answer_source);
        recyclerViewAnswers = findViewById(R.id.rV_answers);

        recyclerViewTracking.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewTracking.setLayoutManager(mLinearLayoutManager);

        idDocument = getIntent().getStringExtra("idDocument");
        fromDocument = getIntent().getStringExtra("from");
        codeDocument = getIntent().getStringExtra("codeDocument");
        sourceDocument = getIntent().getStringExtra("sourceDocument");

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

        findViewById(R.id.btn_read_document_answer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                searchAnswer();
            }
        });

        findViewById(R.id.btn_read_document_answer_source).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goSourceDocument();
            }
        });

        getUserTokenShared();
        searchDocument();

        if (!codeDocument.equals("null")) {
            trackingDocument();
        } else {
            pendingTrackingDocument();
        }

    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void searchDocument() {
        JsonObjectRequest searchDocumentObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_BASE + "documento/detalle/" + idDocument, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Document Detail", "onResponse: " + response);
                        try {
                            // int id = response.getInt("id");
                            String serie = response.getString("serie");
                            String asunto = response.getString("asunto");
                            String fecha = response.getString("fecha");
                            JSONArray respuestas = response.getJSONArray("respuestas");
                            // TODO recordar corregir el link devuelto por el servidor
                            linkDocument = response.getString("idArchivo");
                            // linkDocument = linkDocument.substring(51);

                            txtV_document_read_title.setText(serie);
                            txtV_document_read_subject.setText(asunto);
                            txtV_document_read_date.setText(fecha);

                            if (respuestas.length() > 0) {
                                answersDocument = respuestas;
                                btn_read_document_answer.setVisibility(View.VISIBLE);
                                Log.d("Answers", "onResponse: " + answersDocument);
                            }

                            if (serie.startsWith("CA")) {
                                imgV_type_read_document.setImageResource(R.drawable.ic_document_type_c);
                            }

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

    private void searchDocumentReply(final String sourceDocument) {

        JsonObjectRequest searchDocumentObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_BASE + "documento/getCodigo/" + sourceDocument, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("Document Detail Reply", "onResponse: " + response);
                        try {

                            String code = response.getString("codigo");

                            Intent i = new Intent(getApplicationContext(), DocumentReadActivity.class);
                            i.putExtra("idDocument", sourceDocument);
                            i.putExtra("from", "documents");
                            i.putExtra("codeDocument", code);
                            startActivity(i);

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

    private void trackingDocument() {

        final ArrayList<Tracking> trackingArrayList = new ArrayList<>();

        JsonArrayRequest jsonArrayRequestTracking = new JsonArrayRequest(
                Request.Method.GET, URL_BASE + "sistradoc/secuencia/" + codeDocument, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DocumentTracking", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {

                                JSONObject trackingJsonObject = response.getJSONObject(i);

                                String officeTracking = trackingJsonObject.getString("oficina");
                                String dateTracking = trackingJsonObject.getString("fecha");
                                String notesTracking = trackingJsonObject.getString("notas");
                                String stateTracking = trackingJsonObject.getString("estado");

                                trackingArrayList.add(new Tracking(officeTracking, dateTracking, notesTracking, stateTracking, i));

                            }

                            TrackingAdapter trackingAdapter = new TrackingAdapter(getApplicationContext(), trackingArrayList);
                            trackingAdapter.notifyDataSetChanged();

                            recyclerViewTracking.setAdapter(trackingAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("DocumentTracking", "documents" + error.toString());
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

        //Add request
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequestTracking);
    }

    private void pendingTrackingDocument() {
        switch (fromDocument) {
            case "detailDocument":
            case "replies": {
                txtV_document_read_type.setText(R.string.message_document_answer);
                txtV_document_read_type.setTextColor(getResources().getColor(R.color.colorSemiGreen));

                btn_read_document_answer_source.setVisibility(View.VISIBLE);
                break;
            }
            case "documents": {
                ArrayList<Tracking> trackingArrayList = new ArrayList<>();
                trackingArrayList.add(new Tracking("Pendiente de Recepcion", "Pendiente", "Pendiente", "Pendiente", 0));
                TrackingAdapter trackingAdapter = new TrackingAdapter(getApplicationContext(), trackingArrayList);
                trackingAdapter.notifyDataSetChanged();
                recyclerViewTracking.setAdapter(trackingAdapter);
                break;
            }
        }
    }

    private void searchAnswer() {

        txtV_document_read_answer.setVisibility(View.VISIBLE);
        ArrayList<Answer> answerArrayList = new ArrayList<>();

        recyclerViewAnswers.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerViewAnswers.setLayoutManager(mLinearLayoutManager);

        try {
            for (int i = 0; i < answersDocument.length(); i++) {
                JSONObject answerJsonObject = answersDocument.getJSONObject(i);

                String answerTitle = answerJsonObject.getString("documento");
                String answerSubject = answerJsonObject.getString("asunto");
                int answerId = answerJsonObject.getInt("idDoc");

                answerArrayList.add(new Answer(answerId, answerSubject, answerTitle));
            }

            AnswerAdapter answerAdapter = new AnswerAdapter(getApplicationContext(), answerArrayList);
            answerAdapter.notifyDataSetChanged();

            recyclerViewAnswers.setAdapter(answerAdapter);

            answerAdapter.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    String idDoc = ((TextView) recyclerViewAnswers.findViewHolderForAdapterPosition(recyclerViewAnswers.getChildLayoutPosition(view))
                            .itemView.findViewById(R.id.txtV_item_answer_id)).getText().toString();
                    Toast.makeText(getApplicationContext(), idDoc, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), DocumentReadActivity.class);
                    i.putExtra("from", "detailDocument");
                    i.putExtra("idDocument", idDoc);
                    i.putExtra("codeDocument", "null");
                    startActivity(i);
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }

    private void goSourceDocument() {
        if (fromDocument.equals("detailDocument")) {
            finish();
        }

        if (fromDocument.equals("replies")) {
            Log.d("source button", "goSourceDocument: " + idDocument + fromDocument + codeDocument + sourceDocument);
            searchDocumentReply(sourceDocument);
        }
    }
}
