package ltd.vblago.topdiary.activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Objects;

import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.fragment.AddFragment;
import ltd.vblago.topdiary.fragment.AddGroupFragment;
import ltd.vblago.topdiary.fragment.ChooseGroupFragment;
import ltd.vblago.topdiary.fragment.DescriptionFragment;
import ltd.vblago.topdiary.fragment.EditFragment;
import ltd.vblago.topdiary.fragment.HistoryFragment;
import ltd.vblago.topdiary.fragment.InfoFragment;
import ltd.vblago.topdiary.fragment.MainFragment;
import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.model.FullList;
import ltd.vblago.topdiary.util.EntryComparator;
import ltd.vblago.topdiary.util.ForegroundService;
import ltd.vblago.topdiary.util.MainActivityCallback;
import ltd.vblago.topdiary.util.ReadTask;
import ltd.vblago.topdiary.util.RefreshTimetables;

public class MainActivity extends AppCompatActivity implements MainActivityCallback {

    private static long startPos;
    private static long endPos;
    public ArrayList<Entry> list;
    public ArrayList<Entry> timeTableList;
    public FullList fullList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshSchedule();
        addDayTimeFrame();
        list = new ArrayList<>();
        fullList = new FullList(this);
        getTimeTableItemList();
        finderEntryList();
        runMainFragment();
    }

    public void runMainFragment() {
        setActivityTitle("TopDiary");
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, MainFragment.newInstance(list))
                .commit();
        startService(new View(this));
    }

    private void getTimeTableItemList() {
        timeTableList = new ArrayList<>();
        ReadTask readTaskSet = new ReadTask(this, "timetable-model-list");
        ArrayList<ltd.vblago.topdiary.model.TimeTable> timeTableModelList = (ArrayList<ltd.vblago.topdiary.model.TimeTable>) readTaskSet.getObjectNotBackground();
        if (timeTableModelList == null) {
            timeTableList = new ArrayList<>();
        }
        for (ltd.vblago.topdiary.model.TimeTable timeTableModel : Objects.requireNonNull(timeTableModelList)) {
            ReadTask readTask = new ReadTask(this, "timetable-" + timeTableModel.getName());
            if (readTask.fileExist()) {
                ArrayList<Entry> list = readTask.getListNotBackground();
                if (list != null) {
                    timeTableList.addAll(list);
                }
            }
        }

    }

    private void finderEntryList() {
        list = new ArrayList<>();
        for (Entry entry : timeTableList) {
            long startTime = entry.minStart;
            if (startTime >= startPos && startTime <= endPos) {
                list.add(entry);
            }
        }

        for (Entry entry : fullList.fullList) {
            long startTime = entry.minStart;
            if (startTime >= startPos && startTime <= endPos) {
                list.add(entry);
            }
            if (startTime < startPos && !entry.done) {
                entry.type = 1;
                list.add(entry);
            }
        }
        list = filerDuplicatesAndSort(list);
    }

    public ArrayList<Entry> filerDuplicatesAndSort(ArrayList<Entry> list){
        Comparator<Entry> comparator = new EntryComparator();
        Collections.sort(list, comparator);
        ArrayList<Entry> filterList = new ArrayList<>();
        for (int i = 0; i < list.size() - 1; i++) {
            boolean add = true;
            for (int q = i + 1; q < list.size() - 1; q++){
                if (list.get(i).minStart != list.get(q).minStart){
                    break;
                }
                if (list.get(i).equals(list.get(q))) {
                    add = false;
                    break;
                }
            }
            if (add) {
                filterList.add(list.get(i));
            }
        }
        if (list.size() != 0) {
            filterList.add(list.get(list.size() - 1));
        }
        return filterList;
    }

    public void startService(View v) {

        Intent serviceIntent = new Intent(this, ForegroundService.class);
        serviceIntent.putExtra("list", list);

        ContextCompat.startForegroundService(this, serviceIntent);
    }

    public void stopService(View v) {
        Intent serviceIntent = new Intent(this, ForegroundService.class);
        stopService(serviceIntent);
    }

    @Override
    public void refreshMainFragment() {
        list = new ArrayList<>();
        fullList = new FullList(this);
        refreshSchedule();
        getTimeTableItemList();
        finderEntryList();
        runMainFragment();
    }

    @Override
    public void addEntry() {
        AddFragment addFragment = new AddFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_main, addFragment, "add_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void cancelAddEntry() {
        getSupportFragmentManager()
                .popBackStack();
    }

    @Override
    public void successAddEntry(Entry entry) {
        getSupportFragmentManager().popBackStack();
        addToEntryList(entry);
        runMainFragment();
    }

    @Override
    public void removeItem(int position) {
        fullList.removeItem(list.get(position).id);
        list.remove(position);
        runMainFragment();
    }

    @Override
    public void checkEntry(int position, boolean checked) {
        list.get(position).done = checked;
    }

    @Override
    public void editItem(int position) {
        EditFragment editFragment = EditFragment.newInstance(list.get(position), position);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, editFragment, "edit_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void successEditEntry(Entry entry, int position) {
        if (entry.minStart < startPos) {
            entry.type = 1;
            list.set(position, entry);
        } else if (entry.minStart >= startPos && entry.minStart <= endPos) {
            list.set(position, entry);
        } else {
            fullList.removeItem(list.get(position).id);
            list.remove(position);
            fullList.addItem(entry);
        }
        getSupportFragmentManager().popBackStack();
        fullList.save(list);
        runMainFragment();
    }

    @Override
    public void chooseGroup() {
        ChooseGroupFragment chooseGroupFragment = new ChooseGroupFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_main, chooseGroupFragment, "choose_group_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void backChooseGroup() {
        list = new ArrayList<>();
        fullList = new FullList(this);
        getSupportFragmentManager().popBackStack();
        getTimeTableItemList();
        finderEntryList();
    }

    @Override
    public void openDescription(int position) {
        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(list.get(position));

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.activity_main, descriptionFragment, "description_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openDescription(Entry entry) {
        DescriptionFragment descriptionFragment = DescriptionFragment.newInstance(entry);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE)
                .replace(R.id.activity_main, descriptionFragment, "description_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void editItem(Entry entry) {
        EditFragment editFragment = EditFragment.newInstance(entry, -1);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.activity_main, editFragment, "edit_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void removeItem(Entry entry) {
        fullList.removeItem(entry.id);
        list.remove(entry);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(entry.minStart * 1000 * 60);
        openHistoryFragment(calendar);
    }

    @Override
    public void successEditEntry(Entry entry, Calendar calendar) {
        fullList.setItem(entry.id, entry);
        getSupportFragmentManager().popBackStack();
        getSupportFragmentManager().popBackStack();
        long[] range = addHistoryTimeFrame(calendar);
        if (range[0] != startPos) {
            openHistoryFragment(calendar);
        } else {
            backChooseGroup();
        }

    }

    @Override
    public void openHistoryFragment(Calendar calendar) {
        ArrayList<Entry> list = new ArrayList<>();
        long[] range = addHistoryTimeFrame(calendar);
        for (Entry en : fullList.fullList) {
            if (range[0] <= en.minStart && en.minStart <= range[1]) {
                list.add(en);
            }
        }
        for (Entry en : timeTableList) {
            if (range[0] <= en.minStart && en.minStart <= range[1]) {
                list.add(en);
            }
        }

        list = filerDuplicatesAndSort(list);

        HistoryFragment historyFragment = HistoryFragment.newInstance(list, calendar);

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_main, historyFragment, "edit_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void setActivityTitle(String title) {
        setTitle(title);
    }

    @Override
    public void openAddGroupFragment() {
        AddGroupFragment addGroupFragment = new AddGroupFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_main, addGroupFragment, "add_group_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void openInfoFragment() {
        InfoFragment infoFragment = new InfoFragment();

        getSupportFragmentManager()
                .beginTransaction()
                .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN)
                .replace(R.id.activity_main, infoFragment, "info_fragment")
                .addToBackStack(null)
                .commit();
    }

    @Override
    public void chooseGroupBack() {
        getSupportFragmentManager().popBackStack();
        getTimeTableItemList();
        finderEntryList();
        runMainFragment();
    }

    private static void addDayTimeFrame() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        startPos = calendar.getTimeInMillis() / 1000L / 60L;
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        endPos = calendar.getTimeInMillis() / 1000L / 60L;
    }

    private static long[] addHistoryTimeFrame(Calendar calendar) {
        long[] range = new long[2];
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        range[0] = calendar.getTimeInMillis() / 1000L / 60L;
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        range[1] = calendar.getTimeInMillis() / 1000L / 60L;
        return range;
    }

    private void refreshSchedule() {
        String PERSISTANT_STORAGE_NAME = "SharedPreferences";
        final SharedPreferences settings = getSharedPreferences(PERSISTANT_STORAGE_NAME, Context.MODE_PRIVATE);

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 6);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        final long mill = settings.getLong("last_sync", calendar.getTimeInMillis() - 1);

        if (mill < calendar.getTimeInMillis()) {
            RefreshTimetables refresh = new RefreshTimetables(this, settings);
            refresh.execute();
        }
    }

    public void addToEntryList(Entry entry) {
        long startTime = entry.minStart;

        if (startTime >= startPos && startTime <= endPos) {
            list.add(entry);
        } else if (startTime < startPos && !entry.done) {
            entry.type = 1;
            list.add(entry);
        } else {
            fullList.addItem(entry);
        }
        fullList.save(list);
    }

    @Override
    protected void onPause() {
        super.onPause();
        fullList.save(list);
    }
}
