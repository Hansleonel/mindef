package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mindef.gob.R;

public class PresentmentActivity extends AppCompatActivity {

    private LinearLayout lVPresentmentAnimation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_presentment);

        lVPresentmentAnimation = findViewById(R.id.lVPresentmentAnimation);

        findViewById(R.id.lVComplaint).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresentmentActivity.this, ComplaintActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.lVClaim).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PresentmentActivity.this, ClaimActivity.class);
                startActivity(intent);
            }
        });

        dataDocumentsPresentment();
    }

    private void dataDocumentsPresentment() {
        lVPresentmentAnimation.setVisibility(View.VISIBLE);
    }
}