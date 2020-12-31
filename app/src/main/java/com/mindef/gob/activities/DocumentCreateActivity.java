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

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
import com.mindef.gob.R;
import com.mindef.gob.utilities.Paths;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.mindef.gob.utilities.Constants.URL_BASE;

public class DocumentCreateActivity extends AppCompatActivity {

    private static final int PDF_REQUEST_01 = 101;
    private static final int PDF_REQUEST_02 = 102;
    private static final int PDF_REQUEST_03 = 103;
    private static final int PDF_REQUEST_04 = 104;
    private static final int PDF_REQUEST_05 = 105;

    private ScrollView sV_document;
    private Spinner spV_typeDocument;
    private EditText edt_subject_document;
    private LinearLayout lV_lottie_animation;
    private CardView cardView_document_create_01, cardView_document_create_02, cardView_document_create_03, cardView_document_create_04, cardView_document_create_05;
    private TextView txtV_document_create_tittle_01, txtV_document_create_tittle_02, txtV_document_create_tittle_03, txtV_document_create_tittle_04, txtV_document_create_tittle_05, txtV_document_create_size_01, txtV_document_create_size_02, txtV_document_create_size_03, txtV_document_create_size_04, txtV_document_create_size_05, txtV_document_create_count_files;
    private ImageView arrowDocument;
    private ProgressBar progressBar;

    private LottieAnimationView lottieV_upload, lottieV_error, lottieV_success;
    private TextView txtV_document_upload_wait, txtV_document_upload_error, txtV_document_upload_success;

