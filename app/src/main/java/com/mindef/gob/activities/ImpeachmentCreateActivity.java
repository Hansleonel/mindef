package com.mindef.gob.activities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.mindef.gob.R;
import com.mindef.gob.utilities.Files;

import java.io.File;

public class ImpeachmentCreateActivity extends AppCompatActivity {

    private LinearLayout lVPersonImpeachment;
    private EditText edtPersonImpeachment;
    private CardView cardVDocumentImpeachment;
    private TextView txtVImgAttachedImpeachment;
    private TextView txtVImgAttachedImpeachmentSize;
    private EditText edtSubjectImpeachment;
    private Button btnVPersonImpeachment;
    private Button btnImpeachment;

    private String pdfFileImpeachmentPath;

    private static final int RESULT_CODE_FILE_CHOOSER = 1000;
    private static final int RESULT_ADD_PERSON = 100;
    private String TOKENUSER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impeachment_create);

        lVPersonImpeachment = findViewById(R.id.lVPersonImpeachment);
        edtPersonImpeachment = findViewById(R.id.edtPersonImpeachment);
        cardVDocumentImpeachment = findViewById(R.id.cardVDocumentImpeachment);
        txtVImgAttachedImpeachment = findViewById(R.id.txtVImgAttachedImpeachment);
        txtVImgAttachedImpeachmentSize = findViewById(R.id.txtVImgAttachedImpeachmentSize);
        edtSubjectImpeachment = findViewById(R.id.edtSubjectImpeachment);
        btnVPersonImpeachment = findViewById(R.id.btnVPersonImpeachment);
        btnImpeachment = findViewById(R.id.btnImpeachment);

        getUserTokenShared();

        btnVPersonImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addPersonActicity();
            }
        });

        cardVDocumentImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectFileImpeachment();
            }
        });

        btnImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendImpeachment();
            }
        });
    }

    private void getUserTokenShared() {
        SharedPreferences SP = getApplicationContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
        TOKENUSER = SP.getString("TokenUserString", "TokenDefaultValue");
    }

    private void addPersonActicity() {
        Intent intent = new Intent(ImpeachmentCreateActivity.this, ImpeachmentPersonActivity.class);
        startActivityForResult(intent, RESULT_ADD_PERSON);
    }

    private void selectFileImpeachment() {
        Intent chooseFileIntent = new Intent(Intent.ACTION_GET_CONTENT);
        chooseFileIntent.setType("*/*");
        chooseFileIntent.addCategory(Intent.CATEGORY_OPENABLE);
        chooseFileIntent = Intent.createChooser(chooseFileIntent, "Seleccione Archivo");
        startActivityForResult(chooseFileIntent, RESULT_CODE_FILE_CHOOSER);
    }

    private void sendImpeachment() {
        Log.d("countView", "sendImpeachment: " + lVPersonImpeachment.getChildCount());
        if (lVPersonImpeachment.getChildCount() <= 0) {
            Toast.makeText(this, R.string.accused + " " + lVPersonImpeachment.getChildCount(), Toast.LENGTH_SHORT).show();
        } else if (edtSubjectImpeachment.getText().toString().trim().isEmpty()) {
            Toast.makeText(this, R.string.impeachment_subject_description, Toast.LENGTH_SHORT).show();
        } else {
            for (int i = 0; i < lVPersonImpeachment.getChildCount(); i++) {
                View nameView = lVPersonImpeachment.getChildAt(i);
                TextView txtVPersonName = nameView.findViewById(R.id.chip_text);
                Log.d("lVPersonImpeachment", "sendImpeachment: " + txtVPersonName.getText().toString());
            }
        }
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
                            pdfFileImpeachmentPath = filePath;
                            txtVImgAttachedImpeachment.setText("" + new File(filePath).getName());
                            txtVImgAttachedImpeachmentSize.setText("" + new File(filePath).length() / 1000000.0 + "Mb");
                            cardVDocumentImpeachment.setCardBackgroundColor(getResources().getColor(R.color.success_load));
                        } catch (Exception e) {
                            Log.e("FILE CHOOSER", "onActivityResult: " + e);
                        }
                        Log.d("FILE CHOOSER", "onActivityResult: " + filePath);
                    }
                }
                break;

            case RESULT_ADD_PERSON:
                if (resultCode == RESULT_OK) {
                    String name = data.getExtras().getString("namePersonImpeachment");
                    addView(name);
                } else if (resultCode == RESULT_CANCELED) {
                    Toast.makeText(this, R.string.cancel, Toast.LENGTH_SHORT).show();
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void addView(String name) {
        final View chipViewCreate = getLayoutInflater().inflate(R.layout.item_person_impeachment, null, false);

        ImageView cardClear = chipViewCreate.findViewById(R.id.chip_clear_icon);
        TextView nameChip = chipViewCreate.findViewById(R.id.chip_text);
        nameChip.setText(name);

        cardClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(chipViewCreate);
            }
        });

        edtPersonImpeachment.setVisibility(View.GONE);

        lVPersonImpeachment.addView(chipViewCreate);
    }

    private void removeView(View chip) {
        Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
        lVPersonImpeachment.removeView(chip);
    }
}