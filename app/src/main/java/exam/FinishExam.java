package exam;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.multiplechoiceexaminationapp.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Socket;
import utils.SocketUtil;
import utils.VoteDialog;

public class FinishExam extends AppCompatActivity implements VoteDialog.ExampleDialogListener {
    TextView correct_ques;
    TextView num_questions;
    TextView scoreTv;
    Button back_button, review_button;
    Socket socket;
    String id_list_ques;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_finish_exam);
        correct_ques = findViewById(R.id.num_correct_ans);
        num_questions = findViewById(R.id.num_ques);
        back_button = findViewById(R.id.back_button);
        scoreTv = findViewById(R.id.score);
        float score = getIntent().getFloatExtra("score", 0);
        int num_correct = getIntent().getIntExtra("num_correct", 0);
        int num_ques = getIntent().getIntExtra("num_ques", 0);
        id_list_ques = getIntent().getStringExtra("id_list_ques");
        correct_ques.setText(String.valueOf(num_correct));
        scoreTv.setText(String.valueOf(score) + "/10");
        num_questions.setText(String.valueOf(num_ques));
        try {
            socket = SocketUtil.getConnection();
            socket.connect();

        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        back_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });
    }

    public void openDialog() {
        VoteDialog dialog = new VoteDialog();
        dialog.show(getSupportFragmentManager(), "vote dialog");
    }
    
    @Override
    public void applyVote(int rate) {
        JSONObject data = new JSONObject();
        try {
            data.put("rate", rate);
            data.put("id", id_list_ques);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        socket.emit("rate", data);
    }
}