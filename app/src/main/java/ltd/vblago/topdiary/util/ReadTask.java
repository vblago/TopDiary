package ltd.vblago.topdiary.util;

import android.content.Context;
import android.os.AsyncTask;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.util.ArrayList;

import ltd.vblago.topdiary.model.Entry;

public class ReadTask extends AsyncTask<Void, Void, Object> {

    private Context context;
    private String fileName;
    private ReadCallback readCallback;

    public ReadTask(Context context, String fileName) {
        this.context = context;
        this.fileName = fileName;
    }

    public boolean fileExist(){
        File file = context.getFileStreamPath(fileName);
        return file.exists();
    }

    @Override
    protected Object doInBackground(Void... voids) {
        FileInputStream fis = null; //open stream for reading data from file_name file
        Object object = null;
        try {
            fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return object;
    }

    public ArrayList<Entry> getListNotBackground(){
        FileInputStream fis = null; //open stream for reading data from file_name file
        Object object = null;
        try {
            fis = context.openFileInput(fileName);
            ObjectInputStream ois = new ObjectInputStream(fis);
            object = ois.readObject();

        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return (ArrayList<Entry>) object;
    }

    @Override
    protected void onPostExecute(Object object) {
        super.onPostExecute(object);
        readCallback.saveObject(object);
    }

    public void setReadCallback(ReadCallback readCallback){
         this.readCallback = readCallback;
    }

    public interface ReadCallback{
        void saveObject(Object object);
    }
}

