package com.mindef.gob.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindef.gob.R;
import com.mindef.gob.models.Tracking;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrackingAdapter extends RecyclerView.Adapter<TrackingAdapter.ViewHolder> implements View.OnClickListener {
    private Context context;
    private ArrayList<Tracking> trackings;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public TrackingAdapter(Context context, ArrayList<Tracking> trackings) {
        this.context = context;
        this.trackings = trackings;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_track, parent, false);
        TrackingAdapter.ViewHolder trackingViewHolder = new TrackingAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return trackingViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Tracking item = trackings.get(position);
        viewHolder.bindDocument(item);
    }

    @Override
    public int getItemCount() {
        return trackings.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtV_item_office_track, txtV_item_date_track, txtV_item_notes_track, txtV_item_state_track;
        private View view_item_line_track;

        public ViewHolder(View itemView) {
            super(itemView);

            txtV_item_office_track = itemView.findViewById(R.id.txtV_item_office_track);
            txtV_item_date_track = itemView.findViewById(R.id.txtV_item_date_track);
            txtV_item_state_track = itemView.findViewById(R.id.txtV_item_state_track);
            view_item_line_track = itemView.findViewById(R.id.view_item_line_track);
        }

        public void bindDocument(Tracking tracking) {
            txtV_item_office_track.setText(tracking.getOfficeTracking());
            txtV_item_date_track.setText(tracking.getDateTracking());
            txtV_item_state_track.setText(tracking.getStateTracking());

            if (trackings.size() == tracking.getPositionTracking() + 1) {
                view_item_line_track.setVisibility(View.GONE);
            }
        }
    }
}
