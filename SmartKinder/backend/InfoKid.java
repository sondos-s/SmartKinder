package androidArmy.SmartKinder.backend;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class InfoKid implements Serializable, Parcelable {
    private int id;
    public String Name;
    private String motherId;
    private String fatherId;
    private Date birthdate;

    private String needs;
    //private Bitmap image1 = null;

    public InfoKid() {
    }

    public InfoKid(int id, String name, String motherId, String fatherId, Date birthdate, String needs) {
        this.id = id;
        Name = name;
        this.motherId = motherId;
        this.fatherId = fatherId;
        this.birthdate = birthdate;
        this.needs = needs;
    }

    public String getNeeds() {
        return needs;
    }

    public void setNeeds(String needs) {
        this.needs = needs;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getMotherId() {
        return motherId;
    }

    public void setMotherId(String motherId) {
        this.motherId = motherId;
    }

    public String getFatherId() {
        return fatherId;
    }

    public void setFatherId(String fatherId) {
        this.fatherId = fatherId;
    }

    public Date getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(Date birthdate) {
        this.birthdate = birthdate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(Name);
        dest.writeString(motherId);
        dest.writeString(fatherId);
        dest.writeLong(birthdate != null ? birthdate.getTime() : -1);
        dest.writeString(needs);
    }

    public static final Creator<InfoKid> CREATOR = new Creator<InfoKid>() {
        @Override
        public InfoKid createFromParcel(Parcel in) {
            return new InfoKid(in);
        }

        @Override
        public InfoKid[] newArray(int size) {
            return new InfoKid[size];
        }
    };

    protected InfoKid(Parcel in) {
        id = in.readInt();
        Name = in.readString();
        motherId = in.readString();
        fatherId = in.readString();
        long birthdateMillis = in.readLong();
        birthdate = birthdateMillis != -1 ? new Date(birthdateMillis) : null;
        needs = in.readString();
    }
}
