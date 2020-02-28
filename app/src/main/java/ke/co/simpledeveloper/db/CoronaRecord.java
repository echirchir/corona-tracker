package ke.co.simpledeveloper.db;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class CoronaRecord extends RealmObject {

    @PrimaryKey
    @Index
    private long id;

    private String province_state;
    private String country_region;
    private double latitude;
    private double longitude;
    private int confirmed_cases;
    private int previous_day_cases;
    private int confirmed_death;

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

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public int getConfirmed_cases() {
        return confirmed_cases;
    }

    public void setConfirmed_cases(int confirmed_cases) {
        this.confirmed_cases = confirmed_cases;
    }

    public int getPrevious_day_cases() {
        return previous_day_cases;
    }

    public void setPrevious_day_cases(int previous_day_cases) {
        this.previous_day_cases = previous_day_cases;
    }

    public int getConfirmed_death() {
        return confirmed_death;
    }

    public void setConfirmed_death(int confirmed_death) {
        this.confirmed_death = confirmed_death;
    }
}
