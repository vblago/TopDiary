package ltd.vblago.topdiary.fragment;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.NumberPicker;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import ltd.vblago.topdiary.R;
import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.util.MainActivityCallback;

public class EditFragment extends Fragment {

    Entry entry;
    int position;
    private int duration;
    protected Calendar calendar;
    MainActivityCallback mainActivityCallback;
    Unbinder unbinder;

    @BindView(R.id.titleEdit)
    TextView titleEdit;
    @BindView(R.id.descriptionEdit)
    TextView descriptionEdit;
    @BindView(R.id.placeEdit)
    TextView placeEdit;
    @BindView(R.id.switchDuration)
    Switch switchDuration;
    @BindView(R.id.switchTime)
    Switch switchTime;
    @BindView(R.id.switchAlert)
    Switch switchAlert;
    @BindView(R.id.durationEdit)
    TextView durationEdit;
    @BindView(R.id.dateEdit)
    TextView dateEdit;
    @BindView(R.id.timeEdit)
    TextView timeEdit;

    public EditFragment() {
        // Required empty public constructor
    }

    public static EditFragment newInstance(Entry entry, int position) {
        EditFragment fragment = new EditFragment();
        Bundle args = new Bundle();
        args.putSerializable("entry", entry);
        args.putInt("position", position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        if (getArguments() != null) {
            this.entry = (Entry) getArguments().getSerializable("entry");
            this.position = getArguments().getInt("position");
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        inflater.inflate(R.menu.add_entry_menu, menu);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.cancel:
                mainActivityCallback.cancelAddEntry();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @OnClick(R.id.successAddEntryBtn)
    public void onClickButtonAdd() {
        if (titleEdit.getText().toString().trim().length() == 0) {
            Toast.makeText(getContext(), "To save entry, enter the title, please", Toast.LENGTH_LONG).show();
            return;
        }
        String title = titleEdit.getText().toString();
        String place = placeEdit.getText().toString();
        String description = descriptionEdit.getText().toString();
        boolean alert = switchAlert.isChecked();
        long timeInMin = calendar.getTimeInMillis() / 1000 / 60;
        int type = 2;
        if (switchDuration.isChecked() && switchTime.isChecked()) {
            type = 3;
        } else if (switchTime.isChecked()) {
            type = 4;
        }
        if (duration == 0) {
            duration = entry.duration;
        }
        Entry entryEdit = new Entry(entry.id, false, title, description, place, timeInMin, duration, type, alert);
        if (position == -1){
            mainActivityCallback.successEditEntry(entryEdit, calendar);
        }else {
            mainActivityCallback.successEditEntry(entryEdit, position);
        }
    }

    @OnClick(R.id.switchDuration)
    public void onSwitchDuration(View v) {
        if (switchDuration.isChecked()) {
            durationEdit.setTextColor(Color.BLACK);
            if (!switchTime.isChecked()) {
                switchTime.setChecked(true);
                onSwitchTime(getView());
            }

        } else {
            durationEdit.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.secondary_text));
        }
    }

    @OnClick(R.id.switchTime)
    public void onSwitchTime(View v) {
        if (switchTime.isChecked()) {
            timeEdit.setTextColor(Color.BLACK);
        } else {
            timeEdit.setTextColor(ContextCompat.getColor(Objects.requireNonNull(getContext()), R.color.secondary_text));
            if (switchDuration.isChecked()) {
                switchDuration.setChecked(false);
                onSwitchDuration(getView());
            }
        }
    }

    @OnClick(R.id.durationEdit)
    public void durationEditClick(View v) {
        if (switchDuration.isChecked()) {
            show();
        }
    }

    @OnClick(R.id.dateEdit)
    public void dateSelect(View v) {
        dataPicker();
    }

    @OnClick(R.id.timeEdit)
    public void timeSelect(View v) {
        timePicker();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add, container, false);
        unbinder = ButterKnife.bind(this, view);

        restoreData();
        return view;
    }

    private void restoreData() {
        titleEdit.setText(entry.title);
        switchAlert.setChecked(entry.alert);
        if (placeEdit != null) {
            placeEdit.setText(entry.place);
        }
        calendar = new GregorianCalendar();
        calendar.setTimeInMillis(entry.minStart * 1000 * 60);
        setDate();

        switch (entry.type) {
            case 2:
                switchTime.setChecked(false);
                switchDuration.setChecked(false);
                break;
            case 3:
                switchTime.setChecked(true);
                if (entry.duration != 0) {
                    switchDuration.setChecked(true);
                } else {
                    switchDuration.setChecked(false);
                }
                break;
            default:

                break;
        }
        setTime();
        if (entry.description != null) {
            descriptionEdit.setText(entry.description);
        }
        StringBuilder sb = new StringBuilder();
        if (entry.duration > 0 && entry.duration < 60) {
            sb.append(entry.duration);
            sb.append(" m.");
            durationEdit.setText(sb.toString());
        } else if (entry.duration >= 60) {
            sb.append(entry.duration / 60);
            sb.append(" h. ");
            sb.append(entry.duration % 60);
            sb.append(" m.");
            durationEdit.setText(sb.toString());
        }
    }

    private void setTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        timeEdit.setText(sdf.format(calendar.getTime()));
    }

    private void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        dateEdit.setText(sdf.format(calendar.getTime()));
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unbinder.unbind();
    }

    private void dataPicker() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                getContext(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, month);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                setDate();
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    private void timePicker() {
        if (!switchTime.isChecked()){
            return;
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(
                getContext(), new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
                calendar.set(Calendar.MINUTE, minute);
                setTime();
            }
        }, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), true);
        timePickerDialog.show();
    }

    private void show() {

        final Dialog d = new Dialog(getContext());
        d.setTitle("Duration Picker");
        d.setContentView(R.layout.dialog_duration);
        Button b1 = d.findViewById(R.id.setDurationBtn);
        Button b2 = d.findViewById(R.id.cancelDurationBtn);
        final NumberPicker hoursPicker = d.findViewById(R.id.hoursPicker);
        final NumberPicker minPicker = d.findViewById(R.id.minPicker);

        hoursPicker.setMaxValue(23);
        hoursPicker.setMinValue(0);
        minPicker.setMaxValue(59);
        minPicker.setMinValue(1);
        hoursPicker.setWrapSelectorWheel(false);
        minPicker.setWrapSelectorWheel(false);
        if (entry.duration == 0) {
            minPicker.setValue(10);
        } else {
            hoursPicker.setValue(entry.duration / 60);
            minPicker.setValue(entry.duration % 60);
        }
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder();
                if (hoursPicker.getValue() > 0) {
                    sb.append(hoursPicker.getValue());
                    sb.append(" h. ");
                    sb.append(minPicker.getValue());
                    sb.append(" m.");
                    durationEdit.setText(sb.toString());
                    duration = hoursPicker.getValue() * 60 + minPicker.getValue();
                } else {
                    sb.append(minPicker.getValue());
                    sb.append(" m.");
                    durationEdit.setText(sb.toString());
                    duration = minPicker.getValue();
                }
                d.dismiss();
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                d.dismiss(); // dismiss the dialog
            }
        });
        d.show();
    }
}
