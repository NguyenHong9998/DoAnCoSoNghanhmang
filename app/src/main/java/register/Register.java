package register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import Object.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import login.Login;
import utils.SocketUtil;

import com.example.multiplechoiceexaminationapp.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.net.URISyntaxException;

public class Register extends AppCompatActivity {
    TextView id, name, grade, pass, confirm_pass, notifi, login_notifi;
    Button register_button;
    ImageButton show_pass;
    Socket socket;
    Student student;
    int countOfShowPass = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        id = findViewById(R.id.id);
        name = findViewById(R.id.name);
        grade = findViewById(R.id.grade);
        pass = findViewById(R.id.password);
        confirm_pass = findViewById(R.id.password_confirm);
        notifi = findViewById(R.id.notifi);
        login_notifi = findViewById(R.id.login_notifi);
        register_button = findViewById(R.id.register_button);
        show_pass = findViewById(R.id.show_pass);
        try {
            socket = SocketUtil.getConnection();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();

        register_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String idString = id.getText().toString();
                final String nameString = name.getText().toString();
                if (nameString.isEmpty()) Log.d("aaaaaaaaa", "yyyy");
                final String gradeString = grade.getText().toString();
                final String passString = pass.getText().toString();
                String confirmPassString = confirm_pass.getText().toString();
                if (!idString.isEmpty() && !nameString.isEmpty() && !gradeString.isEmpty() && !passString.isEmpty() && !confirmPassString.isEmpty()) {
                    if (confirmPassString.equals(passString)) {
                        final Student student = new Student(idString, nameString, gradeString, 0f, passString);
                        try {
                            JSONObject idStdJSon = new JSONObject();
                            idStdJSon.put("idStudent", idString);

                            socket.emit("registerAccount", idStdJSon);
                        } catch (Exception e) {
                        }
                        socket.on("check existed account", new Emitter.Listener() {
                            @Override
                            public void call(final Object... args) {
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        JSONObject json = (JSONObject) args[0];
                                        try {
                                            Boolean isExistedAcc = json.getBoolean("isExisted");
                                            if (isExistedAcc) {
                                                notifi.setText("Id tài khoản đã tồn tại!");
                                                notifi.setVisibility(View.VISIBLE);
                                            } else {
                                                //  Student student = new Student(idString, nameString, gradeString, 0f, passString);
                                                JSONObject jsonStd = new JSONObject();
                                                jsonStd.put("idStudent", idString);
                                                jsonStd.put("name", nameString);
                                                jsonStd.put("grade", gradeString);
                                                jsonStd.put("score", 0f);
                                                jsonStd.put("pass", passString);
                                                socket.emit("accountReg", jsonStd);
                                                notifi.setText("Đăng ký thành công!");
                                                notifi.setVisibility(View.VISIBLE);
                                                register_button.setEnabled(false);
                                            }
                                        } catch (Exception e) {
                                        }
                                    }
                                });
                            }
                        });

                    } else {
                        notifi.setText("Xác nhận mật khẩu không đúng!");
                        notifi.setVisibility(View.VISIBLE);
                    }
                } else {
                    notifi.setText("Hãy điền đủ thông tin!");
                    notifi.setVisibility(View.VISIBLE);
                }

            }
        });

        login_notifi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Login.class);
                startActivity(intent);
            }
        });
        show_pass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                countOfShowPass++;
                if (countOfShowPass % 2 != 0) {
                    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    confirm_pass.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    confirm_pass.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
    }

}