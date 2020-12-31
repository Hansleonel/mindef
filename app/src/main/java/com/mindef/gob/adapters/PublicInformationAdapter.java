package com.mindef.gob.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindef.gob.R;
import com.mindef.gob.models.PublicInformation;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PublicInformationAdapter extends RecyclerView.Adapter<PublicInformationAdapter.ViewHolder> implements View.OnClickListener {

    private Context mContext;
    private ArrayList<PublicInformation> publicInformations;
    private View.OnClickListener listener;

    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public PublicInformationAdapter(Context mContext, ArrayList<PublicInformation> publicInformations) {
        this.mContext = mContext;
        this.publicInformations = publicInformations;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_information, parent, false);
        PublicInformationAdapter.ViewHolder publicInformationViewHolder = new PublicInformationAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return publicInformationViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PublicInformation item = publicInformations.get(position);
        viewHolder.bindDocument(item);
    }

    @Override
    public int getItemCount() {
        return publicInformations.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtV_item_public_information_id, txtV_item_public_information_track, txtV_item_public_information_subject, txtV_item_public_information_date;
        private ImageView imgV_item_public_information;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtV_item_public_information_id = itemView.findViewById(R.id.txtV_item_public_information_id);
            txtV_item_public_information_track = itemView.findViewById(R.id.txtV_item_public_information_track);
            txtV_item_public_information_subject = itemView.findViewById(R.id.txtV_item_public_information_subject);
            txtV_item_public_information_date = itemView.findViewById(R.id.txtV_item_public_information_date);

            imgV_item_public_information = itemView.findViewById(R.id.imgV_item_public_information);
        }

        public void bindDocument(PublicInformation publicInformation) {
            txtV_item_public_information_id.setText(String.valueOf(publicInformation.getIdDocPublicInformation()));
            txtV_item_public_information_track.setText(publicInformation.getCodePublicInformation());
            txtV_item_public_information_subject.setText(publicInformation.getSubjectPublicInformation());
            txtV_item_public_information_date.setText(publicInformation.getDatePublicInformation());
        }
    }
}
