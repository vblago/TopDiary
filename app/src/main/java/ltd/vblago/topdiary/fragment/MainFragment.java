package ltd.vblago.topdiary.fragment;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
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
import android.widget.DatePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.adapter.RecyclerAdapter;
import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.util.EntryComparator;
import ltd.vblago.topdiary.util.MainActivityCallback;

public class MainFragment extends Fragment
        implements RecyclerAdapter.AdapterCallback{

    @BindView(R.id.recyclerview)
    public RecyclerView mRecyclerView;
    ArrayList<Entry> list;

    Unbinder unbinder;

    MainActivityCallback mainActivityCallback;
    Calendar historyCalendar;

    public MainFragment(){

    }

    public static MainFragment newInstance(ArrayList<Entry> list) {
        Bundle args = new Bundle();
        args.putSerializable("list", list);
        MainFragment fragment = new MainFragment();
        fragment.setArguments(args);
        return fragment;
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
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            list = (ArrayList<Entry>) getArguments().getSerializable("list");
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_entry:
                mainActivityCallback.addEntry();
                return true;
            case R.id.calendar:
                datePicker();
                return true;
            case R.id.groups:
                mainActivityCallback.chooseGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_main, container, false);
        unbinder = ButterKnife.bind(this, root);

        if (list != null){
            mRecyclerView.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mRecyclerView.setLayoutManager(mLayoutManager);

            Comparator<Entry> comparator = new EntryComparator();
            Collections.sort(list, comparator);

            RecyclerAdapter mAdapter = new RecyclerAdapter(list, this);
            mRecyclerView.setAdapter(mAdapter);
        }

        return root;
    }

    private void datePicker() {
        historyCalendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                historyCalendar.set(Calendar.YEAR, year);
                historyCalendar.set(Calendar.MONTH, month);
                historyCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                mainActivityCallback.openHistoryFragment(historyCalendar);
            }
        }, historyCalendar.get(Calendar.YEAR), historyCalendar.get(Calendar.MONTH), historyCalendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }

    @Override
    public void onItemClick(int position, boolean checked) {
        mainActivityCallback.checkEntry(position, checked);
    }

    @Override
    public void onShortClick(int position) {
        mainActivityCallback.openDescription(position);
    }

    @Override
    public void onLongClick(final int position) {
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setMessage("Choose action"); // сообщение
        ad.setPositiveButton("Edit", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                mainActivityCallback.editItem(position);
            }
        });
        ad.setNegativeButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                mainActivityCallback.removeItem(position);
            }
        });
        ad.show();
    }

    @Override
    public void shiftBtnClicked(int position) {
        mainActivityCallback.editItem(position);
    }

}
