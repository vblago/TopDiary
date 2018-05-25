package ltd.vblago.topdiary.model.NURE;

import java.util.ArrayList;

public class Direction {
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

    private ArrayList<Speciality> specialities;

    public ArrayList<Speciality> getSpecialities() {
        return this.specialities;
    }

    public void setSpecialities(ArrayList<Speciality> specialities) {
        this.specialities = specialities;
    }

    private ArrayList<Group> groups;

    public ArrayList<Group> getGroups() {
        return this.groups;
    }

    public void setGroups(ArrayList<Group> groups) {
        this.groups = groups;
    }
}
