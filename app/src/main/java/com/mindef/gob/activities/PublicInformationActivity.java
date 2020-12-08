package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mindef.gob.R;

public class PublicInformationActivity extends AppCompatActivity {

    private LinearLayout lVPublicInformationAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_public_information);

        lVPublicInformationAnimation = findViewById(R.id.lVPublicInformationAnimation);

        LinearLayout lVPublicInformationAccess = findViewById(R.id.lVPublicInformationAccess);
        lVPublicInformationAccess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PublicInformationActivity.this, ConditionActivity.class);
                startActivity(intent);
            }
        });

        dataDocumentPublicInformation();
    }

    private void dataDocumentPublicInformation() {
        lVPublicInformationAnimation.setVisibility(View.VISIBLE);
    }
}