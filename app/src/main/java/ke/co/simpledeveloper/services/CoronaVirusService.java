package ke.co.simpledeveloper.services;

import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
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
import ke.co.simpledeveloper.db.CoronaCaseRecord;
import ke.co.simpledeveloper.db.CoronaDeathRecord;
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

                            getConfirmedDeaths();

                            getConfirmedCases();

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
    private void getConfirmedDeaths(){

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URLS.CONFIRMED_DEATHS_URL).newBuilder();
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
        //do not catch the virus!! :(
    }

    private void handleSuccess(Iterable<CSVRecord> records){

        Realm realm = Realm.getDefaultInstance();

        for (CSVRecord record : records) {

            saveOrUpdateRecord(record, realm);
        }
    }

    //handle create or update functions
    private void saveOrUpdateRecord(CSVRecord record, Realm realm){

        String provinceState = record.get("Province/State").trim();
        String countryRegion = record.get("Country/Region").trim();

        CoronaDeathRecord existing = null;

        if (!provinceState.isEmpty()){
            existing = realm.where(CoronaDeathRecord.class).equalTo("province_state", provinceState).findFirst();
        }else if (!countryRegion.isEmpty()){
            existing = realm.where(CoronaDeathRecord.class).equalTo("country_region", provinceState).findFirst();
        }

        if (existing != null){
            updateRecord(existing, record, realm);
        }else{
            createRecord(record, realm);
        }

    }

    //do create for the record
    private void createRecord(CSVRecord record, Realm realm){

        RealmResults<CoronaDeathRecord> allRecords = realm.where(CoronaDeathRecord.class).findAll().sort("id", Sort.ASCENDING);

        long lastRecordId;

        final CoronaDeathRecord coronaRecord = new CoronaDeathRecord();

        if (allRecords.isEmpty()){
            coronaRecord.setId(1);
        }else{
            lastRecordId = allRecords.last().getId();
            coronaRecord.setId( lastRecordId + 1);
        }

        String provinceState = record.get("Province/State").trim();

        if (provinceState.isEmpty()){
            coronaRecord.setProvince_state("");
        }else{
            coronaRecord.setProvince_state(provinceState);
        }

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
    private void updateRecord(final CoronaDeathRecord existing, final CSVRecord record, Realm realm){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {

                int confirmedCases = Integer.parseInt(record.get(record.size() - 1));
                int previousCases = Integer.parseInt(record.get(record.size() - 2));

                existing.setConfirmed_cases(confirmedCases);
                existing.setPrevious_day_cases(previousCases);

                realm.copyToRealmOrUpdate(existing);
            }
        });
    }

    /* Query for corona virus updates */
    private void getConfirmedCases(){

        OkHttpClient client = new OkHttpClient();

        HttpUrl.Builder urlBuilder = HttpUrl.parse(URLS.CONFIRMED_DAILY_REPORTS).newBuilder();
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

                handleCasesSuccess(records);
            }
        });

    }

    private void handleCasesSuccess(Iterable<CSVRecord> records){

        Realm realm = Realm.getDefaultInstance();

        for (CSVRecord record : records) {

            saveOrUpdateCaseRecord(record, realm);
        }
    }

    //handle create or update functions
    private void saveOrUpdateCaseRecord(CSVRecord record, Realm realm){

        String provinceState = record.get("Province/State").trim();
        String countryRegion = record.get("Country/Region").trim();

        CoronaCaseRecord existing = null;

        if (!provinceState.isEmpty()){
            existing = realm.where(CoronaCaseRecord.class).equalTo("province_state", provinceState).findFirst();
        }else if (!countryRegion.isEmpty()){
            existing = realm.where(CoronaCaseRecord.class).equalTo("country_region", provinceState).findFirst();
        }

        if (existing != null){
            updateCaseRecord(existing, record, realm);
        }else{
            createCaseRecord(record, realm);
        }

    }

    //update
    private void createCaseRecord(CSVRecord record, Realm realm){

        RealmResults<CoronaCaseRecord> allRecords = realm.where(CoronaCaseRecord.class).findAll().sort("id", Sort.ASCENDING);

        long lastRecordId;

        final CoronaCaseRecord coronaRecord = new CoronaCaseRecord();

        if (allRecords.isEmpty()){
            coronaRecord.setId(1);
        }else{
            lastRecordId = allRecords.last().getId();
            coronaRecord.setId( lastRecordId + 1);
        }

        String provinceState = record.get("Province/State").trim();
        String countryRegion = record.get("Country/Region").trim();

        if (provinceState.isEmpty()){
            coronaRecord.setProvince_state("");
        }else{
            coronaRecord.setProvince_state(provinceState);
        }

        coronaRecord.setCountry_region(countryRegion);
        coronaRecord.setLast_update(record.get("Last Update"));
        coronaRecord.setConfirmed_recovered(Integer.parseInt(record.get("Recovered")));
        coronaRecord.setConfirmed_cases(Integer.parseInt(record.get("Confirmed")));
        coronaRecord.setConfirmed_deaths(Integer.parseInt(record.get("Deaths")));

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {
                realm.copyToRealm(coronaRecord);
            }
        });
    }

    //update
    private void updateCaseRecord(final CoronaCaseRecord existing, final CSVRecord record, Realm realm){

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(@NotNull Realm realm) {

                existing.setLast_update(record.get("Last Update"));
                existing.setConfirmed_recovered(Integer.parseInt(record.get("Recovered")));
                existing.setConfirmed_cases(Integer.parseInt(record.get("Confirmed")));
                existing.setConfirmed_deaths(Integer.parseInt(record.get("Deaths")));

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
