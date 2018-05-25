package ltd.vblago.topdiary.model.NURE.TimeTable;

import java.io.Serializable;
import java.util.ArrayList;

public class TimeTable implements Serializable {

    private ArrayList<Event> events;

    public ArrayList<Event> getEvents() { return this.events; }

    public void setEvents(ArrayList<Event> events) { this.events = events; }

    private ArrayList<Group> groups;

    public ArrayList<Group> getGroups() { return this.groups; }

    public void setGroups(ArrayList<Group> groups) { this.groups = groups; }

    private ArrayList<Teacher> teachers;

    public ArrayList<Teacher> getTeachers() { return this.teachers; }

    public void setTeachers(ArrayList<Teacher> teachers) { this.teachers = teachers; }

    private ArrayList<Subject> subjects;

    public ArrayList<Subject> getSubjects() { return this.subjects; }

    public void setSubjects(ArrayList<Subject> subjects) { this.subjects = subjects; }

    private ArrayList<Type> types;

    public ArrayList<Type> getTypes() { return this.types; }

    public void setTypes(ArrayList<Type> types) { this.types = types; }
}
