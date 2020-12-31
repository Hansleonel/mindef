package com.mindef.gob.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.textfield.TextInputEditText;
import com.mindef.gob.R;
import com.mindef.gob.utilities.Files;

import java.io.File;
import java.util.Arrays;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class ClaimActivity extends AppCompatActivity {

    private static final int RESULT_CODE_FILE_CHOOSER = 1000;

    private ScrollView sVClaim;
    private TextInputEditText edtSubjectClaim;
    private TextInputEditText edtDescriptionClaim;
    private CardView cardVImageClaim;
    private Button btnClaim;
    private TextView txtVImgAttachedClaim;
    private TextView txtVImgAttachedClaimSize;

    private LottieAnimationView lottieUpload, lottieError, lottieSuccess;
    private TextView txtVUpload, txtVError, txtVSuccess;

    private String subjectClaim;
    private String descriptionClaim;
    private String pdfFileClaimPath;
    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_claim);

        sVClaim = findViewById(R.id.sVClaim);
        edtSubjectClaim = findViewById(R.id.edtSubjectClaim);
        edtDescriptionClaim = findViewById(R.id.edtClaimDescription);
        cardVImageClaim = findViewById(R.id.cardVImageClaim);
        txtVImgAttachedClaim = findViewById(R.id.txtVImgAttachedClaim);
        txtVImgAttachedClaimSize = findViewById(R.id.txtVImgAttachedClaimSize);
        btnClaim = findViewById(R.id.btnClaim);

        lottieUpload = findViewById(R.id.lottieVUploadClaim);
        lottieError = findViewById(R.id.lottieVErrorClaim);
        lottieSuccess = findViewById(R.id.lottieVSuccessClaim);

        txtVUpload = findViewById(R.id.txtVDocumentUploadWaitClaim);
        txtVError = findViewById(R.id.txtVDocumentUploadErrorClaim);
        txtVSuccess = findViewById(R.id.txtVDocumentUploadSuccessClaim);

        getUserTokenShared();

        cardVImageClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFile();
            }
        });

        btnClaim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendClaim();
            }
        });
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void selectFile() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Seleccione Archivo");
        startActivityForResult(chooseFileIntent, RESULT_CODE_FILE_CHOOSER);
    }

    private void sendClaim() {
        subjectClaim = edtSubjectClaim.getText().toString();
        descriptionClaim = edtDescriptionClaim.getText().toString();
        if (pdfFileClaimPath == null) {
            Toast.makeText(this, R.string.sending_empty, Toast.LENGTH_LONG).show();
            return;
        }
        UploadTask uploadTask = new UploadTask();
        uploadTask.execute(new String[]{pdfFileClaimPath});
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        switch (requestCode) {
            case RESULT_CODE_FILE_CHOOSER:
                if (resultCode == Activity.RESULT_OK) {
                    if (data != null) {
                        Uri fileUri = data.getData();
                        Log.i("FILE CHOOSER", "onActivityResult: " + fileUri);
                        String filePath = null;
                        try {
                            filePath = Files.getPath(getApplicationContext(), fileUri);
                            pdfFileClaimPath = filePath;
                            txtVImgAttachedClaim.setText("" + new File(filePath).getName());
                            txtVImgAttachedClaimSize.setText("" + new File(filePath).length() / 1000000.0 + "Mb");
                            cardVImageClaim.setCardBackgroundColor(getResources().getColor(R.color.success_load));
                        } catch (Exception e) {
                            Log.e("FILE CHOOSER", "onActivityResult: " + e);
                        }
                        Log.d("FILE CHOOSER", "onActivityResult: " + filePath);
                    }
                }
                break;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
            sVClaim.setVisibility(View.GONE);
            lottieUpload.setVisibility(View.VISIBLE);
            txtVUpload.setVisibility(View.VISIBLE);
        }

        @Override
        protected String doInBackground(String... strings) {
            Log.d("strings", "doInBackground: " + Arrays.toString(strings));
            RequestBody requestBody;

            MultipartBody.Builder builder = new MultipartBody.Builder();
            builder.setType(MultipartBody.FORM);
            for (String files : strings) {
                if (files != null) {
                    File fileSend = new File(files);
                    Log.d("fileSend", "doInBackground: " + fileSend.getName());
                    builder.addFormDataPart("files", fileSend.getName(), RequestBody.create(MediaType.parse("*/*"), fileSend));
                }
            }
            // TODO BUILD DATA
            builder.addFormDataPart("asunto", subjectClaim);
            // builder.addFormDataPart("description", descriptionClaim);
            builder.addFormDataPart("tipodoc", "0015");
            builder.addFormDataPart("tiposerv", "3");
            requestBody = builder.build();

            try {
                // TODO URL CLAIM
                Request request = new Request.Builder()
                        .url(URL_BASE + "documento/register")
                        .addHeader("Authorization", "Bearer " + TOKENUSER)
                        .post(requestBody)
                        .build();

                OkHttpClient okHttpClient = new OkHttpClient();
                Response response = okHttpClient.newCall(request).execute();

                Log.d("RESPONSE", "doInBackground: " + response);
                if (response != null && response.isSuccessful()) {
                    return response.body().string();
                } else {
                    return null;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("ClaimActivity", "onPostExecute: " + s);
            if (s != null) {
                Toast.makeText(ClaimActivity.this, R.string.upload_successful, Toast.LENGTH_LONG).show();
                lottieSuccess.setVisibility(View.VISIBLE);
                txtVSuccess.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(ClaimActivity.this, R.string.upload_wrong, Toast.LENGTH_LONG).show();
                lottieError.setVisibility(View.VISIBLE);
                txtVError.setVisibility(View.VISIBLE);
            }
            lottieUpload.setVisibility(View.GONE);
            txtVUpload.setVisibility(View.GONE);
            // progressBar.setVisibility(View.GONE);
        }
    }
}