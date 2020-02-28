package ke.co.simpledeveloper.adapters;

import androidx.annotation.NonNull;

public class RecordObject {

    private long id;

    private String province_state;

    private String country_region;

    private int total_cases;

    private String date;

    private String description;

    public RecordObject(){}

    public RecordObject(long id, String province_state, String country_region, int total_cases, String date, String description) {
        this.id = id;
        this.province_state = province_state;
        this.country_region = country_region;
        this.total_cases = total_cases;
        this.date = date;
        this.description = description;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProvince_state() {
        return province_state;
    }

    public void setProvince_state(String province_state) {
        this.province_state = province_state;
    }

    public String getCountry_region() {
        return country_region;
    }

    public void setCountry_region(String country_region) {
        this.country_region = country_region;
    }

    public int getTotal_cases() {
        return total_cases;
    }

    public void setTotal_cases(int total_cases) {
        this.total_cases = total_cases;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return super.toString();
    }
}
