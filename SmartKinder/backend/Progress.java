package androidArmy.SmartKinder.backend;

public class Progress {
    private String kidName;
    private int teacherId;
    private String kidProgress;
    private String progressDate;

    public Progress(String kidName, int teacherId, String kidProgress, String progressDate) {
        this.kidName = kidName;
        this.teacherId = teacherId;
        this.kidProgress = kidProgress;
        this.progressDate = progressDate;
    }

    public String getKidName() {
        return kidName;
    }

    public int getTeacherId() {
        return teacherId;
    }

    public String getKidProgress() {
        return kidProgress;
    }

    public String getProgressDate() {
        return progressDate;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public void setKidProgress(String kidProgress) {
        this.kidProgress = kidProgress;
    }

    public void setProgressDate(String progressDate) {
        this.progressDate = progressDate;
    }
}
