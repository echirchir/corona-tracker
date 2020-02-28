package ke.co.simpledeveloper;

import android.app.Application;
import android.content.Context;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class CoronaTrackerApplication extends Application {

    public CoronaTrackerApplication() {
        super();
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration configuration =
                new RealmConfiguration.Builder()
                        .schemaVersion(1)
                        .deleteRealmIfMigrationNeeded()
                        .build();

        Realm.setDefaultConfiguration(configuration);

    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
