package com.mindef.gob.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.mindef.gob.R;
import com.mindef.gob.adapters.PhoneLineAdapter;
import com.mindef.gob.models.PhoneLine;

import java.util.ArrayList;

public class PhoneLinesActivity extends AppCompatActivity {

    private RecyclerView rVPhoneLines;
    ArrayList<PhoneLine> phoneLineArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_phone_lines);

        rVPhoneLines = findViewById(R.id.rVPhoneLines);

        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getApplicationContext(), DividerItemDecoration.VERTICAL);

        rVPhoneLines.setLayoutManager(mLinearLayoutManager);
        rVPhoneLines.addItemDecoration(mDividerItemDecoration);

        getDataPhoneLines();
    }

    private void getDataPhoneLines() {

        phoneLineArrayList.add(new PhoneLine(1, "Sede Central", "https://portal.andina.pe/EDPfotografia3/Thumbnail/2017/03/13/000409010W.jpg", "Av. La Peruanidad, Jesus Maria", "Ver Directorio"));
        phoneLineArrayList.add(new PhoneLine(2, "Ejercito del Peru", "https://www.ejercito.mil.pe/media/k2/items/cache/0aa092ec2d4bbcf47bfbbc9bf74dc198_XL.jpg", "Av. La Peruanidad, Jesus Maria", "Ver Directorio"));
        phoneLineArrayList.add(new PhoneLine(3, "Marina de Guerra", "https://www.infodefensa.com/latam/images_cache/2019/06/01/imdex2019-singapur-valmmanuelvasconesmorey-mgp-marinaguerraperu-520.jpg", "Av. La Peruanidad, Jesus Maria", "Ver Directorio"));
        phoneLineArrayList.add(new PhoneLine(4, "Fuerza Area", "https://cde.laprensa.e3.pe/ima/0/0/1/7/6/176291.jpg", "Av. La Peruanidad, Jesus Maria", "Ver Directorio"));

        PhoneLineAdapter phoneLineAdapter = new PhoneLineAdapter(getApplicationContext(), phoneLineArrayList);
        phoneLineAdapter.notifyDataSetChanged();

        rVPhoneLines.setAdapter(phoneLineAdapter);

        phoneLineAdapter.setOnclickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idPhoneLine = ((TextView) rVPhoneLines.findViewHolderForAdapterPosition(rVPhoneLines.getChildLayoutPosition(view))
                        .itemView.findViewById(R.id.txtV_item_id_establishment)).getText().toString();

                Intent intent = new Intent(PhoneLinesActivity.this, EstablishmentActivity.class);
                startActivity(intent);
            }
        });
    }
}