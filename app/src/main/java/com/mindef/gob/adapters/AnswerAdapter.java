package com.mindef.gob.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.mindef.gob.R;
import com.mindef.gob.models.Answer;

import java.util.ArrayList;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class AnswerAdapter extends RecyclerView.Adapter<AnswerAdapter.ViewHolder> implements View.OnClickListener {

    private Context context;
    private ArrayList<Answer> answers;
    private View.OnClickListener listener;

    public void setOnClickListener(View.OnClickListener listener) {
        this.listener = listener;
    }

    public AnswerAdapter(Context context, ArrayList<Answer> answers) {
        this.context = context;
        this.answers = answers;
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
        View view = LayoutInflater.from(context).inflate(R.layout.item_answer, parent, false);
        AnswerAdapter.ViewHolder answerViewHolder = new AnswerAdapter.ViewHolder(view);
        view.setOnClickListener(this);
        return answerViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int position) {
        Answer item = answers.get(position);
        viewHolder.bindAnswer(item);
    }

    @Override
    public int getItemCount() {
        return answers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView txtV_item_answer_title, txtV_item_answer_subject, txtV_item_answer_id;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtV_item_answer_title = itemView.findViewById(R.id.txtV_item_answer_title);
            txtV_item_answer_subject = itemView.findViewById(R.id.txtV_item_answer_subject);
            txtV_item_answer_id = itemView.findViewById(R.id.txtV_item_answer_id);
        }

        public void bindAnswer(Answer answer) {
            txtV_item_answer_title.setText(answer.getDocumentAnswer());
            txtV_item_answer_subject.setText(answer.getSubjectAnswer());
            txtV_item_answer_id.setText(String.valueOf(answer.getIdAnswer()));
        }
    }
}
