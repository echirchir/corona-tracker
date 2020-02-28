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
import ke.co.simpledeveloper.constants.URLS;
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

            static final long DELAY = 3000000;

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
                        Toast.makeText(CoronaVirusService.this, "Turn ON your data bundles to connect!", Toast.LENGTH_SHORT).show();
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

    private void saveOrUpdateRecord(CSVRecord record, Realm realm){

        Log.d("RESULTEXT", record.get("Province/State"));

        String state = record.get("Province/State");

        if (!state.isEmpty()){



        }

    }

    private void createRecord(CSVRecord record, Realm realm){

        int latestCases = Integer.parseInt(record.get(record.size() - 1));
        int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
        Log.d("RESULTEXT", record.get("Province/State"));
        Log.d("RESULTEXT", record.get("Country/Region"));
        Log.d("RESULTEXT", record.get("Lat"));
        Log.d("RESULTEXT", record.get("Long"));
    }

    private void updateRecord(CSVRecord record, Realm realm){

        int latestCases = Integer.parseInt(record.get(record.size() - 1));
        int prevDayCases = Integer.parseInt(record.get(record.size() - 2));
        Log.d("RESULTEXT", record.get("Province/State"));
        Log.d("RESULTEXT", record.get("Country/Region"));
        Log.d("RESULTEXT", record.get("Lat"));
        Log.d("RESULTEXT", record.get("Long"));
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