    private String subjectDocument;
    private String typeDoc = "0015";
    private String pdfFilePath01, pdfFilePath02, pdfFilePath03, pdfFilePath04, pdfFilePath05;
    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_create);

        sV_document = findViewById(R.id.sV_send_document);
        spV_typeDocument = findViewById(R.id.spV_typeDocument);
        edt_subject_document = findViewById(R.id.edt_subject_document);
        lV_lottie_animation = findViewById(R.id.lV_attach);
        cardView_document_create_01 = findViewById(R.id.cardV_document_create_file_01);
        cardView_document_create_02 = findViewById(R.id.cardV_document_create_file_02);
        cardView_document_create_03 = findViewById(R.id.cardV_document_create_file_03);
        cardView_document_create_04 = findViewById(R.id.cardV_document_create_file_04);
        cardView_document_create_05 = findViewById(R.id.cardV_document_create_file_05);
        txtV_document_create_tittle_01 = findViewById(R.id.txtV_document_create_tittle_01);
        txtV_document_create_tittle_02 = findViewById(R.id.txtV_document_create_tittle_02);
        txtV_document_create_tittle_03 = findViewById(R.id.txtV_document_create_tittle_03);
        txtV_document_create_tittle_04 = findViewById(R.id.txtV_document_create_tittle_04);
        txtV_document_create_tittle_05 = findViewById(R.id.txtV_document_create_tittle_05);
        txtV_document_create_size_01 = findViewById(R.id.txtV_document_create_size_01);
        txtV_document_create_size_02 = findViewById(R.id.txtV_document_create_size_02);
        txtV_document_create_size_03 = findViewById(R.id.txtV_document_create_size_03);
        txtV_document_create_size_04 = findViewById(R.id.txtV_document_create_size_04);
        txtV_document_create_size_05 = findViewById(R.id.txtV_document_create_size_05);
        txtV_document_upload_wait = findViewById(R.id.txtV_document_upload_wait);
        txtV_document_upload_error = findViewById(R.id.txtV_document_upload_error);
        txtV_document_upload_success = findViewById(R.id.txtV_document_upload_success);
        arrowDocument = findViewById(R.id.arrow_create_document);

        lottieV_upload = findViewById(R.id.lottieV_upload);
        lottieV_error = findViewById(R.id.lottieV_error);
        lottieV_success = findViewById(R.id.lottieV_success);

        progressBar = findViewById(R.id.pbV_create_document_upload);

        txtV_document_create_count_files = findViewById(R.id.txtV_document_create_count_files);

        List<String> tipoDoc = new ArrayList<>();
        tipoDoc.add("Seleccione el tipo de Documento");
        tipoDoc.add("Carta");
        tipoDoc.add("Solicitud");
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter(getApplicationContext(), android.R.layout.simple_list_item_1, tipoDoc);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spV_typeDocument.setAdapter(spinnerAdapter);

        spV_typeDocument.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                Log.d("DocumentCreateActivity", "onItemSelected: " + position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        getUserTokenShared();

        lV_lottie_animation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lV_lottie_animation.setVisibility(View.GONE);
                cardView_document_create_01.setVisibility(View.VISIBLE);
                cardView_document_create_02.setVisibility(View.VISIBLE);
                cardView_document_create_03.setVisibility(View.VISIBLE);
                cardView_document_create_04.setVisibility(View.VISIBLE);
                cardView_document_create_05.setVisibility(View.VISIBLE);
                // txtV_document_create_count_files.setVisibility(View.VISIBLE);
                txtV_document_create_count_files.setText(R.string.sending_document);
            }
        });

        cardView_document_create_01.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                // intent.setAction(Intent.ACTION_GET_CONTENT);
                // intent.setType("application/pdf");
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), PDF_REQUEST_01);
            }
        });

        cardView_document_create_02.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), PDF_REQUEST_02);
            }
        });

        cardView_document_create_03.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), PDF_REQUEST_03);
            }
        });

        cardView_document_create_04.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), PDF_REQUEST_04);
            }
        });

        cardView_document_create_05.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setAction(Intent.ACTION_PICK);
                intent.setType("*/*");
                startActivityForResult(Intent.createChooser(intent, "Choose File to Upload"), PDF_REQUEST_05);
            }
        });

        findViewById(R.id.btn_start).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                subjectDocument = edt_subject_document.getText().toString();
                if (pdfFilePath01 == null && pdfFilePath02 == null && pdfFilePath03 == null && pdfFilePath04 == null && pdfFilePath05 == null) {
                    Toast.makeText(DocumentCreateActivity.this, R.string.sending_empty, Toast.LENGTH_LONG).show();
                    return;
                }

                if (subjectDocument.length() <= 10) {
                    Toast.makeText(DocumentCreateActivity.this, R.string.upload_wrong_subject, Toast.LENGTH_LONG).show();
                    return;
                }
                UploadTask uploadTask = new UploadTask();
                uploadTask.execute(new String[]{pdfFilePath01, pdfFilePath02, pdfFilePath03, pdfFilePath04, pdfFilePath05});

            }
        });

        arrowDocument.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQUEST_01) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            Log.d("Uri", "onActivityResult: " + uri);
            String pathPDF = Paths.getFilePath(DocumentCreateActivity.this, uri);
            Log.d("File Path01", "onActivityResult: " + pathPDF);
            if (pathPDF != null) {
                txtV_document_create_tittle_01.setText("" + new File(pathPDF).getName());
                txtV_document_create_size_01.setText("" + new File(pathPDF).length() / 1000000.0 + "Mb");
                cardView_document_create_01.setCardBackgroundColor(getResources().getColor(R.color.success_load));
            }
            pdfFilePath01 = pathPDF;
            /*Uri uri = data.getData();
            Log.d("Uri", "onActivityResult: " + uri);
            Cursor cursor = null;
            final String[] projection = {
                    MediaStore.MediaColumns.DISPLAY_NAME
            };
            cursor = getApplicationContext().getContentResolver().query(uri, projection, null, null,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DISPLAY_NAME);
                String path01 = cursor.getString(index);
                Log.d("Cursor Path 01", "onActivityResult: " + path01);
            }
            if (cursor != null) {
                cursor.close();
            }

            String pathPDF = Paths.getFilePath(DocumentCreateActivity.this, uri);
            Log.d("File Path01", "onActivityResult: " + pathPDF);
            if (pathPDF != null) {
                txtV_document_create_tittle_01.setText("" + new File(pathPDF).getName());
                txtV_document_create_size_01.setText("" + new File(pathPDF).length() / 1000000.0 + "Mb");
                cardView_document_create_01.setCardBackgroundColor(getResources().getColor(R.color.success_load));
            }
            pdfFilePath01 = pathPDF;*/

        } else if (requestCode == PDF_REQUEST_02) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            Log.d("Uri", "onActivityResult: " + uri);
            String pathPDF = Paths.getFilePath(DocumentCreateActivity.this, uri);
            Log.d("File Path02", "onActivityResult: " + pathPDF);
            if (pathPDF != null) {
                txtV_document_create_tittle_02.setText("" + new File(pathPDF).getName());
                txtV_document_create_size_02.setText("" + new File(pathPDF).length() / 1000000.0 + "Mb");
                cardView_document_create_02.setCardBackgroundColor(getResources().getColor(R.color.success_load));
            }
            pdfFilePath02 = pathPDF;

        } else if (requestCode == PDF_REQUEST_03) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            String pathPDF = Paths.getFilePath(DocumentCreateActivity.this, uri);
            Log.d("File Path03", "onActivityResult: " + pathPDF);
            if (pathPDF != null) {
                txtV_document_create_tittle_03.setText("" + new File(pathPDF).getName());
                txtV_document_create_size_03.setText("" + new File(pathPDF).length() / 1000000.0 + "Mb");
                cardView_document_create_03.setCardBackgroundColor(getResources().getColor(R.color.success_load));
            }
            pdfFilePath03 = pathPDF;

        } else if (requestCode == PDF_REQUEST_04) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            String pathPDF = Paths.getFilePath(DocumentCreateActivity.this, uri);
            Log.d("File Path02", "onActivityResult: " + pathPDF);
            if (pathPDF != null) {
                txtV_document_create_tittle_04.setText("" + new File(pathPDF).getName());
                txtV_document_create_size_04.setText("" + new File(pathPDF).length() / 1000000.0 + "Mb");
                cardView_document_create_04.setCardBackgroundColor(getResources().getColor(R.color.success_load));
            }
            pdfFilePath04 = pathPDF;

        } else if (requestCode == PDF_REQUEST_05) {
            if (data == null) {
                return;
            }
            Uri uri = data.getData();
            String pathPDF = Paths.getFilePath(DocumentCreateActivity.this, uri);
            Log.d("File Path02", "onActivityResult: " + pathPDF);
            if (pathPDF != null) {
                txtV_document_create_tittle_05.setText("" + new File(pathPDF).getName());
                txtV_document_create_size_05.setText("" + new File(pathPDF).length() / 1000000.0 + "Mb");
                cardView_document_create_05.setCardBackgroundColor(getResources().getColor(R.color.success_load));
            }
            pdfFilePath05 = pathPDF;

        }
    }

    public class UploadTask extends AsyncTask<String, String, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // progressBar.setVisibility(View.VISIBLE);
            sV_document.setVisibility(View.GONE);
            lottieV_upload.setVisibility(View.VISIBLE);
            txtV_document_upload_wait.setVisibility(View.VISIBLE);
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
            builder.addFormDataPart("asunto", subjectDocument);
            builder.addFormDataPart("tipodoc", "0015");
            requestBody = builder.build();

            try {
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
            Log.d("DocumentCreateActivity", "onPostExecute: " + s);
            if (s != null) {
                Toast.makeText(DocumentCreateActivity.this, R.string.upload_successful, Toast.LENGTH_LONG).show();
                lottieV_success.setVisibility(View.VISIBLE);
                txtV_document_upload_success.setVisibility(View.VISIBLE);
            } else {
                Toast.makeText(DocumentCreateActivity.this, R.string.upload_wrong, Toast.LENGTH_LONG).show();
                lottieV_error.setVisibility(View.VISIBLE);
                txtV_document_upload_error.setVisibility(View.VISIBLE);
            }
            lottieV_upload.setVisibility(View.GONE);
            txtV_document_upload_wait.setVisibility(View.GONE);
            // progressBar.setVisibility(View.GONE);
        }
    }
}
