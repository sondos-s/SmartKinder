package androidArmy.SmartKinder.backend;

public class ChildAllergy {
    int Id;
    String childName;
    String allergyName;

    public ChildAllergy(int id, String allergyName) {
        Id = id;
        this.allergyName = allergyName;
    }

    public ChildAllergy(String childName, String allergyName) {
        this.childName = childName;
        this.allergyName = allergyName;
    }

    public int getId() {
        return Id;
    }

    public void setId(int id) {
        Id = id;
    }

    public String getChildName() {
        return childName;
    }

    public void setChildName(String childName) {
        this.childName = childName;
    }

    public String getAllergyName() {
        return allergyName;
    }

    public void setAllergyName(String allergyName) {
        this.allergyName = allergyName;
    }
}
