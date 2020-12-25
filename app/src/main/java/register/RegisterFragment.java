package register;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.multiplechoiceexaminationapp.R;

import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import login.Login;
import utils.SocketUtil;
import Object.*;


public class RegisterFragment extends Fragment {
    TextView id, name, grade, pass, confirm_pass, notifi, login_notifi;
    Button register_button;
    ImageButton show_pass;
    Socket socket;
    int countOfShowPass = 0;



    public RegisterFragment() {
        // Required empty public constructor
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_register, container, false);

        id = v.findViewById(R.id.id);
        name = v.findViewById(R.id.name);
        grade = v.findViewById(R.id.grade);
        pass = v.findViewById(R.id.password);
        confirm_pass = v.findViewById(R.id.password_confirm);
        notifi = v.findViewById(R.id.notifi);
        login_notifi = v.findViewById(R.id.login_notifi);
        register_button = v.findViewById(R.id.register_button);
        show_pass = v.findViewById(R.id.show_pass);
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
                                getActivity().runOnUiThread(new Runnable() {
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
                                                socket.disconnect();
                                                socket.close();
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
                Login nextFrag = new Login();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.frame, nextFrag, "findThisFragment")
                        .addToBackStack(null)
                        .commit();
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
        return v;
    }
}

