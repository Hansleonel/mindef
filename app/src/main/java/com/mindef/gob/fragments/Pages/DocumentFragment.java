package com.mindef.gob.fragments.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.mindef.gob.adapters.DocumentAdapter;
import com.mindef.gob.activities.DocumentCreateActivity;
import com.mindef.gob.activities.DocumentReadActivity;
import com.mindef.gob.models.Document;
import com.mindef.gob.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class DocumentFragment extends Fragment {

    private LinearLayout lVDocumentAnimation;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewDocuments;

    private String TOKENUSER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        lVDocumentAnimation = view.findViewById(R.id.lV_document_animation);
        recyclerViewDocuments = view.findViewById(R.id.rV_documents);
        swipeRefreshLayout = view.findViewById(R.id.swipeV);

        recyclerViewDocuments.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mLinearLayoutManager.setReverseLayout(false);
        // mLinearLayoutManager.setStackFromEnd(true);

        recyclerViewDocuments.setLayoutManager(mLinearLayoutManager);
        recyclerViewDocuments.addItemDecoration(mDividerItemDecoration);

        getUserTokenShared();
        dataDocumentsAPI();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), R.string.inbox_refresh, Toast.LENGTH_LONG).show();
                dataDocumentsAPI();
            }
        });

        view.findViewById(R.id.fab).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DocumentCreateActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void dataDocumentsAPI() {

        JSONObject jsonDocs = new JSONObject();
        try {
            jsonDocs.put("estado", "R");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final ArrayList<Document> documentArrayList = new ArrayList<>();
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST, URL_BASE + "documento/listDocs", jsonDocs,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DocumentsResponse", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject documentJsonObject = response.getJSONObject(i);
                                int idDocument = 0;
                                try {
                                    idDocument = documentJsonObject.getInt("idDoc");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                String codeDocument = documentJsonObject.getString("codigo");
                                String typeDocument = documentJsonObject.getString("serie");
                                String subjectDocument = documentJsonObject.getString("asunto");
                                String dateDocument = documentJsonObject.getString("fecha");
                                dateDocument = dateDocument.substring(0, 10);
                                if (subjectDocument.length() > 25) {

                                    subjectDocument = subjectDocument.substring(0, 25);
                                }
                                documentArrayList.add(new Document(idDocument, codeDocument, typeDocument, subjectDocument, dateDocument));
                            }

                            DocumentAdapter documentAdapter = new DocumentAdapter(getContext(), documentArrayList);
                            documentAdapter.notifyDataSetChanged();

                            recyclerViewDocuments.setAdapter(documentAdapter);

                            if (response.length() > 0) {
                                lVDocumentAnimation.setVisibility(View.GONE);
                            }
                            swipeRefreshLayout.setVisibility(View.VISIBLE);
                            swipeRefreshLayout.setRefreshing(false);

                            documentAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    // String codeDoc = ((TextView) recyclerViewDocuments.findViewHolderForAdapterPosition(recyclerViewDocuments.getChildLayoutPosition(view))
                                    //         .itemView.findViewById(R.id.txtV_item_document_track)).getText().toString();
                                    String idDoc = ((TextView) recyclerViewDocuments.findViewHolderForAdapterPosition(recyclerViewDocuments.getChildLayoutPosition(view))
                                            .itemView.findViewById(R.id.txtV_item_document_id)).getText().toString();
                                    String codeDoc = ((TextView) recyclerViewDocuments.findViewHolderForAdapterPosition(recyclerViewDocuments.getChildLayoutPosition(view))
                                            .itemView.findViewById(R.id.txtV_item_document_track)).getText().toString();
                                    Toast.makeText(getContext(), idDoc, Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getContext(), DocumentReadActivity.class);
                                    i.putExtra("from", "documents");
                                    i.putExtra("idDocument", idDoc);
                                    i.putExtra("codeDocument", codeDoc);
                                    startActivity(i);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(getContext(), R.string.warning_response + e.toString(), Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError", "documents" + error.toString());
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
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);

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
