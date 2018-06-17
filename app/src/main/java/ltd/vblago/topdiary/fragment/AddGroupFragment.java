package ltd.vblago.topdiary.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
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
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.TreeMap;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnTextChanged;
import butterknife.Unbinder;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.model.NURE.Direction;
import ltd.vblago.topdiary.model.NURE.Faculty;
import ltd.vblago.topdiary.model.NURE.Group;
import ltd.vblago.topdiary.model.NURE.Nure;
import ltd.vblago.topdiary.model.NURE.Speciality;
import ltd.vblago.topdiary.model.NURE.Teachers.Department;
import ltd.vblago.topdiary.model.NURE.Teachers.NureTeachers;
import ltd.vblago.topdiary.model.NURE.Teachers.Teacher;
import ltd.vblago.topdiary.model.NURE.TimeTable.TimeTable;
import ltd.vblago.topdiary.model.NURE.University;
import ltd.vblago.topdiary.util.EventFormator;
import ltd.vblago.topdiary.util.MainActivityCallback;
import ltd.vblago.topdiary.util.ReadTask;
import ltd.vblago.topdiary.util.Retrofit;
import ltd.vblago.topdiary.util.WriteTask;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class AddGroupFragment extends Fragment {

    Unbinder unbinder;
    List<String> groups;
    List<String> groupSaveInstance;
    List<String> teacherList;
    Map<String, Integer> groupsMap;
    ArrayAdapter<String> adapter;
    MainActivityCallback mainActivityCallback;
    ArrayList<ltd.vblago.topdiary.model.TimeTable> timeTableModelList;

    @BindView(R.id.list_nure)
    ListView listNure;
    @BindView(R.id.search_group)
    EditText searchEdit;
//    @BindView(R.id.titleTv)
//    TextView titleTv;
//    @BindView(R.id.check_timetable)
//    Switch checkTimeTable;

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
    }

    @OnTextChanged(R.id.search_group)
    public void searchGroup() {
        List<String> filteredList = new ArrayList<>();
        String query = searchEdit.getText().toString();
        for (String title : groupSaveInstance) {
            if (title.toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(title);
            }
        }
        groups.clear();
        groups.addAll(filteredList);
        adapter.notifyDataSetChanged();
    }

//    @OnClick(R.id.check_timetable)
//    public void switchTimeTable(View v){
//        SharedPreferences settings = getContext().getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = settings.edit();
//        if (checkTimeTable.isChecked()){
//            editor.putBoolean("timetable", true);
//        }else {
//            editor.putBoolean("timetable", false);
//        }
//        editor.apply();
//    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_add_group, container, false);

        view.setFocusableInTouchMode(true);
        view.requestFocus();
        view.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_UP) {
                    //Toast.makeText(getContext(), "ba", Toast.LENGTH_LONG).show();
                    mainActivityCallback.backChooseGroup();
                    return true;
                }
                return false;
            }
        });

        timeTableModelList = getListOfExistTimetables();
        if (timeTableModelList == null){
            timeTableModelList = new ArrayList<>();
        }

        groupsMap = new TreeMap<>();
        groups = new ArrayList<>();
        teacherList = new ArrayList<>();
        groupSaveInstance = new ArrayList<>();

        unbinder = ButterKnife.bind(this, view);

        Retrofit.getNure(new Callback<Nure>() {
            @Override
            public void success(Nure nure, Response response) {

                University university = nure.university;

                for (Faculty faculty : university.getFaculties()) {
                    for (Direction direction : faculty.getDirections()) {
                        for (Speciality speciality : direction.getSpecialities()) {
                            for (Group group : speciality.getGroups()) {
                                groups.add(group.getName());
                                groupsMap.put(group.getName(), group.getId());
                            }
                        }
                        if (direction.getGroups() != null) {
                            for (Group group : direction.getGroups()) {
                                groups.add(group.getName());
                                groupsMap.put(group.getName(), group.getId());
                            }
                        }
                    }
                }

                Collections.sort(groups);
                groups.addAll(teacherList);
                groupSaveInstance.addAll(groups);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void failure(RetrofitError error) {

            }
        });
        Retrofit.getNureTeachers(new Callback<NureTeachers>() {
            @Override
            public void success(NureTeachers nureTeachers, Response response) {
                ltd.vblago.topdiary.model.NURE.Teachers.University university = nureTeachers.getUniversity();
                for (ltd.vblago.topdiary.model.NURE.Teachers.Faculty faculty : university.getFaculties()) {
                    for (Department department : faculty.getDepartments()) {
                        for (Teacher teacher : department.getTeachers()) {
                            teacherList.add(teacher.getFullName());
                            groupsMap.put(teacher.getFullName(), teacher.getId());
                        }
                    }
                }

                Collections.sort(teacherList);
                if (groups.size() != 0) {
                    groups.addAll(teacherList);
                    groupSaveInstance.addAll(teacherList);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                Toast.makeText(getContext(), "No Internet Connection\nCheck your device's connection to WiFi or mobile network ",
                        Toast.LENGTH_LONG).show();
            }
        });

        adapter = new ArrayAdapter<>(Objects.requireNonNull(getContext()), android.R.layout.simple_list_item_1, groups);
        listNure.setAdapter(adapter);
        listNure.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String name = groups.get(position);

                if (name.replaceAll("[^ ]", "").length() >= 2) {
                    getSchedule(name, groupsMap.get(groups.get(position)), 2);
                } else {
                    getSchedule(name, groupsMap.get(groups.get(position)), 1);
                }
            }
        });
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.groups_menu, menu);
    }

    private void getSchedule(final String name, final int id, final int type) {
        String PERSISTANT_STORAGE_NAME = "SharedPreferences";
        final SharedPreferences settings = Objects.requireNonNull(getContext()).getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE);

        Retrofit.getSchedule(id, type, new Callback<TimeTable>() {
            @Override
            public void success(TimeTable timeTable, Response response) {
                ArrayList<Entry> timeTableList = EventFormator.format(timeTable);
                WriteTask writeTask = new WriteTask(getContext(), "timetable-"+name, timeTableList);
                writeTask.execute();

                ltd.vblago.topdiary.model.TimeTable timeTableModel = new ltd.vblago.topdiary.model.TimeTable(name, id, type);
                timeTableModelList.add(timeTableModel);

                SharedPreferences.Editor editor = settings.edit();
                Calendar calendar = Calendar.getInstance();
                calendar.set(Calendar.MINUTE, 0);
                calendar.set(Calendar.HOUR_OF_DAY, 6);
                editor.putLong("last_sync", calendar.getTimeInMillis());
                editor.apply();

                WriteTask writeTaskSet = new WriteTask(getContext(), "timetable-model-list", timeTableModelList);
                writeTaskSet.execute();

                if (getContext() != null){
                    Toast.makeText(getContext(), "Successful add the group/teacher timetable", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void failure(RetrofitError error) {
                if (getContext() != null){
                    Toast.makeText(getContext(), "The timetable for your group isn't posted on the cist.nure.ua", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private ArrayList<ltd.vblago.topdiary.model.TimeTable> getListOfExistTimetables() {
        ReadTask readTask = new ReadTask(getContext(), "timetable-model-list");
        return (ArrayList<ltd.vblago.topdiary.model.TimeTable>) readTask.getObjectNotBackground();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.save:
                mainActivityCallback.backChooseGroup();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
