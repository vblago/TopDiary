package ltd.vblago.topdiary.fragment;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.model.Entry;

public class DescriptionFragment extends Fragment {

    Entry entry;
    Unbinder unbinder;
    @BindView(R.id.titleTv)
    TextView titleTv;
    @BindView(R.id.placeTv)
    TextView placeTv;
    @BindView(R.id.timeTv)
    TextView timeTv;
    @BindView(R.id.descriptionTv)
    TextView descriptionTv;
    @BindView(R.id.alertImage)
    ImageView imageView;

    public DescriptionFragment() {
        // Required empty public constructor
    }

    public static DescriptionFragment newInstance(Entry entry) {
        DescriptionFragment fragment = new DescriptionFragment();
        Bundle args = new Bundle();
        args.putSerializable("entry", entry);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            this.entry = (Entry) getArguments().getSerializable("entry");
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_description, container, false);
        unbinder = ButterKnife.bind(this, view);

        titleTv.setText(entry.title);
        placeTv.setText(entry.place);
        if (entry.type != 2) {
            timeTv.setText(String.format("%s %s", entry.getTimeString(), entry.getDateString()));
        }
        if (entry.alert) {
            imageView.setVisibility(View.VISIBLE);
        }
        descriptionTv.setText(entry.description);
        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }
}
