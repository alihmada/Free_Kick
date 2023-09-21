package ao.play.freekick.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ao.play.freekick.Adapters.DebtDetailsAdapter;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.CustomerDetails;
import ao.play.freekick.R;

public class ForHim extends Fragment {
    ConstraintLayout alert;
    List<CustomerDetails> detailsList;
    RecyclerView recyclerView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_him, container, false);

        initialize(view);
        setupRecycleView(view);
        getRecycleViewData();

        return view;
    }

    private void initialize(View view) {
        detailsList = new ArrayList<>();
        alert = view.findViewById(R.id.alert);
        alert.setVisibility(View.GONE);
    }

    private void setupRecycleView(View view) {
        recyclerView = view.findViewById(R.id.for_him_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void setRecycleViewAdapter(DebtDetailsAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void getRecycleViewData() {
        Query query = Debts.reference.orderByChild(Common.LETTER).equalTo("-");

        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                detailsList.clear();
                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    try {
                        CustomerDetails customer = dataSnapshot.getValue(CustomerDetails.class);
                        if (customer != null) {
                            detailsList.add(customer);
                        }
                    } catch (Exception ignored) {
                    }
                }

                if (detailsList.size() > 0) {
                    setRecycleViewAdapter(new DebtDetailsAdapter(requireContext(), detailsList));
                    alert.setVisibility(View.GONE);
                } else {
                    alert.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}