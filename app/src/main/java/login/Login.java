package login;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexaminationapp.MainActivity;
import com.example.multiplechoiceexaminationapp.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import io.socket.client.Socket;
import Object.*;
import io.socket.emitter.Emitter;
import register.RegisterFragment;
import utils.ShareStorageUtil;
import utils.SocketUtil;

public class Login extends Fragment {
    Button login_button;
    ImageButton show_pass;
    EditText id;
    EditText password;
    LoginAccount loginAccount;
    Student student;
    Socket socket;
    TextView notifi, register;
    ShareStorageUtil shareStorageUtil;
    int countOfShowPass = 0;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.shareStorageUtil = new ShareStorageUtil(getContext());
        View v = inflater.inflate(R.layout.activity_login, container, false);
        id = v.findViewById(R.id.id);
        password = v.findViewById(R.id.password);
        login_button = v.findViewById(R.id.login_button);
        show_pass =v.findViewById(R.id.show_pass_login);
        notifi = v.findViewById(R.id.notifi);
        register = v.findViewById(R.id.register);
        try {
            socket = SocketUtil.getConnection();
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
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    JSONObject status = (JSONObject) args[0];
                                    try {
                                        Boolean isValid = status.getBoolean("status");
                                        if (status.getJSONObject("student").toString() == "") {
                                            isValid = false;
                                        }
                                        Gson gson = new Gson();
                                        if (isValid) {
                                            student = gson.fromJson(status.getJSONObject("student").toString(), Student.class);
                                            notifi.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                            shareStorageUtil.applyValue("token", status.getJSONObject("student").toString());
                                            socket.disconnect();
                                            Intent intent = new Intent(getContext(), MainActivity.class);
                                            startActivity(intent);
                                        } else {
                                            Toast.makeText(getContext(), "Đăng nhập không thành công", Toast.LENGTH_LONG).show();
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
                socket.disconnect();
                socket.close();
                RegisterFragment nextFrag = new RegisterFragment();
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
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });

        return v;

    }


}