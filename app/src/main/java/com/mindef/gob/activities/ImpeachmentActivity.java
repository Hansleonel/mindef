package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.mindef.gob.R;

public class ImpeachmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impeachment);

        LinearLayout lVImpeachment = findViewById(R.id.lVImpeachment);
        lVImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ImpeachmentActivity.this, ImpeachmentCreateActivity.class);
                startActivity(intent);
            }
        });
    }
}