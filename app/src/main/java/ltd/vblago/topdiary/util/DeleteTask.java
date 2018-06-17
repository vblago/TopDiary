package ltd.vblago.topdiary.util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;

public class DeleteTask extends AsyncTask<Void, Void, Void> {

    @SuppressLint("StaticFieldLeak")
    private Context context;
    private String fileName;

    public DeleteTask(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            context.deleteFile("timetable-" + fileName);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

