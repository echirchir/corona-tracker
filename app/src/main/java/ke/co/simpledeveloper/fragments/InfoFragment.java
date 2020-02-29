package ke.co.simpledeveloper.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import ke.co.simpledeveloper.R;
import ke.co.simpledeveloper.adapters.ExpandableItemAdapter;
import ke.co.simpledeveloper.adapters.ExpandableObject;

public class InfoFragment extends Fragment {

    private ExpandableItemAdapter adapter;
    private List<ExpandableObject> models;
    private RecyclerView recyclerView;

    public InfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_info, container, false);

        recyclerView = rootView.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.setHasFixedSize(true);

        setupListItems();

        return rootView;

    }

    private void setupListItems(){

        models = new ArrayList<>();

        ExpandableObject model1 = new ExpandableObject();
        model1.setQuestion(getString(R.string.question_1));
        model1.setAnswer(getString(R.string.answer_1));
        model1.setExpanded(false);

        ExpandableObject model2 = new ExpandableObject();
        model2.setQuestion(getString(R.string.question_2));
        model2.setAnswer(getString(R.string.answer_2));
        model2.setExpanded(false);

        ExpandableObject model3 = new ExpandableObject();
        model3.setQuestion(getString(R.string.question_3));
        model3.setAnswer(getString(R.string.answer_3));
        model3.setExpanded(false);

        ExpandableObject model5 = new ExpandableObject();
        model5.setQuestion(getString(R.string.question_5));
        model5.setAnswer(getString(R.string.answer_5));
        model5.setExpanded(false);

        ExpandableObject model6 = new ExpandableObject();
        model6.setQuestion(getString(R.string.question_6));
        model6.setAnswer(getString(R.string.answer_6));
        model6.setExpanded(false);

        ExpandableObject model7 = new ExpandableObject();
        model7.setQuestion(getString(R.string.question_7));
        model7.setAnswer(getString(R.string.answer_7));
        model7.setExpanded(false);

        ExpandableObject model8 = new ExpandableObject();
        model8.setQuestion(getString(R.string.question_8));
        model8.setAnswer(getString(R.string.answer_8));
        model8.setExpanded(false);

        ExpandableObject model9 = new ExpandableObject();
        model9.setQuestion(getString(R.string.question_9));
        model9.setAnswer(getString(R.string.answer_9));
        model9.setExpanded(false);

        ExpandableObject model10 = new ExpandableObject();
        model10.setQuestion(getString(R.string.question_10));
        model10.setAnswer(getString(R.string.answer_10));
        model10.setExpanded(false);

        ExpandableObject model11 = new ExpandableObject();
        model11.setQuestion(getString(R.string.question_11));
        model11.setAnswer(getString(R.string.answer_11));
        model11.setExpanded(false);

        ExpandableObject model12 = new ExpandableObject();
        model12.setQuestion(getString(R.string.question_12));
        model12.setAnswer(getString(R.string.answer_12));
        model12.setExpanded(false);

        ExpandableObject model13 = new ExpandableObject();
        model13.setQuestion(getString(R.string.question_13));
        model13.setAnswer(getString(R.string.answer_13));
        model13.setExpanded(false);

        ExpandableObject model14 = new ExpandableObject();
        model14.setQuestion(getString(R.string.question_14));
        model14.setAnswer(getString(R.string.answer_14));
        model14.setExpanded(false);

        ExpandableObject model15 = new ExpandableObject();
        model15.setQuestion(getString(R.string.question_15));
        model15.setAnswer(getString(R.string.answer_15));
        model15.setExpanded(false);

        ExpandableObject model16 = new ExpandableObject();
        model16.setQuestion(getString(R.string.question_16));
        model16.setAnswer(getString(R.string.answer_16));
        model16.setExpanded(false);

        ExpandableObject model17 = new ExpandableObject();
        model17.setQuestion(getString(R.string.question_17));
        model17.setAnswer(getString(R.string.answer_17));
        model17.setExpanded(false);

        ExpandableObject model18 = new ExpandableObject();
        model18.setQuestion(getString(R.string.question_18));
        model18.setAnswer(getString(R.string.answer_18));
        model18.setExpanded(false);

        ExpandableObject model19 = new ExpandableObject();
        model19.setQuestion(getString(R.string.question_19));
        model19.setAnswer(getString(R.string.answer_19));
        model19.setExpanded(false);

        models.add(model1);
        models.add(model2);
        models.add(model3);
        models.add(model5);
        models.add(model6);
        models.add(model7);
        models.add(model8);
        models.add(model9);
        models.add(model10);
        models.add(model11);
        models.add(model12);
        models.add(model13);
        models.add(model14);
        models.add(model15);
        models.add(model16);
        models.add(model17);
        models.add(model18);
        models.add(model19);

        adapter = new ExpandableItemAdapter(recyclerView, models);

        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();
    }
}
