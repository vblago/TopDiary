package ltd.vblago.topdiary.fragment;


import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.model.TimeTable;
import ltd.vblago.topdiary.util.DeleteTask;
import ltd.vblago.topdiary.util.MainActivityCallback;
import ltd.vblago.topdiary.util.ReadTask;
import ltd.vblago.topdiary.util.WriteTask;

public class ChooseGroupFragment extends Fragment {

    Unbinder unbinder;
    ArrayAdapter<String> adapter;
    MainActivityCallback mainActivityCallback;
    @BindView(R.id.list_timetables)
    ListView timeTablesLv;
    ArrayList<TimeTable> timeTableModelList;
    ArrayList<String> list;
    View view;

    public ChooseGroupFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_choose_group, container, false);
        unbinder = ButterKnife.bind(this, view);

        timeTableModelList = getListOfExistTimetables();
        list = new ArrayList<>();
        if (timeTableModelList == null){
            timeTableModelList = new ArrayList<>();
        }
        for (TimeTable timeTable:timeTableModelList) {
            list.add(timeTable.getName());
        }

        Collections.sort(list);
        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, list);
        timeTablesLv.setAdapter(adapter);
        timeTablesLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                deleteAlert(list.get(position));
                return false;
            }
        });

        view.setFocusableInTouchMode(true);
        view.requestFocus();

        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (event.getAction() == KeyEvent.ACTION_DOWN) {
                    if (keyCode == KeyEvent.KEYCODE_BACK) {
                        mainActivityCallback.chooseGroupBack();
                        return true;
                    }
                }
                return false;
            }
        });

        return view;
    }

    public void deleteAlert(final String name){
        AlertDialog.Builder ad = new AlertDialog.Builder(getContext());
        ad.setMessage("Delete " + name); // сообщение
        ad.setPositiveButton("Delete", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                deleteTimetable(name);
                adapter.remove(name);
                view.setFocusableInTouchMode(true);
                view.requestFocus();
            }
        });
        ad.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int arg1) {
                dialog.cancel();
            }
        });
        ad.show();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.choose_groups_menu, menu);
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

    private ArrayList<TimeTable> getListOfExistTimetables() {
        ReadTask readTask = new ReadTask(getContext(), "timetable-model-list");
        return  (ArrayList<TimeTable>) readTask.getObjectNotBackground();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_group:
                mainActivityCallback.openAddGroupFragment();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void deleteTimetable(String name){
        DeleteTask deleteTask = new DeleteTask(getContext(), name);
        deleteTask.execute();

        int i = 0;
        while (!timeTableModelList.get(i).getName().equals(name))i++;
        timeTableModelList.remove(i);

        WriteTask writeTaskSet = new WriteTask(getContext(), "timetable-model-list", timeTableModelList);
        writeTaskSet.execute();
    }

    @Override
    public void onDetach() {
        super.onDetach();
        unbinder.unbind();
    }
}
