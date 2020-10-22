package dashboard;


import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.multiplechoiceexaminationapp.R;

import java.util.ArrayList;
import java.util.List;

import Object.*;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.ViewHolder> {
    Context context;
    List<Subjects> listSubjects;
    OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public SubjectAdapter(Context context, List<Subjects> listSubjects) {
        super();
        this.context = context;
        this.listSubjects = listSubjects;

    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_dashboard, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Subjects subject = listSubjects.get(position);
        holder.numOfQuestion.setText(String.valueOf(subject.getNum_ques()) + " câu hỏi");
        holder.numOfRating.setText(String.valueOf(subject.getNum_rate()) + " (rating)");
        for (int i = 0; i < 5; i++) {
            int compare = Float.compare(i, subject.getRating() - 0.5f);
            if (compare < 0) {
                holder.listStar.get(i).setImageResource(R.drawable.choose_star);
            } else if (compare == 0) {
                holder.listStar.get(i).setImageResource(R.drawable.ic_baseline_star_half_24);
            } else {
                holder.listStar.get(i).setImageResource(R.drawable.ic_baseline_star_border_24);
            }
        }
        holder.time.setText("Thời gian: " + subject.getTime().toString());
        holder.comment.setText("Chú thích: " + subject.getComment());
        holder.level.setText("Cấp độ: " + subject.getLevel().toString());
        holder.title.setText(subject.getName());

    }

    @Override
    public int getItemCount() {
        return listSubjects.size();
    }


    class ViewHolder extends RecyclerView.ViewHolder {
        List<ImageView> listStar = new ArrayList<>();
        ImageView star_1, star_2, star_3, star_4, star_5;
        TextView numOfRating, time, numOfQuestion, comment, level, title;

        public ViewHolder(@NonNull View v) {
            super(v);
            // Ánh xạ view
            star_1 = v.findViewById(R.id.star_1);
            star_2 = v.findViewById(R.id.star_2);
            star_3 = v.findViewById(R.id.star_3);
            star_4 = v.findViewById(R.id.star_4);
            star_5 = v.findViewById(R.id.star_5);
            listStar.add(star_1);
            listStar.add(star_2);
            listStar.add(star_3);
            listStar.add(star_4);
            listStar.add(star_5);
            numOfRating = v.findViewById(R.id.num_rating);
            numOfQuestion = v.findViewById(R.id.num_question);
            time = v.findViewById(R.id.time);
            level = v.findViewById(R.id.level);
            title = v.findViewById(R.id.name_exam);
            comment = v.findViewById(R.id.comment);

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



