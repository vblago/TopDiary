package ltd.vblago.topdiary.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;
import java.util.Locale;

public class Entry implements Serializable {

    public int id;
    public boolean done;
    public String title;
    public String description;
    public String place;
    public long minStart;
    public long minEnd;
    public int duration;
    public int type;
    public boolean alert;

    public Entry(int id, boolean done, String title, String description, String place, long minStart, int duration, int type, boolean alert) {
        this.id = id;
        this.done = done;
        this.title = title;
        this.description = description;
        this.place = place;
        this.minStart = minStart;
        this.minEnd = minStart + duration;
        this.duration = duration;
        this.type = type;
        this.alert = alert;
    }

    protected Entry(Parcel in) {
        id = in.readInt();
        done = in.readByte() != 0;
        title = in.readString();
        description = in.readString();
        place = in.readString();
        minStart = in.readLong();
        minEnd = in.readLong();
        duration = in.readInt();
        type = in.readInt();
        alert = in.readByte() != 0;
    }

    private GregorianCalendar getCalendarStart() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(minStart * 1000 * 60);
        return calendar;
    }

    private GregorianCalendar getCalendarEnd() {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(minEnd * 1000 * 60);
        return calendar;
    }

    public String getTimeString() {
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm", Locale.GERMAN);
        StringBuilder sb = new StringBuilder();
        sb.append(timeFormat.format(getCalendarStart().getTime()));
        if (duration != 0) {
            sb.append(" - ");
            sb.append(timeFormat.format(getCalendarEnd().getTime()));
        }
        return sb.toString();
    }

    public String getDateString() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy", Locale.GERMAN);
        return sdf.format(getCalendarStart().getTime());
    }

}
