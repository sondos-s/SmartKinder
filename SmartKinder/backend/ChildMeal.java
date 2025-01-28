package androidArmy.SmartKinder.backend;

public class ChildMeal {
    private String kidName;
    private int mealId;
    private String mealDate;

    public ChildMeal() {
        // Default constructor
    }

    public ChildMeal(String kidName, int mealId, String mealDate) {
        this.kidName = kidName;
        this.mealId = mealId;
        this.mealDate = mealDate;
    }

    public String getKidName() {
        return kidName;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public int getMealId() {
        return mealId;
    }

    public void setMealId(int mealId) {
        this.mealId = mealId;
    }

    public String getMealDate() {
        return mealDate;
    }

    public void setMealDate(String mealDate) {
        this.mealDate = mealDate;
    }
}
