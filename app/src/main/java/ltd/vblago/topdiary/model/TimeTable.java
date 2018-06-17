package ltd.vblago.topdiary.model;

import java.io.Serializable;

public class TimeTable implements Serializable {
    private String name;
    private int id;
    private int type;

    public TimeTable(String name, int id, int type) {
        this.name = name;
        this.id = id;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public int getId() {
        return id;
    }

    public int getType() {
        return type;
    }
}
