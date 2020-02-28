package ke.co.simpledeveloper.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ke.co.simpledeveloper.R;

public class RecordsAdapter extends RecyclerView.Adapter<RecordViewHolder> {

    private List<RecordObject> recordObjects;
    private Context context;

    public RecordsAdapter(Context context, List<RecordObject> records){
        this.context = context;
        this.recordObjects = records;
    }

    @NonNull
    @Override
    public RecordViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.record_item_card, parent, false);
        return new RecordViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull RecordViewHolder holder, final int position) {

        RecordObject record = recordObjects.get(position);

        holder.description.setText(record.getDescription());
        holder.totalCases.setText(String.valueOf(record.getTotal_cases()));
        holder.province.setText(record.getProvince_state());
        holder.date.setText(record.getDate());

    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public int getItemCount() {
        return recordObjects.size();
    }

}