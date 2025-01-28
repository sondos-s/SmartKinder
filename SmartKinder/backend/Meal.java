package androidArmy.SmartKinder.backend;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Meal implements Serializable, Parcelable {
    private int id;
    private String mealName;
    private String type;

    public Meal() {
        // Default constructor
    }

    public Meal(int id, String mealName, String type) {
        this.id = id;
        this.mealName = mealName;
        this.type = type;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMealName() {
        return mealName;
    }

    public void setMealName(String mealName) {
        this.mealName = mealName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Meal{" +
                "id=" + id +
                ", mealName='" + mealName + '\'' +
                ", type='" + type + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(mealName);
        dest.writeString(type);
    }

    public static final Parcelable.Creator<Meal> CREATOR = new Parcelable.Creator<Meal>() {
        @Override
        public Meal createFromParcel(Parcel source) {
            return new Meal(source);
        }

        @Override
        public Meal[] newArray(int size) {
            return new Meal[size];
        }
    };

    private Meal(Parcel in) {
        id = in.readInt();
        mealName = in.readString();
        type = in.readString();
        // Read other relevant data members from the Parcel
    }

}
