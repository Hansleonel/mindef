package com.mindef.gob.Fragments.Pages;

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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.mindef.gob.Adapters.DocumentAdapter;
import com.mindef.gob.Activities.DocumentCreateActivity;
import com.mindef.gob.Activities.DocumentReadActivity;
import com.mindef.gob.Models.Document;
import com.mindef.gob.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.Utilities.Constants.URL_BASE;

public class DocumentFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewDocuments;

    private String TOKENUSER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        recyclerViewDocuments = view.findViewById(R.id.rV_documents);
        swipeRefreshLayout = view.findViewById(R.id.swipeV);

        recyclerViewDocuments.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        recyclerViewDocuments.setLayoutManager(mLinearLayoutManager);
        recyclerViewDocuments.addItemDecoration(mDividerItemDecoration);

        getUserTokenShared();
        dataDocumentsAPI();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "SwipeRefresh", Toast.LENGTH_LONG).show();
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
        final ArrayList<Document> documentArrayList = new ArrayList<>();
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(
                Request.Method.GET, URL_BASE + "sistradoc/documentos", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("DocumentsResponse", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject documentJsonObject = response.getJSONObject(i);
                                int idDocument = documentJsonObject.getInt("id");
                                String typeDocument = documentJsonObject.getString("serie");
                                String titleDocument = documentJsonObject.getString("codigo");
                                String subjectDocument = documentJsonObject.getString("asunto");
                                String dateDocument = documentJsonObject.getString("fecha");
                                documentArrayList.add(new Document(idDocument, titleDocument, typeDocument, subjectDocument, dateDocument));
                            }

                            DocumentAdapter documentAdapter = new DocumentAdapter(getContext(), documentArrayList);
                            documentAdapter.notifyDataSetChanged();

                            recyclerViewDocuments.setAdapter(documentAdapter);

                            swipeRefreshLayout.setRefreshing(false);

                            documentAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String titleDoc = ((TextView) recyclerViewDocuments.findViewHolderForAdapterPosition(recyclerViewDocuments.getChildLayoutPosition(view))
                                            .itemView.findViewById(R.id.txtV_item_document_id)).getText().toString();
                                    Toast.makeText(getContext(), titleDoc, Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getContext(), DocumentReadActivity.class);
                                    i.putExtra("idDocument", titleDoc);
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
}
