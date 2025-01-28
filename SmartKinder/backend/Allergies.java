package androidArmy.SmartKinder.backend;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Allergies implements Serializable, Parcelable {
    public String allergyName;

    public Allergies(String allergyName) {
        this.allergyName = allergyName;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }

    // Parcelable implementation
    protected Allergies(Parcel in) {
        allergyName = in.readString();
    }

    public static final Creator<Allergies> CREATOR = new Creator<Allergies>() {
        @Override
        public Allergies createFromParcel(Parcel in) {
            return new Allergies(in);
        }

        @Override
        public Allergies[] newArray(int size) {
            return new Allergies[size];
        }
    };

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(allergyName);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
