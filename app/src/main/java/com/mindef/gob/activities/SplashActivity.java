package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;
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

public class SplashActivity extends AppCompatActivity {

    private String userLogin;
    private String passwordLogin;
    private String TokenUser;
    private String userViewSlide;

    @Override
    protected void onStart() {
        super.onStart();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkLogin();
            }
        }, 1000);
    }

    private void checkLogin() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("UserSharedFile", MODE_PRIVATE);
        userLogin = SP.getString("UserNameString", "UserDefaultValue");
        passwordLogin = SP.getString("UserPasswordString", "PasswordDefaultValue");
        userViewSlide = SP.getString("UserViewSlideString", "UserViewDefaultValue");


        if (!userViewSlide.equals("UserViewDefaultValue")) {
            if (!userLogin.equals("UserDefaultValue")) {
                login();
            } else {
                noLogin();
            }
        } else {
            viewSlides();
        }

    }

    private void viewSlides() {
        Intent intent = new Intent(SplashActivity.this, StartActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void login() {
        JSONObject jsonLogin = new JSONObject();
        try {
            jsonLogin.put("username", userLogin);
            jsonLogin.put("password", passwordLogin);
            jsonLogin.put("rememberMe", false);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        Log.d("LOGIN", "login: " + jsonLogin);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_BASE + "authenticate", jsonLogin, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("Token", "onResponse: " + response.getString("id_token"));
                    TokenUser = response.getString("id_token");

                    SharedPreferences sharedPreferences = getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("TokenUserString", TokenUser);
                    editor.commit();

                    toNavigation();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Request error", "onLoginError");
                Toast.makeText(SplashActivity.this, R.string.warning_splash, Toast.LENGTH_LONG).show();
                noLogin();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                return headers;
            }
        };
        //Adding request
        Volley.newRequestQueue(this).add(jsonObjectRequest);
    }

    private void toNavigation() {
        Intent intent = new Intent(SplashActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    private void noLogin() {
        Intent intent = new Intent(SplashActivity.this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_splash);
    }
}
