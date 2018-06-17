package ltd.vblago.topdiary.model;

import android.content.Context;

import java.util.ArrayList;

import ltd.vblago.topdiary.util.ReadTask;
import ltd.vblago.topdiary.util.WriteTask;

public class FullList {
    public ArrayList<Entry> fullList;
    private Context context;

    public FullList(Context context) {
        this.context = context;
        fullList = getFullItemList(context);
    }

    public void addItem(Entry entry){
        fullList.add(entry);
        fullList.get(fullList.indexOf(entry)).id = fullList.indexOf(entry);
    }

    public void removeItem(int id){
        for (int i = 0; i < fullList.size(); i++){
            Entry entry = fullList.get(i);
            if (entry.id == id){
                fullList.remove(i);
                updateFullList();
                return;
            }
        }
    }

    public void setItem(int id, Entry entry){
        fullList.set(id, entry);
        updateFullList();
    }

    public void save(ArrayList<Entry> list){
        for (Entry entry:list) {
            if (entry.type == 5){
                continue;
            }
            boolean find = false;
            int id = entry.id;
            if (id != -1){
                for (int i = 0; i<fullList.size(); i++){
                    if (fullList.get(i).id == id){
                        fullList.set(i, entry);
                        find = true;
                        break;
                    }
                }
            }
            if (!find){
                addItem(entry);
            }
        }
        updateFullList();
    }

    private void updateFullList(){
        WriteTask writeTask = new WriteTask(context, "fullList", fullList);
        writeTask.execute();
    }

    private static ArrayList<Entry> getFullItemList(Context context){
        ReadTask readTask = new ReadTask(context, "fullList");
        if (readTask.fileExist()) {
            ArrayList<Entry> list = readTask.getListNotBackground();
            if (list != null){
                return list;
            }
        }
        return new ArrayList<>();
    }
}
