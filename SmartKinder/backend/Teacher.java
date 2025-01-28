package androidArmy.SmartKinder.backend;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Teacher implements Serializable, Parcelable {
    private int teacherId;
    private String firstName;
    private String lastName;

    // Constructor
    public Teacher(int teacherId, String firstName, String lastName) {
        this.teacherId = teacherId;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    // Getters and Setters
    public int getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(int teacherId) {
        this.teacherId = teacherId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    // Parcelable implementation
    protected Teacher(Parcel in) {
        teacherId = in.readInt();
        firstName = in.readString();
        lastName = in.readString();
    }

    public static final Creator<Teacher> CREATOR = new Creator<Teacher>() {
        @Override
        public Teacher createFromParcel(Parcel in) {
            return new Teacher(in);
        }

        @Override
        public Teacher[] newArray(int size) {
            return new Teacher[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(teacherId);
        dest.writeString(firstName);
        dest.writeString(lastName);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
