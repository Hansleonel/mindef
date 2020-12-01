package com.mindef.gob.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mindef.gob.activities.DocumentTrackActivity;
import com.mindef.gob.activities.ImpeachmentActivity;
import com.mindef.gob.activities.PresentmentActivity;
import com.mindef.gob.activities.ProcedureActivity;
import com.mindef.gob.R;
import com.mindef.gob.activities.PublicInformationActivity;

public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        LinearLayout lv_virtual_procedure = view.findViewById(R.id.lv_virtual_procedure);
        lv_virtual_procedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ProcedureActivity.class);
                startActivity(intent);
            }
        });
        LinearLayout lv_track_procedure = view.findViewById(R.id.lv_track_procedures);
        lv_track_procedure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), DocumentTrackActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout lv_presentment = view.findViewById(R.id.lv_presentment);
        lv_presentment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PresentmentActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout lv_public_information = view.findViewById(R.id.lVPublicInformationRequest);
        lv_public_information.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), PublicInformationActivity.class);
                startActivity(intent);
            }
        });

        LinearLayout lV_impeachment = view.findViewById(R.id.lV_impeachment);
        lV_impeachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ImpeachmentActivity.class);
                startActivity(intent);
            }
        });

        return view;
    }
}
