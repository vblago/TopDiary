package ltd.vblago.topdiary.model.NURE.TimeTable;

import java.io.Serializable;

public class Teacher implements Serializable {
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String full_name;

    public String getFullName() {
        return this.full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    private String short_name;

    public String getShortName() {
        return this.short_name;
    }

    public void setShortName(String short_name) {
        this.short_name = short_name;
    }
}
