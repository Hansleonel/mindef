package com.mindef.gob.Fragments.Pages;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.mindef.gob.Adapters.DocumentAdapter;
import com.mindef.gob.Models.Document;
import com.mindef.gob.R;

import java.util.ArrayList;

public class DocumentFragment extends Fragment {

    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerViewDocuments;
    private FloatingActionButton floatingActionButton;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_document, container, false);
        recyclerViewDocuments = view.findViewById(R.id.rV_documents);
        swipeRefreshLayout = view.findViewById(R.id.swipeV);
        floatingActionButton = view.findViewById(R.id.fab);

        recyclerViewDocuments.setHasFixedSize(true);
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getContext());
        DividerItemDecoration mDividerItemDecoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);

        mLinearLayoutManager.setReverseLayout(true);
        mLinearLayoutManager.setStackFromEnd(true);

        recyclerViewDocuments.setLayoutManager(mLinearLayoutManager);
        recyclerViewDocuments.addItemDecoration(mDividerItemDecoration);

        dataDocumentsAPI();

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getContext(), "SwipeRefresh", Toast.LENGTH_LONG).show();
                dataDocumentsAPI();
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Floating Action Button", Toast.LENGTH_LONG).show();
            }
        });

        return view;
    }

    private void dataDocumentsAPI() {
        ArrayList<Document> documentArrayList = new ArrayList<>();
        documentArrayList.add(new Document(1, "Solicitud", "Titulo 01", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(2, "Carta", "Titulo 02", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(3, "Carta", "Titulo 03", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(4, "Carta", "Titulo 04", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(5, "Carta", "Titulo 05", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(6, "Carta", "Titulo 06", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(7, "Solicitud", "Titulo 07", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(8, "Solicitud", "Titulo 08", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(9, "Solicitud", "Titulo 09", "Asunto 01", "01/01/2020"));
        documentArrayList.add(new Document(10, "Solicitud", "Titulo 10", "Asunto 01", "01/01/2020"));

        DocumentAdapter documentAdapter = new DocumentAdapter(getContext(), documentArrayList);
        documentAdapter.notifyDataSetChanged();

        recyclerViewDocuments.setAdapter(documentAdapter);

        swipeRefreshLayout.setRefreshing(false);

        documentAdapter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String titleDocument = ((TextView) recyclerViewDocuments.findViewHolderForAdapterPosition(recyclerViewDocuments.getChildLayoutPosition(view))
                        .itemView.findViewById(R.id.txtV_item_document_title)).getText().toString();
                Toast.makeText(getContext(), "Titulo del item" + titleDocument, Toast.LENGTH_LONG).show();
            }
        });
    }
}
