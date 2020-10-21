package com.mindef.gob.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
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

public class NotificationFragment extends Fragment {

    private LinearLayout lV_notification_mail;
    private SwipeRefreshLayout swipeV_notification;
    private CardView cardViewMail;

    private String TOKENUSER;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_notification, container, false);
        lV_notification_mail = view.findViewById(R.id.lV_notification_mail);
        swipeV_notification = view.findViewById(R.id.swipeV_notification);
        cardViewMail = view.findViewById(R.id.cardV_notification_mail);

        swipeV_notification.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), R.string.notification_refresh, Toast.LENGTH_LONG).show();
                getNotifications();
            }
        });

        cardViewMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resendActivationMail();
            }
        });

        getUserTokenShared();
        getNotifications();
        return view;
    }

    private void resendActivationMail() {
        JsonObjectRequest resendMailObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_BASE + "users/sendActivationEmail", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Toast.makeText(getContext(), R.string.mail_resend, Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError" + "resend mail" + error.toString());
                Toast.makeText(getContext(), R.string.warning_response, Toast.LENGTH_LONG).show();
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
        //Adding request
        Volley.newRequestQueue(getContext()).add(resendMailObjectRequest);
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void getNotifications() {
        getMailValidation();
    }

    private void getMailValidation() {
        JsonObjectRequest mailValidationObjectRequest = new JsonObjectRequest(
                Request.Method.GET, URL_BASE + "users/checkMail", null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            boolean validate = response.getBoolean("validated");
                            if (validate) {
                                lV_notification_mail.setVisibility(View.GONE);
                            } else {
                                lV_notification_mail.setVisibility(View.VISIBLE);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        swipeV_notification.setRefreshing(false);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("onError" + "mail verification" + error.toString());
                Toast.makeText(getContext(), R.string.warning_response, Toast.LENGTH_LONG).show();
                swipeV_notification.setRefreshing(false);
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
        Volley.newRequestQueue(getContext()).add(mailValidationObjectRequest);
    }
}
