package ke.co.simpledeveloper.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.io.StringReader;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ke.co.simpledeveloper.constants.URLS;
import ke.co.simpledeveloper.db.CoronaRecord;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class CoronaVirusService extends Service {
    public CoronaVirusService() {
    }

    public boolean isRunning = false;

    private NetworkResolver detector;

    private volatile HandlerThread mHandlerThread;
    private ServiceHandler mServiceHandler;

    @Override
    public void onCreate() {

        super.onCreate();

        detector = new NetworkResolver(getApplicationContext());

        mHandlerThread = new HandlerThread("CoronaVirusService.HandlerThread");
        mHandlerThread.start();
        mServiceHandler = new ServiceHandler(mHandlerThread.getLooper());

        isRunning = true;
    }

    private final class ServiceHandler extends Handler {

        public ServiceHandler(Looper looper) {
            super(looper);
        }

        @Override
        public void handleMessage(Message message) {

        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (!isRunning) {
            mHandlerThread.quit();
        }
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        mServiceHandler.post(new Runnable() {

            //check for updates every 12 hours;
            static final long DELAY = 43200000;

            @Override
            public void run() {
                while (isRunning) {

                    if (detector.isConnected()) {

                        try {

                            checkVirusUpdates();

                            Thread.sleep(DELAY);

                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    } else {
                        isRunning = false;
                        CoronaVirusService.this.stopSelf();
                        Toast.makeText(CoronaVirusService.this, "Turn ON your 4G data bundles to connect!", Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        return START_STICKY;
    }

    /* Query for corona virus updates */
    private void checkVirusUpdates(){

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URLS.CONFIRMED_CASES_URL).newBuilder();
        String url = urlBuilder.build().toString();

        final Request request = new Request.Builder().url(url).get().build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(@NotNull Call call, @NotNull IOException e) {
                catchCoronaException(e);
            }

            @Override
            public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {

                StringReader csvBodyReader = new StringReader(response.body().string());

                Iterable<CSVRecord> records = CSVFormat.DEFAULT.withFirstRecordAsHeader().parse(csvBodyReader);

                handleSuccess(records);
            }
        });

    }

    private void catchCoronaException(IOException exception){
        Log.i("REQUESTFAILED", "Request failed");
    }

    private void handleSuccess(Iterable<CSVRecord> records){

        Realm realm = Realm.getDefaultInstance();

        for (CSVRecord record : records) {

            saveOrUpdateRecord(record, realm);
        }
    }

    //handle create or update functions
    private void saveOrUpdateRecord(CSVRecord record, Realm realm){

        Log.d("RESULTEXT", record.get("Province/State"));

        String provinceState = record.get("Province/State");

        if (!provinceState.isEmpty()){

            CoronaRecord existing = realm.where(CoronaRecord.class).equalTo("province_state", provinceState).findFirst();

            if (existing != null){
                updateRecord(existing, record, realm);
            }else{
                createRecord(record, realm);
            }

        }

    }

    //do create for the record
    private void createRecord(CSVRecord record, Realm realm){

        RealmResults<CoronaRecord> allRecords = realm.where(CoronaRecord.class).findAll().sort("id", Sort.ASCENDING);

        long lastRecordId;

        final CoronaRecord coronaRecord = new CoronaRecord();

        if (allRecords.isEmpty()){
            coronaRecord.setId(1);
        }else{
            lastRecordId = allRecords.last().getId();
            coronaRecord.setId( lastRecordId + 1);
        }

        coronaRecord.setProvince_state(record.get("Province/State"));
        coronaRecord.setCountry_region(record.get("Country/Region"));
        coronaRecord.setLatitude(Double.parseDouble(record.get("Lat")));
        coronaRecord.setLongitude(Double.parseDouble(record.get("Long")));

        int confirmedCases = Integer.parseInt(record.get(record.size() - 1));
        int previousCases = Integer.parseInt(record.get(record.size() - 2));

        coronaRecord.setConfirmed_cases(confirmedCases);
        coronaRecord.setPrevious_day_cases(previousCases);

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.copyToRealm(coronaRecord);
            }
        });
    }

    //do updates for each record
    private void updateRecord(final CoronaRecord existing, final CSVRecord record, Realm realm){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                existing.setProvince_state(record.get("Province/State"));
                existing.setCountry_region(record.get("Country/Region"));
                existing.setLatitude(Double.parseDouble(record.get("Lat")));
                existing.setLongitude(Double.parseDouble(record.get("Long")));

                int confirmedCases = Integer.parseInt(record.get(record.size() - 1));
                int previousCases = Integer.parseInt(record.get(record.size() - 2));

                existing.setConfirmed_cases(confirmedCases);
                existing.setPrevious_day_cases(previousCases);

                realm.copyToRealmOrUpdate(existing);
            }
        });
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public boolean stopService(Intent name) {
        return super.stopService(name);
    }
}
