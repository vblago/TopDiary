package ltd.vblago.topdiary.util;

import java.util.ArrayList;
import java.util.Collections;

import ltd.vblago.topdiary.model.Entry;
import ltd.vblago.topdiary.model.NURE.TimeTable.Event;
import ltd.vblago.topdiary.model.NURE.TimeTable.Group;
import ltd.vblago.topdiary.model.NURE.TimeTable.Subject;
import ltd.vblago.topdiary.model.NURE.TimeTable.Teacher;
import ltd.vblago.topdiary.model.NURE.TimeTable.TimeTable;
import ltd.vblago.topdiary.model.NURE.TimeTable.Type;

public class EventFormator {
    public static ArrayList<Entry> format(TimeTable timeTable){
        ArrayList<Entry> list = new ArrayList<>();
        ArrayList<Event> events = timeTable.getEvents();

        for (Event event:events) {
            String brief = "", full = "", typeBrief = "", typeFull = "", audirory = "";
            ArrayList<String> teachers = new ArrayList<>();
            ArrayList<String> groups = new ArrayList<>();
            int minStart, numPair;

            for (Subject subject:timeTable.getSubjects()){
                if (subject.getId() == event.getSubjectId()){
                    brief = subject.getBrief();
                    full = subject.getTitle();
                    break;
                }
            }
            for (Type type:timeTable.getTypes()){
                if (type.getId() == event.getType()){
                    typeBrief = type.getShortName();
                    typeFull = type.getFullName();
                    break;
                }
            }
            audirory = event.getAuditory();
            for (int idTeacher:event.getTeachers()){
                for (Teacher teacher:timeTable.getTeachers()){
                    if (teacher.getId() == idTeacher){
                        teachers.add(teacher.getFullName());
                    }
                }
            }
            for (int idGroup:event.getGroups()){
                for (Group group:timeTable.getGroups()){
                    if (group.getId() == idGroup){
                        groups.add(group.getName());
                    }
                }
            }
            minStart = event.getStartTime()/60;
            numPair = event.getNumberPair();

            String title = brief + " - " + typeBrief + " - " + numPair + " pair";
            StringBuilder sBuilder = new StringBuilder();
            sBuilder.append(numPair).append(" pair").append("\n\n");
            sBuilder.append(full).append("\n\n");
            sBuilder.append(typeFull).append("\n\n");

            for (int i = 0; i < teachers.size(); i++){
                if (i != teachers.size()-1){
                    sBuilder.append(teachers.get(i)).append(", ");
                    continue;
                }
                sBuilder.append(teachers.get(i)).append("\n\n");
            }
            Collections.sort(groups);
            for (int i = 0; i < groups.size(); i++){
                if (i != groups.size()-1){
                    sBuilder.append(groups.get(i)).append(", ");
                    continue;
                }
                sBuilder.append(groups.get(i)).append("\n\n");
            }

            Entry entry = new Entry(-1, true, title, sBuilder.toString(), audirory, minStart, 90, 5, true);
            list.add(entry);
        }

        return list;
    }
}
