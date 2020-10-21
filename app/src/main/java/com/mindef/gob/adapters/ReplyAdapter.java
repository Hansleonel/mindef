package com.mindef.gob.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.mindef.gob.R;
import com.mindef.gob.models.Reply;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReplyAdapter extends RecyclerView.Adapter<ReplyAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<Reply> replies;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public ReplyAdapter(Context context, ArrayList<Reply> replies) {
        this.context = context;
        this.replies = replies;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_document, parent, false);
        ReplyAdapter.ViewHolder replyViewHolder = new ReplyAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return replyViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewholder, int position) {
        Reply item = replies.get(position);
        viewholder.bindReply(item);
    }

    @Override
    public int getItemCount() {
        return replies.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView txtV_item_reply_code, txtV_item_reply_date, txtV_item_reply_serie, txtV_item_reply_subject, txtV_item_reply_source, txtV_item_reply_idFile;
        private ImageView imgV_item_reply_type;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtV_item_reply_code = itemView.findViewById(R.id.txtV_item_document_track);
            txtV_item_reply_date = itemView.findViewById(R.id.txtV_item_document_date);
            txtV_item_reply_serie = itemView.findViewById(R.id.txtV_item_document_title);
            txtV_item_reply_subject = itemView.findViewById(R.id.txtV_item_document_subject);
            txtV_item_reply_idFile = itemView.findViewById(R.id.txtV_item_document_id);
            txtV_item_reply_source = itemView.findViewById(R.id.txtV_item_document_principal);
            imgV_item_reply_type = itemView.findViewById(R.id.imgV_type);
        }

        public void bindReply(Reply reply) {
            txtV_item_reply_code.setText(reply.getCodeReply());
            txtV_item_reply_date.setText(reply.getDateReply());
            txtV_item_reply_serie.setText(reply.getSerieReply());
            txtV_item_reply_subject.setText(reply.getSubjectReply());
            txtV_item_reply_source.setText(reply.getSourceReply());
            txtV_item_reply_idFile.setText(String.valueOf(reply.getIdFileReply()));

            if (reply.getSerieReply().startsWith("CA")) {
                imgV_item_reply_type.setImageResource(R.drawable.ic_document_type_c);
            }
        }
    }
}
