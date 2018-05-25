package ltd.vblago.topdiary.model.NURE.TimeTable;

import java.io.Serializable;
import java.util.ArrayList;

public class Subject implements Serializable {
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String brief;

    public String getBrief() {
        return this.brief;
    }

    public void setBrief(String brief) {
        this.brief = brief;
    }

    private String title;

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    private ArrayList<Hour> hours;

    public ArrayList<Hour> getHours() {
        return this.hours;
    }

    public void setHours(ArrayList<Hour> hours) {
        this.hours = hours;
    }
}
