package login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexaminationapp.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;
import Object.*;
import io.socket.emitter.Emitter;

public class Login extends AppCompatActivity {
    MediaCodec.QueueRequest queueRequest;
    Button login_button;
    EditText id;
    EditText password;
    LoginAccount loginAccount;
    Student student;
    Socket socket;
    TextView notifi, register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        id = findViewById(R.id.id);
        password = findViewById(R.id.password);
        login_button = findViewById(R.id.login_button);
        notifi = findViewById(R.id.notifi);
        register = findViewById(R.id.register);
        try {
            socket = IO.socket("http://192.168.1.45:5000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = id.getText().toString();
                String strPass = password.getText().toString();
                JSONObject account = new JSONObject();
                if (strEmail != null && strPass != null) {
                    try {
                        loginAccount = new LoginAccount(strEmail, strPass);
                        account.put("id", strEmail);
                        account.put("pass", strPass);
                        socket.emit("accLogin", account);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    final View view = v;
                    socket.on("check account", new Emitter.Listener() {
                        @Override
                        public void call(final Object... args) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject status = (JSONObject) args[0];

                                    try {
                                        Boolean isValid = status.getBoolean("status");
                                        Gson gson = new Gson();
                                        student = gson.fromJson(status.getJSONObject("student").toString(), Student.class);
                                        if (isValid) {
                                            notifi.setVisibility(View.INVISIBLE);
                                            Toast.makeText(Login.this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                            Intent intent = new Intent(view.getContext(), LoginSuccess.class);
                                            intent.putExtra("student", student);
                                            view.getContext().startActivity(intent);
                                        } else {
                                            Toast.makeText(Login.this, "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
                                            notifi.setVisibility(View.VISIBLE);
                                        }
                                    } catch (JSONException e) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    });
                }
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Register.class);
                v.getContext().startActivity(intent);
            }
        });
    }

}