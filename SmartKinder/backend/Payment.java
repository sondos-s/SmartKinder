package androidArmy.SmartKinder.backend;

public class Payment {
    private String kidName;
    private String year;
    private int firstPayment;
    private int secondPayment;
    private int thirdPayment;

    public Payment(String kidName, String year, int firstPayment, int secondPayment, int thirdPayment) {
        this.kidName = kidName;
        this.year = year;
        this.firstPayment = firstPayment;
        this.secondPayment = secondPayment;
        this.thirdPayment = thirdPayment;
    }

    public String getKidName() {
        return kidName;
    }

    public void setKidName(String kidName) {
        this.kidName = kidName;
    }

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public int getFirstPayment() {
        return firstPayment;
    }

    public void setFirstPayment(int firstPayment) {
        this.firstPayment = firstPayment;
    }

    public int getSecondPayment() {
        return secondPayment;
    }

    public void setSecondPayment(int secondPayment) {
        this.secondPayment = secondPayment;
    }

    public int getThirdPayment() {
        return thirdPayment;
    }

    public void setThirdPayment(int thirdPayment) {
        this.thirdPayment = thirdPayment;
    }
}
