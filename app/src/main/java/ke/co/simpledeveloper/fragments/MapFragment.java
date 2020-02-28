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
import ke.co.simpledeveloper.db.CoronaRecord;

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

        RealmResults<CoronaRecord> records = realm.where(CoronaRecord.class).findAll().sort("id", Sort.ASCENDING);

        if (!records.isEmpty()){

            for (CoronaRecord record : records){
                LatLng state = new LatLng(record.getLatitude(), record.getLongitude());
                mMap.addMarker(new MarkerOptions().position(state).title(record.getProvince_state().concat(": ").concat(String.valueOf(record.getConfirmed_cases()))));
                mMap.moveCamera(CameraUpdateFactory.newLatLng(state));
            }
        }

    }

}
