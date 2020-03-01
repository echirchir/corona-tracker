package ke.co.simpledeveloper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ke.co.simpledeveloper.R;
import ke.co.simpledeveloper.db.CoronaCaseRecord;
import ke.co.simpledeveloper.db.CoronaDeathRecord;

public class MapFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap mMap;

    public MapFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        mMap = googleMap;

        Realm realm = Realm.getDefaultInstance();

        RealmResults<CoronaDeathRecord> records = realm.where(CoronaDeathRecord.class).findAll().sort("id", Sort.ASCENDING);

        if (!records.isEmpty()){

            for (CoronaDeathRecord record : records){

                String provinceState = record.getProvince_state();
                String countryRegion = record.getCountry_region();

                LatLng state = new LatLng(record.getLatitude(), record.getLongitude());
                CoronaCaseRecord caseRecord = null;

                if (!provinceState.isEmpty()){
                    caseRecord = realm.where(CoronaCaseRecord.class).equalTo("province_state", provinceState).findFirst();
                    if (caseRecord != null){
                        mMap.addMarker(new MarkerOptions().position(state).title(caseRecord.getProvince_state().concat(" - confirmed cases: ").concat(String.valueOf(caseRecord.getConfirmed_cases())).concat(" and deaths: ").concat(String.valueOf(caseRecord.getConfirmed_deaths()))));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(state));
                }else if (!countryRegion.isEmpty()){
                    caseRecord = realm.where(CoronaCaseRecord.class).equalTo("country_region", countryRegion).findFirst();
                    if (caseRecord != null){
                        mMap.addMarker(new MarkerOptions().position(state).title(caseRecord.getCountry_region().concat(" - confirmed cases: ").concat(String.valueOf(caseRecord.getConfirmed_cases())).concat(" and deaths: ").concat(String.valueOf(caseRecord.getConfirmed_deaths()))));
                    }
                    mMap.moveCamera(CameraUpdateFactory.newLatLng(state));
                }
            }
        }

    }

}
