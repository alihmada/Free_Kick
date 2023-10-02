package ao.play.freekick.Fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import ao.play.freekick.Adapters.IndebtednessAdapter;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Classes.FirstItemMarginDecoration;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Indebtedness;
import ao.play.freekick.R;

public class ForHim extends Fragment {
    private SwipeRefreshLayout swipeRefreshLayout;
    List<Indebtedness> detailsList;
    RecyclerView recyclerView;
    ConstraintLayout alert;
    String id;

    public ForHim() {
    }

    public ForHim(String id) {
        this.id = id;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_for_him, container, false);

        initialize(view);
        setupRecycleView(view);
        getRecycleViewData();
        setupSwipeRefreshLayout(view);

        return view;
    }

    private void initialize(View view) {
        detailsList = new ArrayList<>();
        alert = view.findViewById(R.id.alert);
        alert.setVisibility(View.GONE);
    }

    private void setupSwipeRefreshLayout(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            getRecycleViewData();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupRecycleView(View view) {
        recyclerView = view.findViewById(R.id.for_him_recycler);
        recyclerView.addItemDecoration(new FirstItemMarginDecoration(getResources().getDimensionPixelSize(R.dimen.margin)));
    }

    private void setRecycleViewAdapter(IndebtednessAdapter adapter) {
        recyclerView.setAdapter(adapter);
    }

    private void getRecycleViewData() {
        Query getReference = Firebase.getDebt(requireContext()).orderByChild("id").equalTo(id);

        getReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                        Query query = dataSnapshot.getRef().orderByChild(Common.LETTER).equalTo("-");

                        query.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot snapshot) {
                                detailsList.clear();
                                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                                    try {
                                        Indebtedness customer = dataSnapshot.getValue(Indebtedness.class);
                                        if (customer != null) {
                                            detailsList.add(customer);
                                        }
                                    } catch (Exception ignored) {
                                    }
                                }

                                if (getContext() != null && detailsList.size() > 0) {
                                    setRecycleViewAdapter(new IndebtednessAdapter(id, getContext(), getParentFragmentManager(), detailsList));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}