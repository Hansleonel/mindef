package com.mindef.gob.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mindef.gob.R;

public class ProfileFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        view.findViewById(R.id.logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences sharedPreferences = getContext().getSharedPreferences("UserSharedFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.clear();
                editor.apply();

                SharedPreferences sharedPreferencesT = getContext().getSharedPreferences("TokenSharedFile", Context.MODE_PRIVATE);
                SharedPreferences.Editor editorT = sharedPreferencesT.edit();
                editorT.clear();
                editorT.apply();

                getActivity().finish();
            }
        });
        return view;
    }
}
