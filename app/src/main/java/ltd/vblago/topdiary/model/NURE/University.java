package ltd.vblago.topdiary.model.NURE;

import java.util.ArrayList;

public class University {
    public String short_name;

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

    private ArrayList<Faculty> faculties;

    public ArrayList<Faculty> getFaculties() {
        return this.faculties;
    }

    public void setFaculties(ArrayList<Faculty> faculties) {
        this.faculties = faculties;
    }
}
