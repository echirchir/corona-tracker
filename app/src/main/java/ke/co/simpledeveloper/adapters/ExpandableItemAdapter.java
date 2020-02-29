package ke.co.simpledeveloper.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ke.co.simpledeveloper.R;
import ke.co.simpledeveloper.customui.CoronaTextView;
import ke.co.simpledeveloper.customui.ExpandableLinearLayout;
import ke.co.simpledeveloper.helpers.ExpandListener;

public class ExpandableItemAdapter extends RecyclerView.Adapter<ExpandableItemAdapter.ViewHolder>{

    private List<ExpandableObject> objects;
    private RecyclerView recyclerView;
    private int lastExpandedCardPosition;

    public ExpandableItemAdapter(RecyclerView recyclerView, List<ExpandableObject> models) {
        this.recyclerView = recyclerView;
        this.objects = models;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.expandable_card_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        holder.question.setText(objects.get(position).getQuestion());
        holder.answer.setText(objects.get(position).getAnswer());

        if(objects.get(position).isExpanded()){
            holder.expandableView.setVisibility(View.VISIBLE);
            holder.expandableView.setExpanded(true);
        }
        else{
            holder.expandableView.setVisibility(View.GONE);
            holder.expandableView.setExpanded(false);
        }
    }

    @Override
    public int getItemCount() {
        return objects.size();
    }

    public void setData(List<ExpandableObject> data) {
        this.objects = data;
    }

    public void addItem(int i) {
        objects.add(i, new ExpandableObject());
        if(i <= lastExpandedCardPosition)
            lastExpandedCardPosition++;
        notifyDataSetChanged();
    }

    public void deleteItem(int i) {
        objects.remove(i);
        notifyDataSetChanged();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ExpandableLinearLayout expandableView;
        CoronaTextView question;
        CoronaTextView answer;

        ExpandListener expandListener = new ExpandListener() {
            @Override
            public void onExpandComplete() {
                if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)!=null){
                    ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).setExpanded(false);
                    objects.get(lastExpandedCardPosition).setExpanded(false);
                    ((ExpandableLinearLayout)recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition).itemView.findViewById(R.id.expandableView)).toggle();
                }
                else if(lastExpandedCardPosition!=getAdapterPosition() && recyclerView.findViewHolderForAdapterPosition(lastExpandedCardPosition)==null){
                    objects.get(lastExpandedCardPosition).setExpanded(false);
                }
                lastExpandedCardPosition = getAdapterPosition();
            }

            @Override
            public void onCollapseComplete() {

            }
        };

        ViewHolder(View itemView) {
            super(itemView);
            question = itemView.findViewById(R.id.question);
            answer = itemView.findViewById(R.id.answer);
            expandableView = itemView.findViewById(R.id.expandableView);
            //expandableView.setExpandListener(expandListener);
            initializeClicks();
        }

        private void initializeClicks() {
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (expandableView.isExpanded()) {
                        expandableView.setExpanded(false);
                        expandableView.toggle();
                        objects.get(getAdapterPosition()).setExpanded(false);
                    } else {
                        expandableView.setExpanded(true);
                        objects.get(getAdapterPosition()).setExpanded(true);
                        expandableView.toggle();
                    }
                }
            });
        }
    }
}
