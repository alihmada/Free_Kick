package ao.play.freekick.Activities;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import ao.play.freekick.Adapters.ControllersAdapter;
import ao.play.freekick.Classes.Capture;
import ao.play.freekick.Classes.EncryptionAndDecryption;
import ao.play.freekick.Data.Firebase;
import ao.play.freekick.Models.Common;
import ao.play.freekick.Models.Controller;
import ao.play.freekick.R;

public class Controllers extends AppCompatActivity {

    RecyclerView recyclerView;
    ActivityResultLauncher<ScanOptions> scanOptionsActivityResultLauncher = registerForActivityResult(new ScanContract(), result -> {
        if (result.getContents() != null) {
            String code = null;
            try {
                code = EncryptionAndDecryption.decrypt(result.getContents());
            } catch (Exception ignored) {
                Toast.makeText(this, result.getContents(), Toast.LENGTH_LONG).show();
            }

            assert code != null;
            if (code.matches("controller\\d+")) {

                getScannedController(code);
            }
        }
    }); // End of registerForActivityResult()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controllers);

        setCustomActionBar();

        recyclerView = findViewById(R.id.controllers_recycler);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        getScannedController(getIntent().getStringExtra(Common.CODE));
    } // End of onCreate()

    private void setCustomActionBar() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setCustomView(R.layout.tool_bar);
    } // End of setCustomActionBar()

    private void getScannedController(String controller) {
        List<Controller> controllers = new ArrayList<>();

        Firebase.getController().child(controller).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                controllers.add(snapshot.getValue(Controller.class));

                ControllersAdapter adapter = new ControllersAdapter(controllers);
                recyclerView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    } // End of getScannedController()

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.controller_menu, menu);
        return true;
    } // End of onCreateOptionsMenu()

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();

        if (itemId == R.id.scan) {
            ScanOptions scanOptions = new ScanOptions();
            scanOptions.setBeepEnabled(true).setOrientationLocked(true).setCaptureActivity(Capture.class);
            scanOptionsActivityResultLauncher.launch(scanOptions);
        }
        return super.onOptionsItemSelected(item);

    } // End of onOptionsItemSelected()

} // End of Controllers()