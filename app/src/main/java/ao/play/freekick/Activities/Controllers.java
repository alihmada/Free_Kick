package ao.play.freekick.Activities;

import android.os.Bundle;
import android.util.TypedValue;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;

import java.util.Objects;

import ao.play.freekick.Adapters.FragmentViewPagerAdapter;
import ao.play.freekick.Fragments.Buttons;
import ao.play.freekick.Fragments.Inside;
import ao.play.freekick.Fragments.Outside;
import ao.play.freekick.Models.Common;
import ao.play.freekick.R;

public class Controllers extends AppCompatActivity {
    TabLayout tabLayout;
    ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_controllers);

        Objects.requireNonNull(getSupportActionBar()).hide();
        getWindow().setStatusBarColor(getPrimaryColor());

        setupHeader();

        setupViewPager();
    } // End of onCreate()

    private void setupHeader() {
        TextView id = findViewById(R.id.id);
        id.setText(ao.play.freekick.Fragments.Controllers.controller.getId());

        TextView name = findViewById(R.id.name);
        String controllerId = getIntent().getStringExtra(Common.ID);
        name.setText(String.format("%s %s", getString(R.string.controller_no), controllerId));

        TextView time = findViewById(R.id.time);
        time.setText(ao.play.freekick.Fragments.Controllers.controller.getTimeOfLastRepair());
    }

    private void setupViewPager() {
        viewPager = findViewById(R.id.view_pager);
        tabLayout = findViewById(R.id.tab_layout);

        tabLayout.setupWithViewPager(viewPager);

        FragmentViewPagerAdapter ordersViewPagerAdapter = new FragmentViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        ordersViewPagerAdapter.addFragment(new Buttons(), getString(R.string.buttons));
        ordersViewPagerAdapter.addFragment(new Outside(), getString(R.string.outside));
        ordersViewPagerAdapter.addFragment(new Inside(), getString(R.string.inside));

        viewPager.setAdapter(ordersViewPagerAdapter);
    }

    private int getPrimaryColor() {
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(androidx.appcompat.R.attr.colorPrimary, typedValue, true);
        return typedValue.data;
    }
} // End of Controllers()