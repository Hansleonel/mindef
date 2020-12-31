package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.mindef.gob.R;
import com.mindef.gob.adapters.PresentmentAdapter;
import com.mindef.gob.models.Document;
import com.mindef.gob.models.Presentment;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class PresentmentActivity extends AppCompatActivity {

    private LinearLayout lVPresentmentAnimation;
    private SwipeRefreshLayout swipeVPresentmentNotification;
    private RecyclerView recyclerViewPresentmentComplaint;

    ArrayList<Presentment> presentmentArrayList = new ArrayList<>();
    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentment);

        lVPresentmentAnimation = findViewById(R.id.lVPresentmentAnimation);
        recyclerViewPresentmentComplaint = findViewById(R.id.rVPresentmentComplaint);
        swipeVPresentmentNotification = findViewById(R.id.swipeVPresentment);

        getUserTokenShared();

        recyclerViewPresentmentComplaint.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManagerComplaint = new LinearLayoutManager(getApplicationContext());
        recyclerViewPresentmentComplaint.setLayoutManager(mLinearLayoutManagerComplaint);


        findViewById(R.id.lVComplaint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresentmentActivity.this, ComplaintActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.lVClaim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresentmentActivity.this, ClaimActivity.class);
                startActivity(intent);
            }
        });

        swipeVPresentmentNotification.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(PresentmentActivity.this, R.string.inbox_refresh, Toast.LENGTH_SHORT).show();
                dataDocumentsComplaint();
                dataDocumentsClaim();
            }
        });

        dataDocumentsComplaint();
        dataDocumentsClaim();
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void dataDocumentsComplaint() {
        JSONObject jsonDocs = new JSONObject();
        try {
            jsonDocs.put("servicio", 31);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // final ArrayList<Presentment> presentmentArrayList = new ArrayList<>();
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST, URL_BASE + "documento/listDocs", jsonDocs,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Complaint", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject presentmentObject = response.getJSONObject(i);
                                int idPresentment = presentmentObject.getInt("idDoc");
                                String codePresentment = presentmentObject.getString("codigo");
                                String subjectPresentment = presentmentObject.getString("asunto");
                                String datePresentment = presentmentObject.getString("fecha");
                                datePresentment = datePresentment.substring(0, 10);

                                if (subjectPresentment.length() > 25) {
                                    subjectPresentment = subjectPresentment.substring(0, 25);
                                }

                                presentmentArrayList.add(new Presentment(idPresentment, "Queja", codePresentment, subjectPresentment, datePresentment));
                            }

                            PresentmentAdapter presentmentAdapter = new PresentmentAdapter(getApplicationContext(), presentmentArrayList);
                            presentmentAdapter.notifyDataSetChanged();

                            recyclerViewPresentmentComplaint.setAdapter(presentmentAdapter);

                            if (response.length() > 0) {
                                lVPresentmentAnimation.setVisibility(View.GONE);
                            } else {
                                lVPresentmentAnimation.setVisibility(View.VISIBLE);
                            }

                            swipeVPresentmentNotification.setVisibility(View.VISIBLE);
                            swipeVPresentmentNotification.setRefreshing(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError", "Complaint" + error.toString());
                Toast.makeText(PresentmentActivity.this, R.string.warning_response + error.networkResponse.toString(), Toast.LENGTH_LONG).show();
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

        // Adding request to request queue
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequest);
        presentmentArrayList.clear();
    }

    private void dataDocumentsClaim() {
        JSONObject jsonDocs = new JSONObject();
        try {
            jsonDocs.put("servicio", 3);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        // final ArrayList<Presentment> presentmentArrayList = new ArrayList<>();
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST, URL_BASE + "documento/listDocs", jsonDocs,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Claim", "onResponse: " + response.length());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject presentmentObject = response.getJSONObject(i);
                                int idPresentment = presentmentObject.getInt("idDoc");
                                String codePresentment = presentmentObject.getString("codigo");
                                String subjectPresentment = presentmentObject.getString("asunto");
                                String datePresentment = presentmentObject.getString("fecha");
                                datePresentment = datePresentment.substring(0, 10);

                                if (subjectPresentment.length() > 25) {
                                    subjectPresentment = subjectPresentment.substring(0, 25);
                                }

                                presentmentArrayList.add(new Presentment(idPresentment, "Reclamo", codePresentment, subjectPresentment, datePresentment));
                            }

                            PresentmentAdapter presentmentAdapterClaim = new PresentmentAdapter(getApplicationContext(), presentmentArrayList);
                            presentmentAdapterClaim.notifyDataSetChanged();

                            recyclerViewPresentmentComplaint.setAdapter(presentmentAdapterClaim);

                            if (response.length() > 0) {
                                lVPresentmentAnimation.setVisibility(View.GONE);
                            } else {
                                lVPresentmentAnimation.setVisibility(View.VISIBLE);
                            }

                            swipeVPresentmentNotification.setVisibility(View.VISIBLE);
                            swipeVPresentmentNotification.setRefreshing(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError", "Claim" + error.toString());
                Toast.makeText(PresentmentActivity.this, R.string.warning_response, Toast.LENGTH_LONG).show();
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
        // Adding request to request queue
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequest);
        presentmentArrayList.clear();
    }

    public class MyJsonArrayRequest extends JsonRequest<JSONArray> {

        public MyJsonArrayRequest(int method, String url, JSONObject jsonRequest, Response.Listener<JSONArray> listener, Response.ErrorListener errorListener) {
            super(method, url, (jsonRequest == null) ? null : jsonRequest.toString(), listener, errorListener);
        }

        @Override
        protected Response<JSONArray> parseNetworkResponse(NetworkResponse response) {
            try {
                String jsonString = new String(response.data, HttpHeaderParser.parseCharset(response.headers, PROTOCOL_CHARSET));
                return Response.success(new JSONArray(jsonString), HttpHeaderParser.parseCacheHeaders(response));
            } catch (UnsupportedEncodingException e) {
                return Response.error(new ParseError(e));
            } catch (JSONException exception) {
                return Response.error(new ParseError(exception));
            }

        }
    }
}