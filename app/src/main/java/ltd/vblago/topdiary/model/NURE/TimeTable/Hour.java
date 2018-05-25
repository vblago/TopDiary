package ltd.vblago.topdiary.model.NURE.TimeTable;

import java.io.Serializable;
import java.util.ArrayList;

public class Hour implements Serializable {
    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int val;

    public int getVal() {
        return this.val;
    }

    public void setVal(int val) {
        this.val = val;
    }

    private ArrayList<Integer> teachers;

    public ArrayList<Integer> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(ArrayList<Integer> teachers) {
        this.teachers = teachers;
    }
}
