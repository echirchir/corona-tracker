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


}
