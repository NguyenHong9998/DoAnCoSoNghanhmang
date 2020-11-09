package login;

import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.media.MediaCodec;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.multiplechoiceexaminationapp.MainActivity;
import com.example.multiplechoiceexaminationapp.R;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

import dashboard.DashBoard;
import io.socket.client.IO;
import io.socket.client.Socket;
import Object.*;
import io.socket.emitter.Emitter;
import register.Register;
import utils.ShareStorageUtil;
import utils.SocketUtil;

public class Login extends Fragment {
    MediaCodec.QueueRequest queueRequest;
    Button login_button;
    EditText id;
    EditText password;
    LoginAccount loginAccount;
    Student student;
    Socket socket;
    TextView notifi, register;
    ShareStorageUtil shareStorageUtil;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        this.shareStorageUtil = new ShareStorageUtil(getContext());
        View v = inflater.inflate(R.layout.activity_login, container, false);
        id = v.findViewById(R.id.id);
        password = v.findViewById(R.id.password);
        login_button = v.findViewById(R.id.login_button);
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
                                        Gson gson = new Gson();
                                        student = gson.fromJson(status.getJSONObject("student").toString(), Student.class);
                                        if (isValid) {
                                            notifi.setVisibility(View.INVISIBLE);
                                            Toast.makeText(getContext(), "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                                            shareStorageUtil.applyValue("token", status.getJSONObject("student").toString());
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
                Intent intent = new Intent(v.getContext(), Register.class);
                v.getContext().startActivity(intent);
            }
        });

        return v;

    }


}