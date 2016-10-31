package lk.ac.mrt.labphone;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import lk.ac.mrt.labphone.fragment.AngleFragment;
import lk.ac.mrt.labphone.fragment.FrequencyFragment;
import lk.ac.mrt.labphone.fragment.HeightFragment;
import lk.ac.mrt.labphone.fragment.LevelFragment;
import lk.ac.mrt.labphone.fragment.VoltageFragment;

public class MainActivity extends AppCompatActivity {

    public static final String MAIN = "main";

    private String[] menuTexts;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

//        LevelFragment fragment = new LevelFragment();
//        navigateToFragment(Constants.EXPERIMENTS.LEVEL.name(), fragment);

//        AngleFragment fragment = new AngleFragment();
//        navigateToFragment(Constants.EXPERIMENTS.ANGLE.name(), fragment);


//        findViewById(R.id.button_level).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                LevelFragment fragment = new LevelFragment();
//                navigateToFragment(Constants.EXPERIMENTS.LEVEL.name(), fragment);
//            }
//        });

        menuTexts = getResources().getStringArray(R.array.menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        final ListView drawerList = (ListView) findViewById(R.id.list_drawer);

        drawerList.setAdapter(new ArrayAdapter<>(this, R.layout.drawer_row, menuTexts));
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                handleMenuSelect(drawerList, position);
            }
        });

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
//                getSupportActionBar().setTitle(getTitle());
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle(getTitle());
//                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };
        drawerLayout.addDrawerListener(drawerToggle);
        drawerToggle.syncState();
    }

    private void handleMenuSelect(ListView drawerList, int position) {
        // Create a new fragment
        Fragment fragment = null;
        switch (position) {
            case 0:
                fragment = new VoltageFragment();
                break;
            case 1:
                fragment = new HeightFragment();
                break;
            case 2:
                fragment = new FrequencyFragment();
                break;
            case 3:
                fragment = new AngleFragment();
                break;
            case 4:
                fragment = new LevelFragment();
                break;
        }

        if (fragment != null) {
            // Insert the fragment by replacing any existing fragment
            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.content_frame, fragment)
                    .commit();

            // Highlight the selected item, update the title, and close the drawer
            drawerList.setItemChecked(position, true);
            setTitle(menuTexts[position]);
            drawerLayout.closeDrawer(drawerLayout.findViewById(R.id.left_drawer), true);
        }
    }

    @Override
    public void setTitle(CharSequence title) {
        getSupportActionBar().setTitle(title);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
