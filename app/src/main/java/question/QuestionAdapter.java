package question;


import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexaminationapp.R;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import Object.*;

public class QuestionAdapter extends RecyclerView.Adapter<QuestionAdapter.ViewHolder> {
    Context context;
    List<Question> listQuestion;
    OnItemClickListener listener;
    Map<Integer, Integer> answers;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public QuestionAdapter(Context context, List<Question> listQuestion, Map<Integer, Integer> answers) {
        super();
        this.context = context;
        this.listQuestion = listQuestion;
        this.answers = answers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_question, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Question question = listQuestion.get(position);
        List<String> answer = new ArrayList<>();
        for (Entry<Integer, String> entry : question.getAnswer().entrySet()) {
            answer.add(entry.getValue().toString());
        }
        List<RadioButton> radioButtons = Arrays.asList(holder.answer_1, holder.answer_2, holder.answer_3, holder.answer_4);
        for (int i = 0; i < radioButtons.size(); i++) {
            radioButtons.get(i).setText(answer.get(i));
        }
        holder.ques_content.setText("Câu " + (position + 1) + ": " + question.getQuestion());

        holder.group_button_answ.setTag(position);
        holder.group_button_answ.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.answer_1:
                        answers.replace(position, 1);
                        break;
                    case R.id.answer_2:
                        answers.replace(position, 2);
                        break;
                    case R.id.answer_3:
                        answers.replace(position, 3);
                        break;
                    case R.id.answer_4:
                        answers.replace(position, 4);
                        break;
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return listQuestion.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        TextView ques_content;
        RadioGroup group_button_answ;
        RadioButton answer_1, answer_2, answer_3, answer_4;
        Integer selectedId = 0;

        public ViewHolder(@NonNull View v) {
            super(v);
            ques_content = v.findViewById(R.id.question_content);
            group_button_answ = v.findViewById(R.id.group_button_answ);
            answer_1 = v.findViewById(R.id.answer_1);
            answer_2 = v.findViewById(R.id.answer_2);
            answer_3 = v.findViewById(R.id.answer_3);
            answer_4 = v.findViewById(R.id.answer_4);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null) {
                        int position = getAdapterPosition();
                        if (position != RecyclerView.NO_POSITION) {
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }
}



