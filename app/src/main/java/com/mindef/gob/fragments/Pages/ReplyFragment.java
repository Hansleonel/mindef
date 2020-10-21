package com.mindef.gob.fragments.Pages;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.mindef.gob.R;
import com.mindef.gob.activities.DocumentReadActivity;
import com.mindef.gob.adapters.ReplyAdapter;
import com.mindef.gob.models.Reply;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class ReplyFragment extends Fragment {

    private LinearLayout lV_reply_animation;
    private SwipeRefreshLayout swipeRefreshLayoutReplies;
    private RecyclerView recyclerViewReplies;

    private String TOKENUSER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_reply, container, false);
        recyclerViewReplies = view.findViewById(R.id.rV_replies);
        swipeRefreshLayoutReplies = view.findViewById(R.id.swipeV_replies);
        lV_reply_animation = view.findViewById(R.id.lV_reply_animation);

        recyclerViewReplies.setHasFixedSize(true);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        linearLayoutManager.setReverseLayout(false);
        // linearLayoutManager.setStackFromEnd(true);

        recyclerViewReplies.setLayoutManager(linearLayoutManager);
        recyclerViewReplies.addItemDecoration(dividerItemDecoration);

        getUserTokenShared();
        dataRepliesAPI();

        swipeRefreshLayoutReplies.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                // Toast.makeText(getContext(), R.string.warning_response, Toast.LENGTH_LONG).show();
                dataRepliesAPI();
            }
        });

        return view;
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void dataRepliesAPI() {
        JSONArray jsonReply = new JSONArray();
        JSONObject jsonObjectReply = new JSONObject();
        try {
            jsonObjectReply.put("value", "value");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        jsonReply.put(jsonObjectReply);

        final ArrayList<Reply> replyArrayList = new ArrayList<>();
        MyJsonArrayRequest jsonArrayRequest = new MyJsonArrayRequest(
                Request.Method.POST, URL_BASE + "documento/respuestas", jsonObjectReply,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("ReplyFragment Data", "onResponse: " + response.length());
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                int idFileReply = 0;
                                JSONObject replyJsonObject = response.getJSONObject(i);
                                String codeReply = replyJsonObject.getString("codigo");
                                String dateReply = replyJsonObject.getString("fecha");
                                String serieReply = replyJsonObject.getString("serie");
                                String subjectReply = replyJsonObject.getString("asunto");
                                String sourceReply = replyJsonObject.getString("principal");
                                dateReply = dateReply.substring(0, 10);
                                try {
                                    idFileReply = replyJsonObject.getInt("idDoc");
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                if (subjectReply.length() > 25) {
                                    subjectReply = subjectReply.substring(0, 25);
                                }

                                replyArrayList.add(new Reply(codeReply, dateReply, serieReply, subjectReply, sourceReply, idFileReply));
                            }

                            ReplyAdapter replyAdapter = new ReplyAdapter(getContext(), replyArrayList);
                            replyAdapter.notifyDataSetChanged();

                            recyclerViewReplies.setAdapter(replyAdapter);

                            if (response.length() > 0) {
                                lV_reply_animation.setVisibility(View.GONE);
                            }
                            swipeRefreshLayoutReplies.setVisibility(View.VISIBLE);
                            swipeRefreshLayoutReplies.setRefreshing(false);

                            replyAdapter.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String idDoc = ((TextView) recyclerViewReplies.findViewHolderForAdapterPosition(recyclerViewReplies.getChildLayoutPosition(view))
                                            .itemView.findViewById(R.id.txtV_item_document_id)).getText().toString();
                                    String codeDoc = ((TextView) recyclerViewReplies.findViewHolderForAdapterPosition(recyclerViewReplies.getChildLayoutPosition(view))
                                            .itemView.findViewById(R.id.txtV_item_document_track)).getText().toString();
                                    String principal = ((TextView) recyclerViewReplies.findViewHolderForAdapterPosition(recyclerViewReplies.getChildLayoutPosition(view))
                                            .itemView.findViewById(R.id.txtV_item_document_principal)).getText().toString();
                                    Toast.makeText(getContext(), idDoc, Toast.LENGTH_LONG).show();
                                    Intent i = new Intent(getContext(), DocumentReadActivity.class);
                                    i.putExtra("from", "replies");
                                    i.putExtra("idDocument", idDoc);
                                    i.putExtra("codeDocument", codeDoc);
                                    i.putExtra("sourceDocument", principal);
                                    startActivity(i);
                                }
                            });

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("ReplyFragment", "replies" + error.toString());
                Log.d("ReplyFragment", "onErrorResponse: " + error.toString());
                Toast.makeText(getContext(), error.toString(), Toast.LENGTH_LONG).show();
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
