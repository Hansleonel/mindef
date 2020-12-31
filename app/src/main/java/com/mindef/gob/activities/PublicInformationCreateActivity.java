package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.mindef.gob.R;
import com.mindef.gob.adapters.TrackingAdapter;
import com.mindef.gob.models.Tracking;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class PublicInformationCreateActivity extends AppCompatActivity {

    private ScrollView sVPublicInformation;
    private Spinner spVTypePersonPublicInformation;
    private Spinner spVOfficePublicInformation;
    private Spinner spVMethodPublicInformation;
    private EditText edtSubjectPublicInformation;
    private TextInputEditText edtPublicInformationDescription;
    private Button btnVPublicInformation;

    private LottieAnimationView lottieVUpload, lottieVError, lottieVSuccess;
    private TextView txtVPublicInformationWait, txtVPublicInformationError, txtVPublicInformationSuccess;

    private final List<String> typePersonList = new ArrayList<>();
    private final List<String> typeOfficeList = new ArrayList<>();
    private final List<String> typeOfficeIdList = new ArrayList<>();
    private final List<String> typeMethodList = new ArrayList<>();
    private final List<Integer> typeMethodIdList = new ArrayList<>();

    private String itemSelected;

    private String typePerson;
    private String typeOffice;
    private int typeMethod;
    private String titleInformation;
    private String descriptionInformation;

    private String TOKENUSER;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_information_create);

        sVPublicInformation = findViewById(R.id.sVPublicInformation);
        spVTypePersonPublicInformation = findViewById(R.id.spVTypePersonPublicInformation);
        spVOfficePublicInformation = findViewById(R.id.spVOfficePublicInformation);
        spVMethodPublicInformation = findViewById(R.id.spVMethodPublicInformation);
        edtSubjectPublicInformation = findViewById(R.id.edtSubjectPublicInformation);
        edtPublicInformationDescription = findViewById(R.id.edtPublicInformationDescription);
        btnVPublicInformation = findViewById(R.id.btnVPublicInformation);

        lottieVUpload = findViewById(R.id.lottieVUploadPublicInformation);
        lottieVError = findViewById(R.id.lottieVErrorPublicInformation);
        lottieVSuccess = findViewById(R.id.lottieVSuccessPublicInformation);

        txtVPublicInformationWait = findViewById(R.id.txtVPublicInformationWait);
        txtVPublicInformationError = findViewById(R.id.txtVPublicInformationError);
        txtVPublicInformationSuccess = findViewById(R.id.txtVPublicInformationSuccess);

        getUserTokenShared();

        spinnerDataTypePerson();
        spinnerDataTypeInformation();
        spinnerDataTypeMethod();

        spinnerAction(spVTypePersonPublicInformation, typePersonList, 1);
        spinnerAction(spVOfficePublicInformation, typeOfficeList, 2);
        spinnerAction(spVMethodPublicInformation, typeMethodList, 3);

        btnVPublicInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (edtSubjectPublicInformation.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PublicInformationCreateActivity.this, "Ingrese Asunto", Toast.LENGTH_SHORT).show();
                } else if (edtPublicInformationDescription.getText().toString().trim().isEmpty()) {
                    Toast.makeText(PublicInformationCreateActivity.this, "Ingrese Descripción", Toast.LENGTH_SHORT).show();
                } else {
                    titleInformation = edtSubjectPublicInformation.getText().toString();
                    descriptionInformation = edtPublicInformationDescription.getText().toString();
                    sendInformation(typePerson, typeOffice, typeMethod, titleInformation, descriptionInformation);
                }
            }
        });

    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void spinnerDataTypePerson() {
        typePersonList.add("Seleccione un tipo de Persona");
        typePersonList.add("Natural");
        typePersonList.add("Jurídica");

        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typePersonList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVTypePersonPublicInformation.setAdapter(spinnerAdapter);
    }

    private void spinnerDataTypeInformation() {
        /*typeOfficeList.add("Seleccione Oficina");
        typeOfficeList.add("Despacho Ministerial");
        typeOfficeList.add("Centro de Operaciones de Emergencia Nacional");
        typeOfficeList.add("Despacho Viceministerial de políticas para la defensa");
        typeOfficeList.add("Despacho Viceministerial de recursos para la defensa");
        typeOfficeList.add("Secretaría General");
        typeOfficeList.add("órgano de Control Institucional");
        typeOfficeList.add("Inspectoría General");
        typeOfficeList.add("Procuraduría Pública");
        typeOfficeList.add("Otro");*/

        JsonArrayRequest jsonArrayRequestOffice = new JsonArrayRequest(
                Request.Method.GET, URL_BASE + "dependencia/list", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("Dependencia", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject dependenciaJsonObject = response.getJSONObject(i);

                                String office = dependenciaJsonObject.getString("abreviado");
                                String officeId = dependenciaJsonObject.getString("idDependencia");

                                typeOfficeList.add(office);
                                typeOfficeIdList.add(officeId);
                            }

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typeOfficeList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spVOfficePublicInformation.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("onError", "Dependencia" + error.toString());
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
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequestOffice);

        /*ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typeOfficeList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVOfficePublicInformation.setAdapter(spinnerAdapter);*/
    }

    private void spinnerDataTypeMethod() {
        /* typeMethodList.add("Seleccione Metodo Entrega");
        typeMethodList.add("Copia Simple");
        typeMethodList.add("Copia Certificada");
        typeMethodList.add("CD");
        typeMethodList.add("Email");*/

        JsonArrayRequest jsonArrayRequestOffice = new JsonArrayRequest(
                Request.Method.GET, URL_BASE + "tipoEntrega", null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d("TypeMethodSend", "onResponse: " + response);
                        try {
                            for (int i = 0; i < response.length(); i++) {
                                JSONObject typeMethodJsonObject = response.getJSONObject(i);

                                String methodSend = typeMethodJsonObject.getString("abreviado");
                                int methodSendId = typeMethodJsonObject.getInt("idTipoent");

                                typeMethodList.add(methodSend);
                                typeMethodIdList.add(methodSendId);
                            }

                            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typeMethodList);
                            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            spVMethodPublicInformation.setAdapter(spinnerAdapter);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                VolleyLog.d("onError", "TypeMethodSend" + error.toString());
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
        Volley.newRequestQueue(getApplicationContext()).add(jsonArrayRequestOffice);

        /*ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(PublicInformationCreateActivity.this, android.R.layout.simple_list_item_1, typeMethodList);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spVMethodPublicInformation.setAdapter(spinnerAdapter);*/
    }

    private void spinnerAction(Spinner spinner, final List<String> listSpinner, final int parameter) {
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                itemSelected = listSpinner.get(position);

                if (parameter == 1) {
                    typePerson = itemSelected;
                    Log.d("PARAMETER", "onItemSelected: " + typePerson);
                } else if (parameter == 2) {
                    // typeOffice = itemSelected;
                    typeOffice = typeOfficeIdList.get(position);
                    Log.d("PARAMETER", "onItemSelected: " + typeOffice + " List " + typeOfficeIdList.get(position));
                } else if (parameter == 3) {
                    // typeMethod = itemSelected;
                    typeMethod = typeMethodIdList.get(position);
                    Log.d("PARAMETER", "onItemSelected: " + typeMethod + " List " + typeMethodIdList.get(position));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public void sendInformation(String typePerson, String typeOffice, int typeMethod, String titleInformation, String descriptionInformation) {
        sVPublicInformation.setVisibility(View.GONE);
        lottieVUpload.setVisibility(View.VISIBLE);
        txtVPublicInformationWait.setVisibility(View.VISIBLE);

        Log.d("PublicInformationCreate", "sendInformation: " + typePerson + " " + typeOffice + " " + typeMethod + " " + titleInformation + " " + descriptionInformation);
        JSONObject jsonPublicInformation = new JSONObject();
        try {
            jsonPublicInformation.put("asunto", titleInformation);
            jsonPublicInformation.put("observaciones", descriptionInformation);
            jsonPublicInformation.put("tipoEntrega", typeMethod);
            jsonPublicInformation.put("dependencia", typeOffice);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(
                Request.Method.POST, URL_BASE + "documento/register/aip", jsonPublicInformation, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Log.d("RegisterAIP", "onResponse: " + response.getString("idDocumento"));
                    successPublicInformationAction();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d("RegisterAIP error", "onLoginError");
                Toast.makeText(PublicInformationCreateActivity.this, R.string.warning_response, Toast.LENGTH_LONG).show();
                errorPublicInformationAction();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("Authorization", "Bearer " + TOKENUSER);
                return headers;
            }
        };
        //Adding request
        Volley.newRequestQueue(this).add(jsonObjectRequest);

    }

    private void successPublicInformationAction() {
        lottieVUpload.setVisibility(View.GONE);
        txtVPublicInformationWait.setVisibility(View.GONE);
        lottieVSuccess.setVisibility(View.VISIBLE);
        txtVPublicInformationSuccess.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                finish();
            }
        }, 3000);

    }

    private void errorPublicInformationAction() {
        lottieVUpload.setVisibility(View.GONE);
        txtVPublicInformationWait.setVisibility(View.GONE);
        lottieVError.setVisibility(View.VISIBLE);
        txtVPublicInformationError.setVisibility(View.VISIBLE);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                sVPublicInformation.setVisibility(View.VISIBLE);
                lottieVError.setVisibility(View.GONE);
                txtVPublicInformationError.setVisibility(View.GONE);
            }
        }, 2000);
    }
}