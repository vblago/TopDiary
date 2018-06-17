package ltd.vblago.topdiary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;

import java.util.ArrayList;
import java.util.Calendar;

import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.model.NURE.TimeTable.TimeTable;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class RefreshTimetables extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private SharedPreferences settings;

    public RefreshTimetables(Context context, SharedPreferences settings){
        this.context = context;
        this.settings = settings;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        ReadTask readTask = new ReadTask(context, "timetable-model-list");
        ArrayList<ltd.vblago.topdiary.model.TimeTable> timeTableModelList = (ArrayList<ltd.vblago.topdiary.model.TimeTable>) readTask.getObjectNotBackground();
        if (timeTableModelList == null){
            timeTableModelList = new ArrayList<>();
        }

        for (final ltd.vblago.topdiary.model.TimeTable timeTableModel : timeTableModelList) {
            Retrofit.getSchedule(timeTableModel.getId(), timeTableModel.getType(), new Callback<TimeTable>() {
                @Override
                public void success(TimeTable timeTable, Response response) {
                    ArrayList<Entry> timeTableList = EventFormator.format(timeTable);
                    WriteTask writeTask = new WriteTask(context, "timeTable-" + timeTableModel.getName(), timeTableList);
                    writeTask.execute();
                }

                @Override
                public void failure(RetrofitError error) {
                }
            });
        }

        SharedPreferences.Editor editor = settings.edit();
        Calendar calendar = Calendar.getInstance();
        editor.putLong("last_sync", calendar.getTimeInMillis());
        editor.apply();
        return null;
    }

}
