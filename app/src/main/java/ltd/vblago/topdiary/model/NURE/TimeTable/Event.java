package ltd.vblago.topdiary.model.NURE.TimeTable;

import java.io.Serializable;
import java.util.ArrayList;

public class Event implements Serializable{
    private int subject_id;

    public int getSubjectId() {
        return this.subject_id;
    }

    public void setSubjectId(int subject_id) {
        this.subject_id = subject_id;
    }

    private int start_time;

    public int getStartTime() {
        return this.start_time;
    }

    public void setStartTime(int start_time) {
        this.start_time = start_time;
    }

    private int end_time;

    public int getEndTime() {
        return this.end_time;
    }

    public void setEndTime(int end_time) {
        this.end_time = end_time;
    }

    private int type;

    public int getType() {
        return this.type;
    }

    public void setType(int type) {
        this.type = type;
    }

    private int number_pair;

    public int getNumberPair() {
        return this.number_pair;
    }

    public void setNumberPair(int number_pair) {
        this.number_pair = number_pair;
    }

    private String auditory;

    public String getAuditory() {
        return this.auditory;
    }

    public void setAuditory(String auditory) {
        this.auditory = auditory;
    }

    private ArrayList<Integer> teachers;

    public ArrayList<Integer> getTeachers() {
        return this.teachers;
    }

    public void setTeachers(ArrayList<Integer> teachers) {
        this.teachers = teachers;
    }

    private ArrayList<Integer> groups;

    public ArrayList<Integer> getGroups() {
        return this.groups;
    }

    public void setGroups(ArrayList<Integer> groups) {
        this.groups = groups;
    }
}
