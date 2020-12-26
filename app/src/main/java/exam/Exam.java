package exam;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexaminationapp.MainActivity;
import com.example.multiplechoiceexaminationapp.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import question.QuestionAdapter;
import Object.*;
import question.StartExamFragment;
import utils.SocketUtil;

public class Exam extends AppCompatActivity implements QuestionAdapter.OnItemClickListener {
    RecyclerView recyclerView;
    List<Question> questions;
    QuestionAdapter adapter;
    Question question;
    Socket socket;
    TextView name_examtv;
    TextView timeout;
    Subjects subject;
    ImageButton submit_button;
    Map<Integer, Integer> selectedAnswers;
    CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_exam);
        recyclerView = findViewById(R.id.recicleview_exam);
        name_examtv = findViewById(R.id.name_exam);
        timeout = findViewById(R.id.time_out);
        submit_button = findViewById(R.id.submit_button);

        subject = getIntent().getParcelableExtra("subject");
        String time = subject.getTime();
        selectedAnswers = new LinkedHashMap<>();
        for (int i = 0; i < subject.getNum_ques(); i++) {
            selectedAnswers.put(i, 0);
        }

        Long ltimeMilis = Long.parseLong(time) * 60 * 1000;

        countDownTimer = new CountDownTimer(ltimeMilis, 1000) {
            public void onTick(long millisUntilFinished) {
                int seconds = (int) (millisUntilFinished / 1000);
                int hours = seconds / (60 * 60);
                int tempMint = (seconds - (hours * 60 * 60));
                int minutes = tempMint / 60;
                seconds = tempMint - (minutes * 60);

                timeout.setText(String.format("%02d", hours)
                        + ":" + String.format("%02d", minutes)
                        + ":" + String.format("%02d", seconds));
            }

            public void onFinish() {
                timeout.setText("Hết giờ!");

            }
        }.start();
        try {
            socket = SocketUtil.getConnection();
            socket.connect();
        } catch (Exception e) {
            e.printStackTrace();
        }
        JSONObject id = new JSONObject();
        try {
            id.put("id", subject.getId_list_ques());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("send list question", id);
        socket.on("get list question", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject ques = (JSONObject) args[0];
                        try {
                            JSONArray ques_set = ques.getJSONArray("question_set");
                            questions = new ArrayList<Question>();
                            for (int i = 0; i < ques_set.length(); i++) {
                                JSONObject questionJSON = ques_set.getJSONObject(i);
                                Map<Integer, String> answers = new LinkedHashMap<>();
                                String ques_content = questionJSON.getString("question");
                                Integer correct_ans_id = questionJSON.getInt("correct");
                                JSONArray answersJSON = questionJSON.getJSONArray("answers");
                                for (int j = 0; j < answersJSON.length(); j++) {
                                    JSONObject answer = (JSONObject) answersJSON.get(j);
                                    Integer id = answer.getInt("id");
                                    String ans_content = answer.getString("text");
                                    answers.put(id, ans_content);
                                }
                                question = new Question(ques_content, answers, correct_ans_id);
                                questions.add(question);
                            }

                            adapter = new QuestionAdapter(getApplicationContext(), questions, selectedAnswers);
                            recyclerView.setAdapter(adapter);
                            recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                });
            }
        });

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirm();
            }
        });
    }

    @Override
    public void onItemClick(int position) {
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yêu cầu xác nhận: ");
        builder.setMessage("Nếu bạn thoát, hệ thống sẽ không lưu những gì bạn đã thự hiện\n Bạn chắc chắn muốn thoát ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                socket.disconnect();
                socket.close();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void confirm() {

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Yêu cầu xác nhận: ");
        builder.setMessage("Thời gian chưa hết\n Bạn chắc chắn nộp bài chứ ?");

        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

            public void onClick(DialogInterface dialog, int which) {
                socket.disconnect();
                socket.close();
                viewScore();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public void viewScore() {
        int correct_ques = 0;
        List<Map.Entry<Integer, Integer>> entries = new ArrayList<>(selectedAnswers.entrySet());
        for (int i = 0; i < questions.size(); i++) {
            if (questions.get(i).getCorrectAnswer() == entries.get(i).getValue()) {
                correct_ques++;
            }
        }
        float score = (float) correct_ques * 10.0f / (float) questions.size();
        Intent intent = new Intent(getApplicationContext(), FinishExam.class);
        intent.putExtra("score", score);
        intent.putExtra("num_correct", correct_ques);
        intent.putExtra("num_ques", questions.size());
        intent.putExtra("id_list_ques", subject.getId_list_ques());
        startActivity(intent);
    }

}