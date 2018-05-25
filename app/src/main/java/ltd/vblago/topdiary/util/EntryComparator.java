package ltd.vblago.topdiary.util;

import java.util.Comparator;

import ltd.vblago.topdiary.model.Entry;

public class EntryComparator implements Comparator<Entry> {
    @Override
    public int compare(Entry entry1, Entry entry2) {
        int type1 = entry1.type;
        int type2 = entry2.type;

        if (type1 == 4 || type1 == 5){
            type1 = 3;
        }
        if (type2 == 4 || type2 == 5){
            type2 = 3;
        }
        if (type2 > type1){
            return -1;
        }
        if (type1 == type2){
            if (entry2.minStart > entry1.minStart){
                return -1;
            }
        }
        return 1;
    }
}
