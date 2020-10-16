package dashboard;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.multiplechoiceexaminationapp.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.Subject;

import Object.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class DashBoard extends Fragment {

    RecyclerView view;
    List<Subjects> subjects;
    SubjectAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    Subjects subject;
    Socket socket;

    public DashBoard() {

    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_dash_board, container, false);
        view = (RecyclerView) v.findViewById(R.id.gridView);
        layoutManager = new LinearLayoutManager(this.getActivity());
        view.setLayoutManager(layoutManager);

        view.setHasFixedSize(true);
        DividerItemDecoration divider = new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL);
        divider.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.recycler_view_divider));
        view.addItemDecoration(divider);
        subjects = new ArrayList<>();

        try {
            socket = IO.socket("http://192.168.1.118:5000/");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        socket.connect();
        socket.emit("send subject");

        socket.on("get subjects", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                DashBoard.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONArray subjectJSON = (JSONArray) args[0];

                        try {
                            for (int i = 0; i < subjectJSON.length(); i++) {
                                JSONObject explrObject = subjectJSON.getJSONObject(i);
                                Gson gson = new Gson();
                                subject= new Subjects();
                                subject = gson.fromJson(String.valueOf(explrObject), Subjects.class);
                                subjects.add(subject);
                                adapter = new SubjectAdapter(getActivity().getApplicationContext(), subjects);
                                view.setAdapter(adapter);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

        return v;
    }

}