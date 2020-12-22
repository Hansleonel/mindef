package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.mindef.gob.R;

public class ImpeachmentCreateActivity extends AppCompatActivity {

    private LinearLayout lVPersonImpeachment;
    private Button btnVPersonImpeachment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_impeachment_create);

        lVPersonImpeachment = findViewById(R.id.lVPersonImpeachment);
        btnVPersonImpeachment = findViewById(R.id.btnVPersonImpeachment);

        btnVPersonImpeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addView();
            }
        });
    }

    private void addView() {
        final View chipViewCreate = getLayoutInflater().inflate(R.layout.item_person_impeachment, null, false);

        ImageView cardClear = chipViewCreate.findViewById(R.id.chip_clear_icon);

        cardClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeView(chipViewCreate);
            }
        });

        lVPersonImpeachment.addView(chipViewCreate);
    }

    private void removeView(View chip) {
        Toast.makeText(this, "Clear", Toast.LENGTH_SHORT).show();
        lVPersonImpeachment.removeView(chip);
    }
}