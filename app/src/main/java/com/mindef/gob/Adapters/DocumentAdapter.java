package com.mindef.gob.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindef.gob.Models.Document;
import com.mindef.gob.R;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<Document> documents;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public DocumentAdapter(Context context, ArrayList<Document> documents) {
        this.mContext = context;
        this.documents = documents;
    }

    @Override
    public void onClick(View view) {
        if (listener != null) {
            listener.onClick(view);
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_document, parent, false);
        DocumentAdapter.ViewHolder documentViewHolder = new DocumentAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return documentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Document item = documents.get(position);
        viewHolder.bindDocument(item);
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtV_item_document_title, txtV_item_document_subject, txtV_item_document_date;
        private ImageView imgV_item_document_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtV_item_document_title = itemView.findViewById(R.id.txtV_item_document_title);
            txtV_item_document_subject = itemView.findViewById(R.id.txtV_item_document_subject);
            txtV_item_document_date = itemView.findViewById(R.id.txtV_item_document_date);
            imgV_item_document_type = itemView.findViewById(R.id.imgV_type);
        }

        public void bindDocument(Document document) {
            txtV_item_document_title.setText(document.getTitleDocument());
            txtV_item_document_subject.setText(document.getSubjectDocument());
            txtV_item_document_date.setText(document.getDateDocument());

        }
    }
}
