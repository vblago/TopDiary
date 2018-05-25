package ltd.vblago.topdiary.util;


import android.content.Context;
import android.os.AsyncTask;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class WriteTask extends AsyncTask<Void, Void, String> {

    private Context context;
    private String fileName;
    private Object object;

    public WriteTask(Context context, String fileName, Object object) {
        this.context = context;
        this.fileName = fileName;
        this.object= object;
    }

    @Override
    protected String doInBackground(Void... voids) {
        //write to file
        FileOutputStream fos = null; //open stream for writing data to file_name file
        try {
            fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
            ObjectOutputStream oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
            oos.close();
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
}
