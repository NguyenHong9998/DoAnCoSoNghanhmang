package dashboard;

import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.multiplechoiceexaminationapp.R;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import Object.*;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import question.StartExamFragment;
import utils.ShareStorageUtil;
import utils.SocketUtil;


public class DashBoard extends Fragment implements SubjectAdapter.OnItemClickListener {

    RecyclerView view;
    List<Subjects> subjects;
    SubjectAdapter adapter;
    RecyclerView.LayoutManager layoutManager;
    ShareStorageUtil shareStorageUtil;
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
        shareStorageUtil = new ShareStorageUtil(getContext());
        view.setHasFixedSize(true);
        subjects = new ArrayList<>();

        try {
            socket = SocketUtil.getConnection();
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
                                subject = new Subjects();
                                subject = gson.fromJson(String.valueOf(explrObject), Subjects.class);
                                subjects.add(subject);
                            }
                            adapter = new SubjectAdapter(getActivity().getApplicationContext(), subjects);
                            view.setAdapter(adapter);
                            adapter.setOnItemClickListener(new SubjectAdapter.OnItemClickListener() {
                                @Override
                                public void onItemClick(int position) {
                                    if (shareStorageUtil.getValue("token") == "" || shareStorageUtil.getValue("token") == null) {
                                        Toast.makeText(getContext(), "Bạn hãy đăng nhập", Toast.LENGTH_LONG).show();
                                    } else {
                                        socket.disconnect();
                                        socket.close();
                                        StartExamFragment nextFrag = new StartExamFragment();
                                        Bundle bundle = new Bundle();
                                        bundle.putParcelable("subject", subjects.get(position));
                                        nextFrag.setArguments(bundle);
                                        getActivity().getSupportFragmentManager().beginTransaction()
                                                .replace(R.id.frame, nextFrag, "findThisFragment")
                                                .addToBackStack(null)
                                                .commit();
                                    }
                                }
                            });
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });
            }

        });

        return v;
    }

    @Override
    public void onItemClick(int position) {

    }
}