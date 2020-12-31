package com.mindef.gob.activities;

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
import com.mindef.gob.adapters.PublicInformationAdapter;
import com.mindef.gob.models.Document;
import com.mindef.gob.models.PublicInformation;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class PublicInformationActivity extends AppCompatActivity {

    private LinearLayout lVPublicInformationAnimation;
    private SwipeRefreshLayout swipeVPublicInformationNotifications;
    private RecyclerView recyclerViewPublicInformation;

    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_information);

        lVPublicInformationAnimation = findViewById(R.id.lVPublicInformationAnimation);
        recyclerViewPublicInformation = findViewById(R.id.rVPublicInformation);
        swipeVPublicInformationNotifications = findViewById(R.id.swipeVPublicInformationNotifications);

        getUserTokenShared();

        recyclerViewPublicInformation.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());

        recyclerViewPublicInformation.setLayoutManager(mLinearLayoutManager);

        LinearLayout lVPublicInformationAccess = findViewById(R.id.lVPublicInformationAccess);
        lVPublicInformationAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicInformationActivity.this, ConditionActivity.class);
                startActivity(intent);
            }
        });

        swipeVPublicInformationNotifications.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getApplicationContext(), R.string.inbox_refresh, Toast.LENGTH_LONG).show();
                dataDocumentPublicInformation();
            }
        });

        dataDocumentPublicInformation();
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void dataDocumentPublicInformation() {
        JSONObject jsonDocs = new JSONObject();
        try {
            jsonDocs.put("servicio", 2);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<PublicInformation> publicInformationArrayList = new ArrayList<>();
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST, URL_BASE + "documento/listDocs", jsonDocs,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("PublicInformation", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject publicInformationObject = response.getJSONObject(i);
                                int idPublicInformation = 0;
                                try {
                                    idPublicInformation = publicInformationObject.getInt("idDoc");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String codePublicInformation = publicInformationObject.getString("codigo");
                                String subjectPublicInformation = publicInformationObject.getString("asunto");
                                String datePublicInformation = publicInformationObject.getString("fecha");
                                datePublicInformation = datePublicInformation.substring(0, 10);

                                if (subjectPublicInformation.length() > 25) {
                                    subjectPublicInformation = subjectPublicInformation.substring(0, 25);
                                }

                                publicInformationArrayList.add(new PublicInformation(idPublicInformation, codePublicInformation, subjectPublicInformation, datePublicInformation));
                            }

                            PublicInformationAdapter publicInformationAdapter = new PublicInformationAdapter(getApplicationContext(), publicInformationArrayList);
                            publicInformationAdapter.notifyDataSetChanged();

                            recyclerViewPublicInformation.setAdapter(publicInformationAdapter);

                            if (response.length() > 0) {
                                lVPublicInformationAnimation.setVisibility(View.GONE);
                            } else {
                                lVPublicInformationAnimation.setVisibility(View.VISIBLE);
                            }

                            swipeVPublicInformationNotifications.setVisibility(View.VISIBLE);
                            swipeVPublicInformationNotifications.setRefreshing(false);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError", "PublicInformation" + error.toString());
                Toast.makeText(PublicInformationActivity.this, R.string.warning_response, Toast.LENGTH_LONG).show();
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