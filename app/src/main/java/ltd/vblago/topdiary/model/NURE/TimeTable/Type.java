package ltd.vblago.topdiary.model.NURE.TimeTable;

import java.io.Serializable;

public class Type implements Serializable {
    private int id;

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String short_name;

    public String getShortName() {
        return this.short_name;
    }

    public void setShortName(String short_name) {
        this.short_name = short_name;
    }

    private String full_name;

    public String getFullName() {
        return this.full_name;
    }

    public void setFullName(String full_name) {
        this.full_name = full_name;
    }

    private int id_base;

    public int getIdBase() {
        return this.id_base;
    }

    public void setIdBase(int id_base) {
        this.id_base = id_base;
    }

    private String type;

    public String getType() {
        return this.type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
