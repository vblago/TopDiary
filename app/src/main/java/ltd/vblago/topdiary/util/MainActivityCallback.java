package ltd.vblago.topdiary.util;

import java.util.Calendar;
import java.util.List;

import ltd.vblago.topdiary.model.Entry;

public interface MainActivityCallback {
    void addEntry();
    void cancelAddEntry();
    void successAddEntry(Entry entry);
    void removeItem(int position);
    void checkEntry(int position, boolean checked);
    void editItem(int position);
    void successEditEntry(Entry entry, int position);
    void chooseGroup();
    void backChooseGroup();
    void openDescription(int position);
    void openDescription(Entry entry);
    void editItem(Entry entry);
    void removeItem(Entry entry);
    void successEditEntry(Entry entry, Calendar calendar);
    void openHistoryFragment(Calendar calendar);
    void setActivityTitle(String title);
}
