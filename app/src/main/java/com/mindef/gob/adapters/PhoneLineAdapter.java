package com.mindef.gob.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.mindef.gob.R;
import com.mindef.gob.models.PhoneLine;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import de.hdodenhof.circleimageview.CircleImageView;

public class PhoneLineAdapter extends RecyclerView.Adapter<PhoneLineAdapter.ViewHolder> implements View.OnClickListener {
    private Context mContext;
    private ArrayList<PhoneLine> phoneLines;
    private View.OnClickListener listener;

    public void setOnclickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public PhoneLineAdapter(Context mContext, ArrayList<PhoneLine> phoneLines) {
        this.mContext = mContext;
        this.phoneLines = phoneLines;
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
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_phone_lines, parent, false);
        PhoneLineAdapter.ViewHolder phoneLineViewHolder = new PhoneLineAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return phoneLineViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        PhoneLine item = phoneLines.get(position);
        viewHolder.bindPhoneLine(item);
    }

    @Override
    public int getItemCount() {
        return phoneLines.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtV_item_id_establishment, txtV_item_name_establishment, txtV_item_direction_establishment, txtV_item_directory_establishment;
        private CircleImageView imgV_item_image_establishment;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txtV_item_id_establishment = itemView.findViewById(R.id.txtV_item_id_establishment);
            txtV_item_name_establishment = itemView.findViewById(R.id.txtV_item_name_establishment);
            txtV_item_direction_establishment = itemView.findViewById(R.id.txtV_item_direction_establishment);
            txtV_item_directory_establishment = itemView.findViewById(R.id.txtV_item_directory_establishment);

            imgV_item_image_establishment = itemView.findViewById(R.id.imgV_item_image_establishment);

        }

        public void bindPhoneLine(PhoneLine phoneLine) {
            txtV_item_id_establishment.setText(String.valueOf(phoneLine.getIdPhoneLine()));
            txtV_item_name_establishment.setText(phoneLine.getNamePhoneLine());
            txtV_item_direction_establishment.setText(phoneLine.getDirectionPhoneLine());
            txtV_item_directory_establishment.setText(phoneLine.getDirectoryPhoneLine());

            Glide.with(mContext)
                    .load(phoneLine.getImagePhoneLine())
                    .centerCrop()
                    .into(imgV_item_image_establishment);
        }
    }
}
