package androidArmy.SmartKinder.backend;

import java.util.Date;

public class Attendance {
    private Date date;
    private String childId;
    private boolean attendance;

    public Attendance(Date date, String childId, boolean attendance) {
        this.date = date;
        this.childId = childId;
        this.attendance = attendance;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getChildId() {
        return childId;
    }

    public void setChildId(String childId) {
        this.childId = childId;
    }

    public boolean isAttendance() {
        return attendance;
    }

    public void setAttendance(boolean attendance) {
        this.attendance = attendance;
    }
}
