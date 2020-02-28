package ke.co.simpledeveloper.adapters;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import ke.co.simpledeveloper.R;

public class RecordViewHolder extends RecyclerView.ViewHolder {

    public TextView province;

    public TextView totalCases;

    public TextView date;

    public TextView description;

    public RecordViewHolder(@NonNull View itemView) {
        super(itemView);

        date = itemView.findViewById(R.id.date);
        province = itemView.findViewById(R.id.province);
        totalCases = itemView.findViewById(R.id.total_cases);
        description = itemView.findViewById(R.id.description);
    }
}
