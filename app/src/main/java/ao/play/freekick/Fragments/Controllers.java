package ao.play.freekick.Fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;

import ao.play.freekick.Adapters.ControllerAdapter;
import ao.play.freekick.Classes.Capture;
import ao.play.freekick.Classes.Ciphering;
import ao.play.freekick.Classes.Common;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Dialogs.Loading;
import ao.play.freekick.Intenet.Internet;
import ao.play.freekick.Interfaces.ViewOnClickListener;
import ao.play.freekick.Models.Controller;
import ao.play.freekick.R;

public class Controllers extends Fragment implements ViewOnClickListener {

    private SwipeRefreshLayout swipeRefreshLayout;
    ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher;
    private List<Controller> controllerList;
    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private ConstraintLayout alert;
    private ImageView imageView;
    private TextView textView;

    public Controllers() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        setHasOptionsMenu(true);

        View view = inflater.inflate(R.layout.fragment_controllers, container, false);

        Loading.progressDialogConstructor(requireContext());

        controllerList = new ArrayList<>();

        alert = view.findViewById(R.id.alert);

        imageView = view.findViewById(R.id.alert_image);
        textView = view.findViewById(R.id.alert_text);

        progressBar = view.findViewById(R.id.progressBar);

        recyclerView = view.findViewById(R.id.controllers_recycler);

        setupScanOptionsActivityResultLauncher();
        setupSwipeRefreshLayout(view);
        setupView();

        return view;
    }

    private void setupSwipeRefreshLayout(View view) {
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);

        swipeRefreshLayout.setOnRefreshListener(() -> {
            setupView();
            swipeRefreshLayout.setRefreshing(false);
        });
    }

    private void setupView() {
        if (Internet.isConnectedWithoutMessage(requireContext())) {
            if (Internet.isNetworkLimited(requireContext())) {
                setupWifi(getString(R.string.internet_limited));
            } else {
                alert.setVisibility(View.GONE);
                setRecyclerView();
            }
        } else if (controllerList.size() == 0) {
            setupWifi(getString(R.string.no_internet));
        }
    }

    private void setupScanOptionsActivityResultLauncher() {
        scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
            if (result.getContents() != null) {
                Loading.showProgressDialog();
                String code = null;
                try {
                    code = Ciphering.decrypt(result.getContents());
                } catch (Exception ignored) {
                    Toast.makeText(requireContext(), result.getContents(), Toast.LENGTH_LONG).show();
                }

                assert code != null;
                if (code.matches("controller\\d+")) {
                    getController(code);
                }
            }
        });
    } // End of registerForActivityResult()

    private void setupWifi(String message) {
        alert.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.GONE);
        imageView.setImageDrawable(AppCompatResources.getDrawable(requireContext(), R.drawable.wifi_off));
        textView.setText(message);
    }

    private void setRecyclerView() {
        Firebase.getController(requireContext()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                controllerList.clear();
                progressBar.setVisibility(View.VISIBLE);

                for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
                    Controller controller = dataSnapshot.getValue(Controller.class);
                    if (controller != null) {
                        controllerList.add(controller);
                    }
                }

                if (!controllerList.isEmpty()) {
                    Controller controller = controllerList.get(1);
                    controllerList.remove(1);
                    controllerList.add(controller);
                    if (isAdded()) {
                        recyclerView.setAdapter(new ControllerAdapter(requireContext(), controllerList, Controllers.this));
                        progressBar.setVisibility(View.GONE);
                        alert.setVisibility(View.GONE);
                    }
                } else {
                    if (isAdded()) {
                        recyclerView.setAdapter(new ControllerAdapter(requireContext(), controllerList, Controllers.this));
                        alert.setVisibility(View.VISIBLE);
                        progressBar.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        });
    }

    private void getController(String name) {

        Intent intent = new Intent(requireContext(), ao.play.freekick.Activities.Controllers.class);
        intent.putExtra(Common.NAME, name);
        startActivity(intent);

        Loading.dismissProgressDialog();
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.controller_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        if (item.getItemId() == R.id.report) {
//
//            return true;
//        } else
        if (item.getItemId() == R.id.scan) {
            try {
                setupScanner();
            } catch (IllegalStateException e) {
                setupScanOptionsActivityResultLauncher();
                setupScanner();
            }
            return true;
        } else {
            return super.onOptionsItemSelected(item);
        }
    }

    private void setupScanner() {
        ScanOptions scanOptions = new ScanOptions();
        scanOptions.setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);
        scanOptionsActivityResultLauncher.launch(scanOptions);
    }

    @Override
    public void onClickListener(String name, String empty) {
        getController(name);
    }
}