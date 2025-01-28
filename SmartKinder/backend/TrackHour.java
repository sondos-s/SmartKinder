package androidArmy.SmartKinder.backend;

import java.io.Serializable;
import java.sql.Time;
import java.util.Date;

public class TrackHour implements Serializable {
    private int teacherId;
    private Date date;
    private Time startTime;
    private Time endTime;


    // Constructor
    public TrackHour(int teacherId, Date date,Time startTime, Time endTime) {
        this.teacherId = teacherId;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    // Getters and Setters

    public Date getDate() {
        return date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "TrackHour{" +
                "teacherId=" + teacherId +
                ", date=" + date +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }
}
