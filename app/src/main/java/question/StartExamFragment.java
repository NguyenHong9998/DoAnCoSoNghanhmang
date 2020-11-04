package question;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.os.Parcelable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.multiplechoiceexaminationapp.R;

import exam.Exam;
import Object.*;

public class StartExamFragment extends Fragment {
    Button start_exam_button;
    Subjects subject;

    @Override

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_start_exam2, container, false);
        start_exam_button = v.findViewById(R.id.start_exam_button);
        subject = getArguments().getParcelable("subject");
        start_exam_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Exam.class);
                intent.putExtra("subject", (Parcelable) subject);
                startActivity(intent);
            }
        });
        return v;
    }
}