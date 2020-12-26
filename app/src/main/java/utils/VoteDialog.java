package utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.multiplechoiceexaminationapp.R;

import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;

public class VoteDialog extends AppCompatDialogFragment {
    private ExampleDialogListener listener;
    ImageButton star_1, star_2, star_3, star_4, star_5;
    int rate;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        star_1 = view.findViewById(R.id.rvstar_1);
        star_2 = view.findViewById(R.id.rvstar_2);
        star_3 = view.findViewById(R.id.rvstar_3);
        star_4 = view.findViewById(R.id.rvstar_4);
        star_5 = view.findViewById(R.id.rvstar_5);
        List<ImageButton> stars = Arrays.asList(star_1, star_2, star_3, star_4, star_5);
        for (int i = 0; i < stars.size(); i++) {
            final int count = i;
            stars.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int j = 0; j <= count; j++) {
                        stars.get(j).setImageResource(R.drawable.choose_star);
                        Toast.makeText(getContext(), "Thanks for your choice", Toast.LENGTH_LONG).show();
                    }
                    rate = count;
                }
            });
        }
        builder.setView(view).setTitle("Hãy đóng góp trước khi thoát").setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                listener.applyVote(rate);
            }
        });
        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener = (ExampleDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement ExampleDialogListener");
        }
    }

    public interface ExampleDialogListener {
        void applyVote(int rate);
    }
}
