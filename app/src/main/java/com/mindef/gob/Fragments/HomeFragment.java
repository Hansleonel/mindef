package com.mindef.gob.Fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.mindef.gob.Activities.ProcedureActivity;
import com.mindef.gob.R;

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

        return view;
    }
}
