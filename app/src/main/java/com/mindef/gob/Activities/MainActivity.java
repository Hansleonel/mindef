package com.mindef.gob.Activities;

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

import static com.mindef.gob.Utilities.Constants.URL_BASE;

public class MainActivity extends AppCompatActivity {

    private EditText editTextEmail, editTextPassword;
    private String TokenUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editTextEmail = findViewById(R.id.edt_email_login);
        editTextPassword = findViewById(R.id.edt_password_login);

        findViewById(R.id.btn_login).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (editTextEmail.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.register_dni, Toast.LENGTH_LONG).show();
                } else if (editTextPassword.getText().toString().trim().isEmpty()) {
                    Toast.makeText(MainActivity.this, R.string.register_password, Toast.LENGTH_LONG).show();
                } else {
                    login();
                }
            }
        });

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void login() {

        JSONObject jsonLogin = new JSONObject();
        try {
            jsonLogin.put("username", editTextEmail.getText().toString());
            jsonLogin.put("password", editTextPassword.getText().toString());
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

                    SharedPreferences sharedPreferencesUser = getSharedPreferences("UserSharedFile", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editorUser = sharedPreferencesUser.edit();
                    editorUser.putString("UserNameString", editTextEmail.getText().toString().trim());
                    editorUser.putString("UserPasswordString", editTextPassword.getText().toString().trim());
                    editorUser.commit();

                    toNavigation();

                } catch (JSONException e) {
                    e.printStackTrace();
                    Toast.makeText(MainActivity.this, R.string.warning_response, Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("Request error", "onLoginError" + error.getMessage());
                Toast.makeText(MainActivity.this, R.string.warning_login, Toast.LENGTH_LONG).show();
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
        Intent intent = new Intent(MainActivity.this, NavigationActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
