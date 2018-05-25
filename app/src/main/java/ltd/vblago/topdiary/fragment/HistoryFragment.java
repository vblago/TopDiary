package ltd.vblago.topdiary.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.adapter.RecyclerAdapter;
import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.model.FullList;
import ltd.vblago.topdiary.util.EntryComparator;
import ltd.vblago.topdiary.util.MainActivityCallback;

public class HistoryFragment extends Fragment implements RecyclerAdapter.AdapterCallback {

    @BindView(R.id.recyclerview)
    public RecyclerView mRecyclerView;
    @BindView(R.id.dateTitle) public TextView dateTv;
    ArrayList<Entry> list;

    Unbinder unbinder;
    MainActivityCallback mainActivityCallback;
    RecyclerAdapter mAdapter;
    Calendar date;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_history, container, false);

        unbinder = ButterKnife.bind(this, root);

        if (list != null){
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            Comparator<Entry> comparator = new EntryComparator();
            Collections.sort(list, comparator);

            mAdapter = new RecyclerAdapter(list, this);
            mRecyclerView.setAdapter(mAdapter);
        }

        dateTv.setText(formatDate(date));

        return root;
    }

    public static HistoryFragment newInstance(ArrayList<Entry> list, Calendar calendar) {
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        args.putSerializable("calendar", calendar);
        HistoryFragment fragment = new HistoryFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            list = (ArrayList<Entry>) getArguments().getSerializable("list");
            date = (Calendar) getArguments().getSerializable("calendar");
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivityCallback) {
            mainActivityCallback = (MainActivityCallback) context;
        } else {
            throw new RuntimeException(Context.class.getSimpleName() + " must implement ActivityCommunication interface");
        }
    }


    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(int position, boolean checked) {
        Toast.makeText(getContext(), "You can do this task today, also you can shift or delete it", Toast.LENGTH_LONG).show();
    }

    public String formatDate(Calendar calendar) {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        return sdf.format(calendar.getTime());
    }

    @Override
    public void onShortClick(int position) {
        mainActivityCallback.openDescription(list.get(position));
    }

    @Override
    public void onLongClick(final int position) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setMessage("Choose action"); // сообщение
        ad.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                mainActivityCallback.editItem(list.get(position));
            }
        });
        ad.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                mainActivityCallback.removeItem(list.get(position));
                list.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });
        ad.show();
    }

    @Override
    public void shiftBtnClicked(int position) {
        mainActivityCallback.editItem(list.get(position));
    }

}
