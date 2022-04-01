package com.home.coexcleducation.quiz.solution;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.home.coexcleducation.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class QuestionsAdapter extends RecyclerView.Adapter<QuestionsAdapter.QuestionsViewHolder> {
    private List<String> Questions;

    QuestionsAdapter(List<String> questions) {
        Questions = questions;
    }

    @NonNull
    @Override
    public QuestionsAdapter.QuestionsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new QuestionsViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.relative, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull QuestionsAdapter.QuestionsViewHolder questionsViewHolder, int i) {
        String qus ="Q."+(i + 1);
        String question = Questions.get(i);

        questionsViewHolder.question.setText(qus +" "+question);
        questionsViewHolder.answered.setVisibility(View.GONE);
        questionsViewHolder.correct.setVisibility(View.GONE);
    }

    @Override
    public int getItemCount() {
        return Questions.size();
    }

    class QuestionsViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_question)
        TextView question;

        @BindView(R.id.tv_answered)
        TextView answered;

        @BindView(R.id.tv_correct)
        TextView correct;

        QuestionsViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
        }
    }
}
