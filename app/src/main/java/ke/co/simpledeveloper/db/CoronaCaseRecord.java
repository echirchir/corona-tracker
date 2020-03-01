package ke.co.simpledeveloper.db;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class CoronaCaseRecord extends RealmObject {

    @PrimaryKey
    @Index
    private long id;

    private String province_state;
    private String country_region;
    private int confirmed_cases;
    private int confirmed_deaths;
    private int confirmed_recovered;
    private String last_update;

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

    public int getConfirmed_cases() {
        return confirmed_cases;
    }

    public void setConfirmed_cases(int confirmed_cases) {
        this.confirmed_cases = confirmed_cases;
    }

    public int getConfirmed_deaths() {
        return confirmed_deaths;
    }

    public void setConfirmed_deaths(int confirmed_deaths) {
        this.confirmed_deaths = confirmed_deaths;
    }

    public int getConfirmed_recovered() {
        return confirmed_recovered;
    }

    public void setConfirmed_recovered(int confirmed_recovered) {
        this.confirmed_recovered = confirmed_recovered;
    }

    public String getLast_update() {
        return last_update;
    }

    public void setLast_update(String last_update) {
        this.last_update = last_update;
    }
}
