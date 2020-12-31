package com.mindef.gob.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindef.gob.R;
import com.mindef.gob.models.Presentment;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PresentmentAdapter extends RecyclerView.Adapter<PresentmentAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<Presentment> presentments;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public PresentmentAdapter(Context mContext, ArrayList<Presentment> presentments) {
        this.mContext = mContext;
        this.presentments = presentments;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_presentment, parent, false);
        PresentmentAdapter.ViewHolder presentmentViewHolder = new PresentmentAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return presentmentViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Presentment item = presentments.get(position);
        viewHolder.bindPresentment(item);
    }

    @Override
    public int getItemCount() {
        return presentments.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtV_item_presentment_id, txtV_item_presentment_track, txtV_item_presentment_type, txtV_item_presentment_subject, txtV_item_presentment_date;
        private ImageView imgV_item_presentment_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtV_item_presentment_id = itemView.findViewById(R.id.txtV_item_presentment_id);
            txtV_item_presentment_track = itemView.findViewById(R.id.txtV_item_presentment_track);
            txtV_item_presentment_type = itemView.findViewById(R.id.txtV_item_presentment_type);
            txtV_item_presentment_subject = itemView.findViewById(R.id.txtV_item_presentment_subject);
            txtV_item_presentment_date = itemView.findViewById(R.id.txtV_item_presentment_date);

            imgV_item_presentment_type = itemView.findViewById(R.id.imgV_item_presentment_type);

        }

        public void bindPresentment(Presentment presentment) {
            txtV_item_presentment_id.setText(String.valueOf(presentment.getIdDocPresentment()));
            txtV_item_presentment_track.setText(presentment.getCodePresentment());
            txtV_item_presentment_type.setText(presentment.getTypePresentment());
            txtV_item_presentment_subject.setText(presentment.getSubjectPresentment());
            txtV_item_presentment_date.setText(presentment.getDatePresentment());

            if (presentment.getTypePresentment().startsWith("Q")) {
                imgV_item_presentment_type.setImageResource(R.drawable.ic_complain);
            } else if (presentment.getTypePresentment().startsWith("R")) {
                imgV_item_presentment_type.setImageResource(R.drawable.ic_claim);
            }
        }
    }
}
