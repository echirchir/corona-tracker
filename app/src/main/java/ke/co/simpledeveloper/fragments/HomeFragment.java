package ke.co.simpledeveloper.fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import io.realm.Sort;
import ke.co.simpledeveloper.R;
import ke.co.simpledeveloper.adapters.RecordObject;
import ke.co.simpledeveloper.adapters.RecordsAdapter;
import ke.co.simpledeveloper.customui.CoronaTextView;
import ke.co.simpledeveloper.db.CoronaCaseRecord;
import ke.co.simpledeveloper.helpers.Helpers;

public class HomeFragment extends Fragment {

    private RecordsAdapter adapter;
    private List<RecordObject> records;
    private RecyclerView recyclerView;
    private CoronaTextView generic_empty_view;

    private Realm realm;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        realm = Realm.getDefaultInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_home, container, false);

        recyclerView = rootView.findViewById(R.id.recycler);
        generic_empty_view = rootView.findViewById(R.id.generic_empty_view);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        setupListOfRecords();

        return rootView;
    }

    private void setupListOfRecords(){

        records = new ArrayList<>();

        RealmResults<CoronaCaseRecord> allRecords = realm.where(CoronaCaseRecord.class).findAll().sort("confirmed_cases", Sort.DESCENDING);

        if (!allRecords.isEmpty()){

            for (CoronaCaseRecord record : allRecords){

                RecordObject recordObject = new RecordObject();

                if (record.getProvince_state().isEmpty()){
                    recordObject.setProvince_state(record.getCountry_region());
                }else{
                    recordObject.setProvince_state(record.getProvince_state());
                }

                recordObject.setId(record.getId());
                recordObject.setCountry_region(record.getCountry_region());
                recordObject.setDate(Helpers.getCurrentDateFormatted());
                String totalCasesOverDeaths = "".concat(String.valueOf(record.getConfirmed_cases()).concat(" / ").concat(String.valueOf(record.getConfirmed_deaths())));
                recordObject.setTotal_cases(totalCasesOverDeaths);

                String description = recordObject.getProvince_state().concat(" in ").concat(record.getCountry_region()).concat(" has recorded a total of ".concat(String.valueOf(record.getConfirmed_cases())).concat(" cases "));

                recordObject.setDescription(description);

                records.add(recordObject);

            }
        }

        adapter = new RecordsAdapter(getActivity(), records);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        if (adapter.getItemCount() > 0){

            recyclerView.setVisibility(View.VISIBLE);
            generic_empty_view.setVisibility(View.GONE);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        realm.close();
    }
}
